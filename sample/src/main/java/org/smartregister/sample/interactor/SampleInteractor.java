package org.smartregister.sample.interactor;

import org.smartregister.reporting.BuildConfig;
import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.job.RecurringIndicatorGeneratingJob;

import java.util.concurrent.TimeUnit;

public class SampleInteractor implements ReportContract.Interactor {

    public SampleInteractor() {
    }

    @Override
    public void scheduleDailyTallyJob() {
        RecurringIndicatorGeneratingJob.scheduleJob(RecurringIndicatorGeneratingJob.TAG,
                TimeUnit.MINUTES.toMillis(BuildConfig.REPORT_INDICATOR_GENERATION_MINUTES), TimeUnit.MINUTES.toMillis(1));
    }
}
