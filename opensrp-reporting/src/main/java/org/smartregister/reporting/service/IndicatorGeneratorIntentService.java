package org.smartregister.reporting.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 * @author allan
 */
public class IndicatorGeneratorIntentService extends IntentService {

    public IndicatorGeneratorIntentService() {
        super("IndicatorGeneratorIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

        }
    }
}
