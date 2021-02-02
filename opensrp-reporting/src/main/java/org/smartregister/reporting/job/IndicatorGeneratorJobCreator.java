package org.smartregister.reporting.job;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class IndicatorGeneratorJobCreator implements JobCreator {
    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case RecurringIndicatorGeneratingJob.TAG:
                return new RecurringIndicatorGeneratingJob();
            default:
                Log.d(IndicatorGeneratorJobCreator.class.getCanonicalName(), "Looks like you tried to create a job " + tag + " that is not declared in the Job Creator");
                return null;

        }
    }
}
