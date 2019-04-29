package org.smartregister.sample;

import android.util.Log;

import com.evernote.android.job.JobManager;

import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.domain.IndicatorQuery;
import org.smartregister.reporting.domain.ReportIndicator;
import org.smartregister.reporting.job.IndicatorGeneratorJobCreator;
import org.smartregister.repository.Repository;
import org.smartregister.sample.domain.IndicatorYamlConfigItem;
import org.smartregister.sample.domain.IndicatorsYamlConfig;
import org.smartregister.sample.repository.SampleRepository;
import org.smartregister.view.activity.DrishtiApplication;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.smartregister.util.Log.logError;

public class SampleApplication extends DrishtiApplication {

    private Yaml yaml;
    private static String indicatorsConfigFile = "config/indicator-definitions.yml";

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        context = Context.getInstance();
        context.updateApplicationContext(getApplicationContext());
        CoreLibrary.init(context);
        repository = getRepository();
        ReportingLibrary.init(Context.getInstance(), repository, null, BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);
        initYamlIndicatorConfig();
        Iterable<Object> indicatorsFromFile = null;
        try {
            indicatorsFromFile = loadIndicatorsFromFile();
        } catch (IOException ioe) {
            Log.e("SampleApplication", ioe.getMessage());
        }
        if (indicatorsFromFile != null) {
            IndicatorsYamlConfig indicatorsConfig;
            List<ReportIndicator> indicators = new ArrayList<>();
            List<IndicatorQuery> indicatorQueries = new ArrayList<>();
            ReportIndicator indicator;
            IndicatorQuery indicatorQuery;

            for (Object indicatorObject : indicatorsFromFile) {
                indicatorsConfig = (IndicatorsYamlConfig) indicatorObject;
                for (IndicatorYamlConfigItem indicatorYamlConfigItem : indicatorsConfig.getIndicators()) {
                    indicator = new ReportIndicator(null, indicatorYamlConfigItem.getKey(), indicatorYamlConfigItem.getDescription(), null);
                    indicatorQuery = new IndicatorQuery(null, indicatorYamlConfigItem.getKey(), indicatorYamlConfigItem.getIndicatorQuery(), 0);
                    indicators.add(indicator);
                    indicatorQueries.add(indicatorQuery);
                }
            }
            SampleRepository.addSampleData(indicators, indicatorQueries);
        }

        JobManager.create(this).addJobCreator(new IndicatorGeneratorJobCreator());
    }

    public Repository getRepository() {
        try {
            if (repository == null) {
                repository = new SampleRepository(getInstance().getApplicationContext(), context);
            }
        } catch (UnsatisfiedLinkError e) {
            logError("Error on getRepository: " + e);

        }
        return repository;
    }

    public static synchronized SampleApplication getInstance() {
        return (SampleApplication) mInstance;
    }

    @Override
    public void logoutCurrentUser() {

    }

    public void initYamlIndicatorConfig() {
        Constructor constructor = new Constructor(IndicatorsYamlConfig.class);
        TypeDescription typeDescription = new TypeDescription(IndicatorsYamlConfig.class);
        typeDescription.addPropertyParameters(IndicatorYamlConfigItem.INDICATOR_PROPERTY, IndicatorYamlConfigItem.class);
        constructor.addTypeDescription(typeDescription);
        yaml = new Yaml(constructor);
    }

    public Iterable<Object> loadIndicatorsFromFile() throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(getApplicationContext().getAssets().open(indicatorsConfigFile));
        return yaml.loadAll(inputStreamReader);
    }
}
