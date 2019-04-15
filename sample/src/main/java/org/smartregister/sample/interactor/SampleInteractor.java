package org.smartregister.sample.interactor;

import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.job.RecurringIndicatorGeneratingJob;

public class SampleInteractor implements ReportContract.Interactor {

    public SampleInteractor() {
    }

    @Override
    public void scheduleDailyTallyJob() {
        // TODO :: Change this to periodic after testing
        RecurringIndicatorGeneratingJob.scheduleJobImmediately(RecurringIndicatorGeneratingJob.TAG);
    }
}
