package org.smartregister.reporting.job;

import android.content.Intent;

import androidx.annotation.NonNull;

import org.smartregister.AllConstants;
import org.smartregister.job.BaseJob;
import org.smartregister.reporting.service.IndicatorGeneratorIntentService;

import timber.log.Timber;

public class RecurringIndicatorGeneratingJob extends BaseJob {

    public static final String TAG = "IndicatorGeneratingJob";

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        Intent intent = new Intent(getApplicationContext(), IndicatorGeneratorIntentService.class);
        getApplicationContext().startService(intent);
        Timber.i("IndicatorGeneratorIntentService start service called");
        return params != null && params.getExtras().getBoolean(AllConstants.INTENT_KEY.TO_RESCHEDULE, false) ? Result.RESCHEDULE : Result.SUCCESS;
    }
}
