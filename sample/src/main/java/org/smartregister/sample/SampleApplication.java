package org.smartregister.sample;

import com.evernote.android.job.JobManager;

import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.job.IndicatorGeneratorJobCreator;
import org.smartregister.repository.Repository;
import org.smartregister.sample.repository.SampleRepository;
import org.smartregister.view.activity.DrishtiApplication;

import static org.smartregister.util.Log.logError;

public class SampleApplication extends DrishtiApplication {

    private static final String HAS_LOADED_SAMPLE_DATA = "has_loaded_sample_data";

    public static synchronized SampleApplication getInstance() {
        return (SampleApplication) mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = Context.getInstance();
        context.updateApplicationContext(getApplicationContext());
        CoreLibrary.init(context);
        ReportingLibrary.init(Context.getInstance(), getRepository(), null,
                BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);
        ReportingLibrary reportingLibraryInstance = ReportingLibrary.getInstance();
        String sampleIndicatorConfigFile = "config/indicator-definitions.yml";
        reportingLibraryInstance.initIndicatorData(sampleIndicatorConfigFile, null);

        if (!hasLoadedSampleData()) {
            SampleRepository.addSampleData();
            context.allSharedPreferences().savePreference(HAS_LOADED_SAMPLE_DATA, "true");
        }

        JobManager.create(this).addJobCreator(new IndicatorGeneratorJobCreator());
    }

    @Override
    public void logoutCurrentUser() {

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

    private boolean hasLoadedSampleData() {
        return Boolean.parseBoolean(context.allSharedPreferences().getPreference(HAS_LOADED_SAMPLE_DATA));
    }
}
