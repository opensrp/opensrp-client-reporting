package org.smartregister.reporting.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;

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

    private DailyIndicatorCountRepository dailyIndicatorCountRepository;
    private IndicatorQueryRepository indicatorQueryRepository;
    private IndicatorRepository indicatorRepository;
    private ReportIndicatorDaoImpl reportIndicatorDao;
    private String lastProcessedDate;

    public IndicatorGeneratorIntentService() {
        super("IndicatorGeneratorIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        lastProcessedDate = ReportingLibrary.getInstance().getContext().allSharedPreferences().getPreference(ReportIndicatorDaoImpl.REPORT_LAST_PROCESSED_DATE);
        SQLiteDatabase database = ReportingLibrary.getInstance().getRepository().getWritableDatabase();
        reportIndicatorDao.generateDailyIndicatorTallies(database, lastProcessedDate);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        dailyIndicatorCountRepository = ReportingLibrary.getInstance().dailyIndicatorCountRepository();
        indicatorQueryRepository = ReportingLibrary.getInstance().indicatorQueryRepository();
        indicatorRepository = ReportingLibrary.getInstance().indicatorRepository();
        reportIndicatorDao = new ReportIndicatorDaoImpl(indicatorQueryRepository, dailyIndicatorCountRepository, indicatorRepository);
        return super.onStartCommand(intent, flags, startId);
    }
}
