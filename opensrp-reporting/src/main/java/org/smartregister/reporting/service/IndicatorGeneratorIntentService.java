package org.smartregister.reporting.service;

import android.app.IntentService;
import android.content.Intent;

import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.dao.ReportIndicatorDaoImpl;
import org.smartregister.reporting.domain.TallyStatus;
import org.smartregister.reporting.event.EventBusHelper;
import org.smartregister.reporting.event.IndicatorTallyEvent;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;

import timber.log.Timber;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * The specific task will be to build the indicator data
 *
 * @author allan
 */
public class IndicatorGeneratorIntentService extends IntentService {

    public static String TAG = "IndicatorGeneratorIntentService";
    private ReportIndicatorDaoImpl reportIndicatorDao;

    public IndicatorGeneratorIntentService() {
        super("IndicatorGeneratorIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Timber.i("IndicatorGeneratorIntentService running");
        String lastProcessedDate = ReportingLibrary.getInstance().getContext().allSharedPreferences().getPreference(ReportIndicatorDaoImpl.REPORT_LAST_PROCESSED_DATE);
        Timber.d("LastProcessedDate %s", lastProcessedDate);
        IndicatorTallyEvent tallyEvent = new IndicatorTallyEvent();
        tallyEvent.setStatus(TallyStatus.STARTED);
        EventBusHelper.postEvent(tallyEvent);
        reportIndicatorDao.generateDailyIndicatorTallies(lastProcessedDate);
        tallyEvent.setStatus(TallyStatus.COMPLETE);
        EventBusHelper.postEvent(tallyEvent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            // TODO fix missing password on core
            // generating indicators can cause errors
            DailyIndicatorCountRepository dailyIndicatorCountRepository = ReportingLibrary.getInstance().dailyIndicatorCountRepository();
            IndicatorQueryRepository indicatorQueryRepository = ReportingLibrary.getInstance().indicatorQueryRepository();
            IndicatorRepository indicatorRepository = ReportingLibrary.getInstance().indicatorRepository();
            reportIndicatorDao = new ReportIndicatorDaoImpl(indicatorQueryRepository, dailyIndicatorCountRepository, indicatorRepository);
        } catch (Exception e) {
            Timber.e(e);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
