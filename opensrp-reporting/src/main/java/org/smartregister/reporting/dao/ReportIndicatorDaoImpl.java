package org.smartregister.reporting.dao;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.model.IndicatorQuery;
import org.smartregister.reporting.model.IndicatorTally;
import org.smartregister.reporting.model.ReportIndicator;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ReportIndicatorDao allows for processing of Indicators. This class acts as the Interactor for
 * Presenters and also allows for use in other Services
 *
 * @author allan
 */

public class ReportIndicatorDaoImpl implements ReportIndicatorDao {

    private IndicatorQueryRepository indicatorQueryRepository;
    private DailyIndicatorCountRepository dailyIndicatorCountRepository;
    private IndicatorRepository indicatorRepository;
    public static String PREVIOUS_REPORT_DATES_QUERY = "select distinct eventDate, " + EventClientRepository.event_column.updatedAt + " from " + EventClientRepository.Table.event.name();
    public static final String REPORT_LAST_PROCESSED_DATE = "REPORT_LAST_PROCESSED_DATE";
    private static String TAG = ReportIndicatorDaoImpl.class.getCanonicalName();

    public ReportIndicatorDaoImpl(IndicatorQueryRepository indicatorQueryRepository, DailyIndicatorCountRepository dailyIndicatorCountRepository, IndicatorRepository indicatorRepository) {
        this.indicatorQueryRepository = indicatorQueryRepository;
        this.dailyIndicatorCountRepository = dailyIndicatorCountRepository;
        this.indicatorRepository = indicatorRepository;
    }

    public ReportIndicatorDaoImpl() {
    }

    @Override
    public void addReportIndicator(ReportIndicator indicator) {
        indicatorRepository.add(indicator);
    }

    @Override
    public void addIndicatorQuery(IndicatorQuery indicatorQuery) {
        indicatorQueryRepository.add(indicatorQuery);
    }

    @Override
    public List<Map<String, IndicatorTally>> getIndicatorsDailyTallies() {
        return dailyIndicatorCountRepository.getAllDailyTallies();
    }

    @Override
    public void generateDailyIndicatorTallies(String lastProcessedDate) {
        int count;
        IndicatorTally tally;
        SQLiteDatabase database = ReportingLibrary.getInstance().getRepository().getWritableDatabase();

        ArrayList<HashMap<String, String>> reportEventDates = getReportEventDates(lastProcessedDate, database);
        Map<String, String> indicatorQueries = indicatorQueryRepository.getAllIndicatorQueries();
        if (!reportEventDates.isEmpty() && !indicatorQueries.isEmpty()) {
            String date;
            String lastUpdatedDate = "";
            for (Map<String, String> dates : reportEventDates) {
                date = dates.get(EventClientRepository.event_column.eventDate.name());
                lastUpdatedDate = dates.get(EventClientRepository.event_column.updatedAt.name());
                for (Map.Entry<String, String> entry : indicatorQueries.entrySet()) {
                    count = executeQueryAndReturnCount(entry.getValue(), date, database);
                    tally = new IndicatorTally();
                    tally.setIndicatorCode(entry.getKey());
                    tally.setCount(count);
                    dailyIndicatorCountRepository.add(tally);
                }
            }
            ReportingLibrary.getInstance().getContext().allSharedPreferences().savePreference(REPORT_LAST_PROCESSED_DATE, lastUpdatedDate);
            Log.logDebug("generateDailyIndicatorTallies: Generate daily tallies complete");
        }
    }

    private ArrayList<HashMap<String, String>> getReportEventDates(String lastProcessedDate, SQLiteDatabase database) {
        if (lastProcessedDate == null || lastProcessedDate.isEmpty()) {
            return dailyIndicatorCountRepository.rawQuery(database, PREVIOUS_REPORT_DATES_QUERY);
        } else {
            return dailyIndicatorCountRepository.rawQuery(database, PREVIOUS_REPORT_DATES_QUERY.concat(" where " + EventClientRepository.event_column.updatedAt + " >'" + lastProcessedDate + "'" + " order by eventDate asc"));
        }
    }

    private int executeQueryAndReturnCount(String queryString, String date, SQLiteDatabase database) {
        // Use date in querying if specified
        String formattedQueryString = "";
        String formattedDate = "";
        if (date != null) {
            // Format date first
            formattedDate = formatDate(date);
            formattedQueryString = String.format(queryString, formattedDate);
        }
        Cursor cursor = null;
        int count = 0;
        try {
            cursor = database.rawQuery(formattedQueryString, null);
            if (null != cursor) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    count = cursor.getInt(0);
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.logError(e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    private String formatDate(String date) {
        String dbDateFormatString = "E MMM dd hh:mm:ss z yyyy";
        String queryFormatString = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dbDate = new SimpleDateFormat(dbDateFormatString);
        SimpleDateFormat queryFormat = new SimpleDateFormat(queryFormatString);
        String formatedDate = "";
        try {
            formatedDate = queryFormat.format(dbDate.parse(date));
        } catch (ParseException pe) {
            // Oh no!
            Log.logError(TAG, "Error parsing the db date");
        }
        return formatedDate;
    }

    public void setIndicatorQueryRepository(IndicatorQueryRepository indicatorQueryRepository) {
        this.indicatorQueryRepository = indicatorQueryRepository;
    }

    public void setDailyIndicatorCountRepository(DailyIndicatorCountRepository dailyIndicatorCountRepository) {
        this.dailyIndicatorCountRepository = dailyIndicatorCountRepository;
    }

    public void setIndicatorRepository(IndicatorRepository indicatorRepository) {
        this.indicatorRepository = indicatorRepository;
    }
}
