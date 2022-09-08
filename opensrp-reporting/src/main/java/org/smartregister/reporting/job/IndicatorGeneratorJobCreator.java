package org.smartregister.reporting.job;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

import timber.log.Timber;

public class IndicatorGeneratorJobCreator implements JobCreator {
    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case RecurringIndicatorGeneratingJob.TAG:
                return new RecurringIndicatorGeneratingJob();
            default:
                Timber.d("Looks like you tried to create a job %s that is not declared in the Job Creator", tag);
                return null;
        }
    }
}
