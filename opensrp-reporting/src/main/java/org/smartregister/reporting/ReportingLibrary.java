package org.smartregister.reporting;

import android.support.annotation.NonNull;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.reporting.domain.IndicatorQuery;
import org.smartregister.reporting.domain.IndicatorYamlConfigItem;
import org.smartregister.reporting.domain.IndicatorsYamlConfig;
import org.smartregister.reporting.domain.ReportIndicator;
import org.smartregister.reporting.processor.DefaultMultiResultProcessor;
import org.smartregister.reporting.processor.MultiResultProcessor;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;
import org.smartregister.reporting.util.AppProperties;
import org.smartregister.reporting.util.Constants;
import org.smartregister.reporting.util.ReportingUtil;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.Repository;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ReportingLibrary {

    private static boolean appOnDebugMode;
    private static ReportingLibrary instance;
    private Repository repository;
    private DailyIndicatorCountRepository dailyIndicatorCountRepository;
    private IndicatorQueryRepository indicatorQueryRepository;
    private IndicatorRepository indicatorRepository;
    private EventClientRepository eventClientRepository;
    private Context context;
    private CommonFtsObject commonFtsObject;
    private int applicationVersion;
    private int databaseVersion;
    private Yaml yaml;
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private AppProperties appProperties;

    private ArrayList<MultiResultProcessor> multiResultProcessors;
    private MultiResultProcessor defaultMultiResultProcessor;

    private ReportingLibrary(Context context, Repository repository, CommonFtsObject commonFtsObject, int applicationVersion, int databaseVersion) {
        this.repository = repository;
        this.context = context;
        this.commonFtsObject = commonFtsObject;
        this.applicationVersion = applicationVersion;
        this.databaseVersion = databaseVersion;
        initRepositories();
        this.appProperties = ReportingUtil.getProperties(this.context.applicationContext());

        this.multiResultProcessors = new ArrayList<>();
        this.defaultMultiResultProcessor = new DefaultMultiResultProcessor();

        // Install a default Timber tree in case the importing client app does not do that
        // This should be removed when a unified Timber Tree is agreed on for OpenSRP Core and client apps usage
        installDefaultTimberTree();

        initRepositories();
    }

    private void installDefaultTimberTree() {
        if (Timber.treeCount() == 0) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static void init(Context context, Repository repository, CommonFtsObject commonFtsObject, int applicationVersion, int databaseVersion) {
        if (instance == null) {
            instance = new ReportingLibrary(context, repository, commonFtsObject, applicationVersion, databaseVersion);
        }
        appOnDebugMode = BuildConfig.DEBUG;
    }

    public static ReportingLibrary getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Instance does not exist!!! Call " + ReportingLibrary.class.getName() + ".init() in the onCreate() method of your Application class");
        }
        return instance;
    }

    private void initRepositories() {
        this.dailyIndicatorCountRepository = new DailyIndicatorCountRepository(getRepository());
        this.indicatorQueryRepository = new IndicatorQueryRepository(getRepository());
        this.indicatorRepository = new IndicatorRepository(getRepository());
        this.eventClientRepository = new EventClientRepository(getRepository());
    }

    /**
     * This method should be called in onUpgrade method of the Repository class where the migrations
     * are already managed instead of writing new code to manage them
     */
    public void performMigrations(@NonNull SQLiteDatabase database) {
        Timber.i("Running Reporting Library migrations");

        IndicatorQueryRepository.performMigrations(database);
        DailyIndicatorCountRepository.performMigrations(database);
    }

    public Repository getRepository() {
        return repository;
    }

    public DailyIndicatorCountRepository dailyIndicatorCountRepository() {
        if (dailyIndicatorCountRepository == null) {
            dailyIndicatorCountRepository = new DailyIndicatorCountRepository(getRepository());
        }

        return dailyIndicatorCountRepository;
    }

    public IndicatorQueryRepository indicatorQueryRepository() {
        if (indicatorQueryRepository == null) {
            indicatorQueryRepository = new IndicatorQueryRepository(getRepository());
        }

        return indicatorQueryRepository;
    }

    public IndicatorRepository indicatorRepository() {
        if (indicatorRepository == null) {
            indicatorRepository = new IndicatorRepository(getRepository());
        }

        return indicatorRepository;
    }

    public EventClientRepository eventClientRepository() {
        if (eventClientRepository == null) {
            eventClientRepository = new EventClientRepository(getRepository());
        }

        return eventClientRepository;
    }

    public Context getContext() {
        return context;
    }

    public CommonFtsObject getCommonFtsObject() {
        return commonFtsObject;
    }

    public int getApplicationVersion() {
        return applicationVersion;
    }

    public int getDatabaseVersion() {
        return databaseVersion;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /***
     * Method that initializes the indicator queries.
     * Indicator queries are only read once on release mode but when on debug queries are refreshed
     * with content from the config files
     * @param configFilePath path of file containing the indicator definitions
     * @param sqLiteDatabase database to write the content obtained from the file
     */
    public void initIndicatorData(String configFilePath, SQLiteDatabase sqLiteDatabase) {
        if (hasInitializedIndicators(sqLiteDatabase)) {
            return;
        }
        readConfigFile(configFilePath, sqLiteDatabase);
    }

    private boolean hasInitializedIndicators(SQLiteDatabase sqLiteDatabase) {
        if (!appOnDebugMode && (!isAppUpdated() || isIndicatorsInitialized())) {
            return true;
        }
        truncateIndicatorDefinitionTables(sqLiteDatabase);
        return false;
    }

    public void truncateIndicatorDefinitionTables(SQLiteDatabase sqLiteDatabase) {
        if (sqLiteDatabase != null) {
            indicatorRepository.truncateTable(sqLiteDatabase);
            indicatorQueryRepository.truncateTable(sqLiteDatabase);
        } else {
            indicatorRepository.truncateTable();
            indicatorQueryRepository.truncateTable();
        }
    }

    public void readConfigFile(String configFilePath, SQLiteDatabase sqLiteDatabase) {
        initYamlIndicatorConfig();
        Iterable<Object> indicatorsFromFile = null;
        try {
            indicatorsFromFile = loadIndicatorsFromFile(configFilePath);
        } catch (IOException ioe) {
            Log.e("SampleApplication", ioe.getMessage());
        }
        if (indicatorsFromFile != null) {
            IndicatorsYamlConfig indicatorsConfig;
            List<ReportIndicator> reportIndicators = new ArrayList<>();
            List<IndicatorQuery> indicatorQueries = new ArrayList<>();
            ReportIndicator indicator;
            IndicatorQuery indicatorQuery;

            for (Object indicatorObject : indicatorsFromFile) {
                indicatorsConfig = (IndicatorsYamlConfig) indicatorObject;
                for (IndicatorYamlConfigItem indicatorYamlConfigItem : indicatorsConfig.getIndicators()) {
                    indicator = new ReportIndicator(null, indicatorYamlConfigItem.getKey(), indicatorYamlConfigItem.getDescription(), null);

                    String query = indicatorYamlConfigItem.getIndicatorQuery();
                    if(!query.trim().isEmpty()) {
                        indicatorQuery = new IndicatorQuery(null, indicatorYamlConfigItem.getKey()
                                , indicatorYamlConfigItem.getIndicatorQuery()
                                , 0
                                , indicatorYamlConfigItem.isMultiResult()
                                , indicatorYamlConfigItem.getExpectedIndicators());
                        indicatorQueries.add(indicatorQuery);
                    }

                    reportIndicators.add(indicator);
                }
            }
            if (sqLiteDatabase != null) {
                saveIndicators(reportIndicators, sqLiteDatabase);
                saveIndicatorQueries(indicatorQueries, sqLiteDatabase);
            } else {
                saveIndicators(reportIndicators);
                saveIndicatorQueries(indicatorQueries);
            }

            context.allSharedPreferences().savePreference(Constants.PrefKey.INDICATOR_DATA_INITIALISED, "true");
            context.allSharedPreferences().savePreference(Constants.PrefKey.APP_VERSION_CODE, String.valueOf(BuildConfig.VERSION_CODE));
        }
    }

    /**
     * Method to initialize multiple files
     * @param configFiles configuration files for the indicators
     * @param sqLiteDatabase database to store the indicator queries
     */
    public void initMultipleIndicatorsData(List<String> configFiles, SQLiteDatabase sqLiteDatabase) {
        if (hasInitializedIndicators(sqLiteDatabase)) {
            return;
        }
        for (String configFile: configFiles){
            readConfigFile(configFile, sqLiteDatabase);
        }
    }

    private void initYamlIndicatorConfig() {
        Constructor constructor = new Constructor(IndicatorsYamlConfig.class);
        TypeDescription typeDescription = new TypeDescription(IndicatorsYamlConfig.class);
        typeDescription.addPropertyParameters(IndicatorYamlConfigItem.INDICATOR_PROPERTY, IndicatorYamlConfigItem.class);
        constructor.addTypeDescription(typeDescription);
        yaml = new Yaml(constructor);
    }

    private Iterable<Object> loadIndicatorsFromFile(String configFilePath) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(context.applicationContext().getAssets().open(configFilePath));
        return yaml.loadAll(inputStreamReader);
    }

    private void saveIndicators(List<ReportIndicator> indicators) {
        for (ReportIndicator indicator : indicators) {
            this.indicatorRepository().add(indicator);
        }
    }

    private void saveIndicators(List<ReportIndicator> indicators, SQLiteDatabase sqLiteDatabase) {
        for (ReportIndicator indicator : indicators) {
            this.indicatorRepository().add(indicator, sqLiteDatabase);
        }
    }

    private void saveIndicatorQueries(List<IndicatorQuery> indicatorQueries) {
        for (IndicatorQuery indicatorQuery : indicatorQueries) {
            this.indicatorQueryRepository().add(indicatorQuery);
        }
    }

    private void saveIndicatorQueries(List<IndicatorQuery> indicatorQueries, SQLiteDatabase sqLiteDatabase) {
        for (IndicatorQuery indicatorQuery : indicatorQueries) {
            this.indicatorQueryRepository().add(indicatorQuery, sqLiteDatabase);
        }
    }

    private boolean isAppUpdated() {
        String savedAppVersion = ReportingLibrary.getInstance().getContext().allSharedPreferences().getPreference(Constants.PrefKey.APP_VERSION_CODE);
        if (savedAppVersion.isEmpty()) {
            return true;
        } else {
            return (BuildConfig.VERSION_CODE > Integer.parseInt(savedAppVersion));
        }
    }

    private boolean isIndicatorsInitialized() {
        return Boolean.parseBoolean(context.allSharedPreferences().getPreference(Constants.PrefKey.INDICATOR_DATA_INITIALISED));
    }

    public AppProperties getAppProperties() {
        return appProperties;
    }

    public void setDefaultMultiResultProcessor(@NonNull MultiResultProcessor defaultMultiResultProcessor) {
        this.defaultMultiResultProcessor = defaultMultiResultProcessor;
    }

    public void addMultiResultProcessor(@NonNull MultiResultProcessor multiResultProcessor) {
        this.multiResultProcessors.add(multiResultProcessor);
    }

    @NonNull
    public ArrayList<MultiResultProcessor> getMultiResultProcessors() {
        return multiResultProcessors;
    }

    @NonNull
    public MultiResultProcessor getDefaultMultiResultProcessor() {
        return defaultMultiResultProcessor;
    }
}
