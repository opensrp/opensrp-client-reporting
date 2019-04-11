package org.smartregister.reporting.dao;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.reporting.model.IndicatorQuery;
import org.smartregister.reporting.model.IndicatorTally;
import org.smartregister.reporting.model.ReportIndicator;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.util.Log;

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

    private SQLiteDatabase database;
    private IndicatorQueryRepository indicatorQueryRepository;
    private DailyIndicatorCountRepository dailyIndicatorCountRepository;
    private IndicatorRepository indicatorRepository;
    public static String PREVIOUS_REPORT_DATES_QUERY = "select distinct strftime('%Y-%m-%d'," + EventClientRepository.event_column.eventDate + ") as eventDate, " + EventClientRepository.event_column.updatedAt + " from " + EventClientRepository.Table.event.name();
    public static final String REPORT_LAST_PROCESSED_DATE = "REPORT_LAST_PROCESSED_DATE";

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
    public void generateDailyIndicatorTallies(SQLiteDatabase db, String lastProcessedDate) {
        database = db;
        int count = 0;
        IndicatorTally tally;

        ArrayList<Map<String, String>> reportEventDates = getReportEventDates(lastProcessedDate);
        for (Map<String, String> dates : reportEventDates) {
            String date = dates.get(EventClientRepository.event_column.eventDate.name());
            // String updatedAt = dates.get(EventClientRepository.event_column.updatedAt.name());
            Map<String, String> indicatorQueries = indicatorQueryRepository.getAllIndicatorQueries();
            for (Map.Entry<String, String> entry : indicatorQueries.entrySet()) {
                count = executeQueryAndReturnCount(entry.getValue(), date);
                tally = new IndicatorTally();
                tally.setIndicatorCode(entry.getKey());
                tally.setCount(count);
                dailyIndicatorCountRepository.add(tally);
            }
        }
    }

    private ArrayList<Map<String, String>> getReportEventDates(String lastProcessedDate) {
        ArrayList<Map<String, String>> eventDatesList = new ArrayList<>();
        Cursor cursor;
        if (lastProcessedDate == null) {
            cursor = database.rawQuery(PREVIOUS_REPORT_DATES_QUERY, null);
        } else {
            cursor = database.rawQuery(PREVIOUS_REPORT_DATES_QUERY.concat(" where " + EventClientRepository.event_column.updatedAt + " >'" + lastProcessedDate + "'" + " order by eventDate asc"), null);
        }
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            int i = 0;
            while (!cursor.isAfterLast()) {
                Map<String, String> dateMap = new HashMap<>();
                dateMap.put(cursor.getColumnName(i), cursor.getString(i));
                eventDatesList.add(dateMap);
                i++;
            }
        }
        return eventDatesList;
    }

    private int executeQueryAndReturnCount(String query, String date) {
        if (date != null) {
            // Use date in querying if specified
            query = String.format(query, date);
        }
        Cursor cursor = null;
        int count = 0;
        try {
            cursor = database.rawQuery(query, null);
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
