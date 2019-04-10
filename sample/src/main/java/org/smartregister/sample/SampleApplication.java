package org.smartregister.sample;

import org.smartregister.Context;
import org.smartregister.reporting.ReportingLibrary;
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
        ReportingLibrary.init(Context.getInstance(), getRepository(), null, BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);
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

    public static synchronized SampleApplication getInstance() {
        return (SampleApplication) mInstance;
    }
}
