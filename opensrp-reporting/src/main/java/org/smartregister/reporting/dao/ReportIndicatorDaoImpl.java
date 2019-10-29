package org.smartregister.reporting.dao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;

import com.google.gson.Gson;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.domain.CompositeIndicatorTally;
import org.smartregister.reporting.domain.IndicatorQuery;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.domain.ReportIndicator;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;
import org.smartregister.repository.EventClientRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import timber.log.Timber;

/**
 * The ReportIndicatorDao allows for processing of Indicators. This class acts as the Interactor for
 * Presenters and also allows for use in other Services
 *
 * @author allan
 */

public class ReportIndicatorDaoImpl implements ReportIndicatorDao {
    public static final String REPORT_LAST_PROCESSED_DATE = "REPORT_LAST_PROCESSED_DATE";
    public static String DAILY_TALLY_DATE_FORMAT = "yyyy-MM-dd";

    public static String PREVIOUS_REPORT_DATES_QUERY = "select distinct eventDate, " + EventClientRepository.event_column.updatedAt + " from "
            + EventClientRepository.Table.event.name();

    private static String eventDateFormat = "yyyy-MM-dd HH:mm:ss";
    private static String eventDateFormatWithoutTimePart = "yyyy-MM-dd";
    private IndicatorQueryRepository indicatorQueryRepository;
    private DailyIndicatorCountRepository dailyIndicatorCountRepository;
    private IndicatorRepository indicatorRepository;
    private ReportingLibrary reportingLibrary;

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
        SQLiteDatabase database = getReportingLibrary().getRepository().getWritableDatabase();

        Set<String> executedQueries = new HashSet<>();
        Date timeNow = Calendar.getInstance().getTime();
        LinkedHashMap<String, Date> reportEventDates = getReportEventDates(timeNow, lastProcessedDate, database);

        Map<String, IndicatorQuery> indicatorQueries = indicatorQueryRepository.getAllIndicatorQueries();

        if (!reportEventDates.isEmpty() && !indicatorQueries.isEmpty()) {
            String lastUpdatedDate = null;

            for (Map.Entry<String, Date> dates : reportEventDates.entrySet()) {
                if (dates.getValue().getTime() != timeNow.getTime()) {
                    lastUpdatedDate = new SimpleDateFormat(eventDateFormat, Locale.getDefault()).format(dates.getValue());
                }
                saveTallies(indicatorQueries, dates, database, executedQueries);
            }

            if (!TextUtils.isEmpty(lastUpdatedDate)) {
                getReportingLibrary().getContext().allSharedPreferences().savePreference(REPORT_LAST_PROCESSED_DATE, lastUpdatedDate);
            }

            Timber.i("generateDailyIndicatorTallies: Generate daily tallies complete");
        }
    }

    private ReportingLibrary getReportingLibrary() {
        if (reportingLibrary == null) {
            reportingLibrary = ReportingLibrary.getInstance();
        }
        return reportingLibrary;
    }

    @VisibleForTesting
    protected void saveTallies(Map<String, IndicatorQuery> indicatorQueries, Map.Entry<String, Date> dates, SQLiteDatabase database, Set<String> executedQueries) {
        for (Map.Entry<String, IndicatorQuery> entry : indicatorQueries.entrySet()) {
            IndicatorQuery indicatorQuery = entry.getValue();
            CompositeIndicatorTally tally = null;

            if (indicatorQuery.isMultiResult()) {
                ArrayList<Object> result = executeQueryAndReturnMultiResult(indicatorQuery.getQuery(), dates.getKey(), database);

                // If the size contains actual result other than the column names which are at index 0
                if (result.size() > 1 || (indicatorQuery.getExpectedIndicators() != null && indicatorQuery.getExpectedIndicators().size() > 0)) {
                    tally = new CompositeIndicatorTally();
                    tally.setValueSetFlag(true);
                    tally.setValueSet(new Gson().toJson(result));
                    tally.setExpectedIndicators(indicatorQuery.getExpectedIndicators());
                }
            } else {

                String queryString = indicatorQuery.getQuery();
                String date = dates.getKey();
                if (date != null)
                    queryString = queryString.contains("%s") ? queryString.replaceAll("%s", date) : queryString;

                if (!executedQueries.contains(queryString)) {
                    Timber.i("QUERY : %s", queryString);
                    float count = executeQueryAndReturnCount(queryString, database);

                    if (count > 0) {
                        tally = new CompositeIndicatorTally();
                        tally.setCount(count);
                    }
                    executedQueries.add(queryString);
                }
            }

            if (tally != null) {
                tally.setIndicatorCode(entry.getKey());

                try {
                    tally.setCreatedAt(new SimpleDateFormat(DAILY_TALLY_DATE_FORMAT, Locale.getDefault()).parse(dates.getKey()));
                } catch (ParseException e) {
                    tally.setCreatedAt(new Date());
                }

                dailyIndicatorCountRepository.add(tally);
            }
        }
    }

    @VisibleForTesting
    @NonNull
    protected LinkedHashMap<String, Date> getReportEventDates(@NonNull Date timeNow, @Nullable String lastProcessedDate, @NonNull SQLiteDatabase database) {

        ArrayList<HashMap<String, String>> values;
        if (lastProcessedDate == null || lastProcessedDate.isEmpty()) {
            values = dailyIndicatorCountRepository.rawQuery(database, PREVIOUS_REPORT_DATES_QUERY);
        } else {
            values = dailyIndicatorCountRepository.rawQuery(database, PREVIOUS_REPORT_DATES_QUERY.concat(" where " + EventClientRepository.event_column.updatedAt + " > '" + lastProcessedDate + "'" + " order by eventDate asc"));
        }

        LinkedHashMap<String, Date> reportEventDates = new LinkedHashMap<>();

        Date eventDate;
        Date updateDate;
        for (HashMap<String, String> val : values) {
            eventDate = formatDate(val.get(EventClientRepository.event_column.eventDate.name()), eventDateFormat);
            updateDate = formatDate(val.get(EventClientRepository.event_column.updatedAt.name()), eventDateFormat);

            String keyDate = new SimpleDateFormat(DAILY_TALLY_DATE_FORMAT, Locale.getDefault()).format(eventDate);

            if (reportEventDates.get(keyDate) != null && updateDate != null) {
                if (reportEventDates.get(keyDate).getTime() < updateDate.getTime()) {
                    reportEventDates.put(keyDate, updateDate);
                }
            } else {
                reportEventDates.put(keyDate, updateDate);
            }
        }

        String dateToday = new SimpleDateFormat(DAILY_TALLY_DATE_FORMAT, Locale.getDefault()).format(timeNow);

        if (reportEventDates.get(dateToday) == null) {
            reportEventDates.put(dateToday, timeNow);
        }

        return reportEventDates;
    }

    private float executeQueryAndReturnCount(String query, SQLiteDatabase database) {
        // Use date in querying if specified
        Cursor cursor = null;
        float count = 0;
        try {
            cursor = database.rawQuery(query, null);
            if (null != cursor) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    int type = cursor.getType(0);

                    if (type == Cursor.FIELD_TYPE_FLOAT) {
                        count = (Float) cursor.getFloat(0);
                    } else if (type == Cursor.FIELD_TYPE_INTEGER) {
                        count = ((Integer) cursor.getInt(0)).floatValue();
                    }
                }
                cursor.close();
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    @NonNull
    private ArrayList<Object> executeQueryAndReturnMultiResult(@NonNull String queryString, @Nullable String date, @NonNull SQLiteDatabase database) {
        // Use date in querying if specified
        String query = "";
        if (date != null) {
            Timber.i("QUERY : %s", queryString);
            query = queryString.contains("%s") ? queryString.replaceAll("%s", date) : queryString;
        }
        Cursor cursor = null;
        ArrayList<Object> rows = new ArrayList<>();
        try {
            cursor = database.rawQuery(query, null);
            if (null != cursor) {
                int cols = cursor.getColumnCount();
                rows.add(cursor.getColumnNames());

                while (cursor.moveToNext()) {
                    Object[] col = new Object[cols];

                    for (int i = 0; i < cols; i++) {
                        int type = cursor.getType(i);
                        Object cellValue = null;

                        if (type == Cursor.FIELD_TYPE_FLOAT) {
                            cellValue = (Float) cursor.getFloat(i);
                        } else if (type == Cursor.FIELD_TYPE_INTEGER) {
                            cellValue = (Integer) cursor.getInt(i);
                        } else if (type == Cursor.FIELD_TYPE_STRING) {
                            cellValue = (String) cursor.getString(i);
                        }

                        // Types BLOB and NULL are ignored
                        // Blob is not supposed to a reporting result & NULL is already defined in the cellValue at the top
                        if (cols > 1) {
                            col[i] = cellValue;
                        } else {
                            rows.add(cellValue);
                        }
                    }

                    if (cols > 1) {
                        rows.add(col);
                    }

                }
                cursor.close();
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return rows;
    }

    @Nullable
    private Date formatDate(String date, String format) {
        try {
            return new SimpleDateFormat(format, Locale.getDefault()).parse(date);
        } catch (ParseException pe) {
            // Oh no!
            Timber.e(pe);
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
