package org.smartregister.reporting.dao;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.domain.IndicatorQuery;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.domain.ReportIndicator;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The ReportIndicatorDao allows for processing of Indicators. This class acts as the Interactor for
 * Presenters and also allows for use in other Services
 *
 * @author allan
 */

public class ReportIndicatorDaoImpl implements ReportIndicatorDao {

    public static final String REPORT_LAST_PROCESSED_DATE = "REPORT_LAST_PROCESSED_DATE";
    public static String PREVIOUS_REPORT_DATES_QUERY = "select distinct eventDate, " + EventClientRepository.event_column.updatedAt + " from "
            + EventClientRepository.Table.event.name();
    private static String TAG = ReportIndicatorDaoImpl.class.getCanonicalName();
    private static String eventDateFormat = ReportingLibrary.getInstance().getDateFormat();
    private IndicatorQueryRepository indicatorQueryRepository;
    private DailyIndicatorCountRepository dailyIndicatorCountRepository;
    private IndicatorRepository indicatorRepository;

    public ReportIndicatorDaoImpl(IndicatorQueryRepository indicatorQueryRepository, DailyIndicatorCountRepository dailyIndicatorCountRepository,
                                  IndicatorRepository indicatorRepository) {
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

        LinkedHashMap<String, Date> reportEventDates = getReportEventDates(lastProcessedDate, database);
        Map<String, String> indicatorQueries = indicatorQueryRepository.getAllIndicatorQueries();
        if (!reportEventDates.isEmpty() && !indicatorQueries.isEmpty()) {
            String lastUpdatedDate = "";
            for (Map.Entry<String, Date> dates : reportEventDates.entrySet()) {
                lastUpdatedDate = new SimpleDateFormat(eventDateFormat, Locale.getDefault()).format(dates.getValue());
                for (Map.Entry<String, String> entry : indicatorQueries.entrySet()) {
                    count = executeQueryAndReturnCount(entry.getValue(), dates.getKey(), database);
                    if (count > 0) {
                        tally = new IndicatorTally();
                        tally.setIndicatorCode(entry.getKey());
                        tally.setCount(count);
                        try {
                            tally.setCreatedAt(new SimpleDateFormat(eventDateFormat, Locale.getDefault()).parse(dates.getKey()));
                        } catch (ParseException e) {
                            tally.setCreatedAt(new Date());
                        }
                        dailyIndicatorCountRepository.add(tally);
                    }
                }
            }
            ReportingLibrary.getInstance().getContext().allSharedPreferences().savePreference(REPORT_LAST_PROCESSED_DATE, lastUpdatedDate);
            Log.logDebug("generateDailyIndicatorTallies: Generate daily tallies complete");
        }
    }

    private LinkedHashMap<String, Date> getReportEventDates(String lastProcessedDate, SQLiteDatabase database) {

        ArrayList<HashMap<String, String>> values;
        if (lastProcessedDate == null || lastProcessedDate.isEmpty()) {
            values = dailyIndicatorCountRepository.rawQuery(database, PREVIOUS_REPORT_DATES_QUERY);
        } else {
            values = dailyIndicatorCountRepository.rawQuery(database, PREVIOUS_REPORT_DATES_QUERY.concat(" where " + EventClientRepository.event_column.updatedAt + " > '" + lastProcessedDate + "'" + " order by eventDate asc"));
        }

        LinkedHashMap<String, Date> result = new LinkedHashMap<>();

        Date eventDate;
        Date updateDate;
        for (HashMap<String, String> val : values) {
            eventDate = formatDate(val.get(EventClientRepository.event_column.eventDate.name()), eventDateFormat);
            updateDate = formatDate(val.get(EventClientRepository.event_column.updatedAt.name()), eventDateFormat);

            String keyDate = new SimpleDateFormat(eventDateFormat, Locale.getDefault()).format(eventDate);

            if (result.get(keyDate) != null && updateDate != null) {
                if (result.get(keyDate).getTime() < updateDate.getTime()) {
                    result.put(keyDate, updateDate);
                }
            } else {
                result.put(keyDate, updateDate);
            }
        }
        return result;
    }

    private int executeQueryAndReturnCount(String queryString, String date, SQLiteDatabase database) {
        // Use date in querying if specified
        String query = "";
        if (date != null) {
            Log.logDebug("QUERY :" + queryString);
            query = queryString.contains("'%s'") ? String.format(queryString, date) : queryString;
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

    private Date formatDate(String date, String format) {
        try {
            return new SimpleDateFormat(format, Locale.getDefault()).parse(date);
        } catch (ParseException pe) {
            // Oh no!
            Log.logError(TAG, "Error parsing the db date");
            return null;
        }
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
