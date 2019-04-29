package org.smartregister.reporting;

import android.util.Log;

import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.reporting.domain.IndicatorQuery;
import org.smartregister.reporting.domain.IndicatorYamlConfigItem;
import org.smartregister.reporting.domain.IndicatorsYamlConfig;
import org.smartregister.reporting.domain.ReportIndicator;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.Repository;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ReportingLibrary {

    private Repository repository;
    private DailyIndicatorCountRepository dailyIndicatorCountRepository;
    private IndicatorQueryRepository indicatorQueryRepository;
    private IndicatorRepository indicatorRepository;
    private EventClientRepository eventClientRepository;
    private Context context;
    private static ReportingLibrary instance;
    private CommonFtsObject commonFtsObject;
    private int applicationVersion;
    private int databaseVersion;
    private Yaml yaml;

    public static void init(Context context, Repository repository, CommonFtsObject commonFtsObject, int applicationVersion, int databaseVersion) {
        if (instance == null) {
            instance = new ReportingLibrary(context, repository, commonFtsObject, applicationVersion, databaseVersion);
        }
    }

    public static ReportingLibrary getInstance() {
        if (instance == null) {
            throw new IllegalStateException(" Instance does not exist!!! Call " + ReportingLibrary.class.getName() + ".init() in the onCreate() method of your Application class");
        }
        return instance;
    }

    private ReportingLibrary(Context context, Repository repository, CommonFtsObject commonFtsObject, int applicationVersion, int databaseVersion) {
        this.repository = repository;
        this.context = context;
        this.commonFtsObject = commonFtsObject;
        this.applicationVersion = applicationVersion;
        this.databaseVersion = databaseVersion;
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

    public void initIndicatorData(String configFilePath) {
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
                    indicatorQuery = new IndicatorQuery(null, indicatorYamlConfigItem.getKey(), indicatorYamlConfigItem.getIndicatorQuery(), 0);
                    reportIndicators.add(indicator);
                    indicatorQueries.add(indicatorQuery);
                }
            }
            addIndicators(reportIndicators);
            addIndicatorQueries(indicatorQueries);
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
        InputStreamReader inputStreamReader = new InputStreamReader(Context.getInstance().applicationContext().getAssets().open(configFilePath));
        return yaml.loadAll(inputStreamReader);
    }

    private void addIndicators(List<ReportIndicator> indicators) {
        for (ReportIndicator indicator : indicators) {
            this.indicatorRepository().add(indicator);
        }
    }

    private void addIndicatorQueries(List<IndicatorQuery> indicatorQueries) {
        for (IndicatorQuery indicatorQuery : indicatorQueries) {
            this.indicatorQueryRepository().add(indicatorQuery);
        }
    }
}
