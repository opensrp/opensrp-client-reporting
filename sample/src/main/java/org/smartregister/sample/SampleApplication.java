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

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        context = Context.getInstance();
        context.updateApplicationContext(getApplicationContext());
        CoreLibrary.init(context);
        repository = getRepository();
        ReportingLibrary.init(Context.getInstance(), repository, null, BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);
        SampleRepository.addSampleData();
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
}
