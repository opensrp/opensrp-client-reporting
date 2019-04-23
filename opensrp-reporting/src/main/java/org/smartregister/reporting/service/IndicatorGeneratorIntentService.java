package org.smartregister.reporting.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.dao.ReportIndicatorDaoImpl;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * The specific task will be to build the indicator data
 *
 * @author allan
 */
public class IndicatorGeneratorIntentService extends IntentService {

    private ReportIndicatorDaoImpl reportIndicatorDao;
    public static String TAG = "IndicatorGeneratorIntentService";

    public IndicatorGeneratorIntentService() {
        super("IndicatorGeneratorIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "IndicatorGeneratorIntentService running");
        String lastProcessedDate = ReportingLibrary.getInstance().getContext().allSharedPreferences().getPreference(ReportIndicatorDaoImpl.REPORT_LAST_PROCESSED_DATE);
        Log.d(TAG, "LastProcessedDate " + lastProcessedDate);
        reportIndicatorDao.generateDailyIndicatorTallies(lastProcessedDate);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DailyIndicatorCountRepository dailyIndicatorCountRepository = ReportingLibrary.getInstance().dailyIndicatorCountRepository();
        IndicatorQueryRepository indicatorQueryRepository = ReportingLibrary.getInstance().indicatorQueryRepository();
        IndicatorRepository indicatorRepository = ReportingLibrary.getInstance().indicatorRepository();
        reportIndicatorDao = new ReportIndicatorDaoImpl(indicatorQueryRepository, dailyIndicatorCountRepository, indicatorRepository);
        return super.onStartCommand(intent, flags, startId);
    }
}
