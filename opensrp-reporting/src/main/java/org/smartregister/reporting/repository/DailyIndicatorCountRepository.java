package org.smartregister.reporting.repository;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.dao.ReportIndicatorDaoImpl;
import org.smartregister.reporting.domain.CompositeIndicatorTally;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.exception.MultiResultProcessorException;
import org.smartregister.reporting.processor.MultiResultProcessor;
import org.smartregister.reporting.util.Constants;
import org.smartregister.reporting.util.ReportingUtils;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

/**
 * This DailyIndicatorCountRepository class handles saving daily computed indicator values
 * These values will consist the datetime of saving, the key and value
 *
 * @author allan
 */

public class DailyIndicatorCountRepository extends BaseRepository {

    public static String CREATE_DAILY_TALLY_TABLE = "CREATE TABLE " + Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE
            + "(" + Constants.DailyIndicatorCountRepository.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + Constants.DailyIndicatorCountRepository.INDICATOR_CODE + " TEXT NOT NULL, " + Constants.DailyIndicatorCountRepository.INDICATOR_VALUE
            + " REAL, " + Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET + " TEXT, "
            + Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET_FLAG + " BOOLEAN NOT NULL default 0, "
            + Constants.DailyIndicatorCountRepository.DAY + " DATETIME NOT NULL DEFAULT (DATETIME('now')))";

    private static String CREATE_UNIQUE_CONSTRAINT = "CREATE UNIQUE INDEX indicator_daily_tally_ix ON " +
            Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE + " ( " +
            Constants.DailyIndicatorCountRepository.INDICATOR_CODE + " , "
            + Constants.DailyIndicatorCountRepository.DAY + " ) ";

    public DailyIndicatorCountRepository(Repository repository) {
        super(repository);
    }

    public static void performMigrations(@NonNull SQLiteDatabase database) {
        // Perform migrations
        if (ReportingUtils.isTableExists(database, Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE)
                && !ReportingUtils.isColumnExists(database, Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE
                , Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET)) {
            addValueSetColumns(database);
        }

        if (ReportingUtils.isTableExists(database, Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE)) {
            // If there are multiple indicator tallies for a single day the we need to aggregate
            ArrayList<Object[]> results = ReportingUtils.performQuery(database,
                    "SELECT count(*) AS total_count FROM indicator_daily_tally GROUP BY indicator_code" +
                            ", strftime('%Y-%m-%d', day), indicator_is_value_set ORDER BY total_count DESC LIMIT 1");

            if (results.size() > 0 & ((int) results.get(1)[0]) > 1) {
                aggregateDailyTallies(database);
            }
        }
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_DAILY_TALLY_TABLE);
        database.execSQL(CREATE_UNIQUE_CONSTRAINT);
    }

    public void add(@Nullable CompositeIndicatorTally indicatorTally) {
        if (indicatorTally == null) {
            return;
        }

        SQLiteDatabase database = getWritableDatabase();
        database.delete(Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE
                , Constants.DailyIndicatorCountRepository.INDICATOR_CODE + " = ? AND "
                        + Constants.DailyIndicatorCountRepository.DAY + " = ? "
                , new String[]{
                        indicatorTally.getIndicatorCode(),
                        new SimpleDateFormat(
                                ReportIndicatorDaoImpl.DAILY_TALLY_DATE_FORMAT,
                                Locale.getDefault()).format(indicatorTally.getCreatedAt()
                        )
        });
        database.insert(Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE, null, createContentValues(indicatorTally));
    }

    public List<Map<String, IndicatorTally>> getAllDailyTallies() {
        List<Map<String, IndicatorTally>> indicatorTallies = new ArrayList<>();
        Map<String, IndicatorTally> tallyMap;

        SQLiteDatabase database = getReadableDatabase();
        Object[] queryArgs = {
                Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE, Constants.DailyIndicatorCountRepository.ID
                , Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE, Constants.DailyIndicatorCountRepository.INDICATOR_CODE
                , Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE, Constants.DailyIndicatorCountRepository.INDICATOR_VALUE
                , Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE, Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET
                , Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE, Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET_FLAG
                , Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE, Constants.DailyIndicatorCountRepository.DAY
                , Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE, Constants.IndicatorQueryRepository.INDICATOR_QUERY_EXPECTED_INDICATORS
                , Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE
                , Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE
                , Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE, Constants.DailyIndicatorCountRepository.INDICATOR_CODE
                , Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE, Constants.IndicatorQueryRepository.INDICATOR_CODE
        };

        Cursor cursor = null;
        try {
            cursor = database.rawQuery(String.format("SELECT %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s FROM %s INNER JOIN %s ON %s.%s = %s.%s", queryArgs)
                    , null);
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                MultiResultProcessor defaultMultiResultProcessor = ReportingLibrary.getInstance().getDefaultMultiResultProcessor();
                ArrayList<MultiResultProcessor> multiResultProcessors = ReportingLibrary.getInstance().getMultiResultProcessors();

                while (!cursor.isAfterLast()) {
                    tallyMap = new HashMap<>();
                    CompositeIndicatorTally compositeIndicatorTally = processCursorRow(cursor);

                    if (compositeIndicatorTally.isValueSet()) {
                        extractIndicatorTalliesFromMultiResult(tallyMap, defaultMultiResultProcessor, multiResultProcessors, compositeIndicatorTally);
                    } else {
                        tallyMap.put(compositeIndicatorTally.getIndicatorCode(), compositeIndicatorTally);
                    }

                    if (tallyMap.size() > 0) {
                        indicatorTallies.add(tallyMap);
                    }

                    cursor.moveToNext();
                }
            }
        } catch (Exception ex) {
            Timber.e(ex.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return indicatorTallies;
    }

    /**
     * Inserts the uncondensed {@link IndicatorTally}s from the {@link CompositeIndicatorTally} into the tallyMap passed as
     * the first parameter
     *
     * @param tallyMap
     * @param defaultMultiResultProcessor
     * @param multiResultProcessors
     * @param compositeIndicatorTally
     */
    private void extractIndicatorTalliesFromMultiResult(@NonNull Map<String, IndicatorTally> tallyMap, @NonNull MultiResultProcessor defaultMultiResultProcessor
            , @NonNull ArrayList<MultiResultProcessor> multiResultProcessors, @NonNull CompositeIndicatorTally compositeIndicatorTally) {
        ArrayList<Object[]> compositeTallies = new Gson().fromJson(compositeIndicatorTally.getValueSet(), new TypeToken<List<Object[]>>(){}.getType());
        List<IndicatorTally> uncondensedTallies = null;
        if (compositeTallies.size() > 1) {
            Object[] objectFieldNames = compositeTallies.get(0);
            String[] fieldNames = new String[objectFieldNames.length];

            for (int i = 0; i < objectFieldNames.length; i++) {
                fieldNames[i] = (String) objectFieldNames[i];
            }

            if (defaultMultiResultProcessor.canProcess(fieldNames.length,fieldNames)) {
                uncondensedTallies = processMultipleTallies(defaultMultiResultProcessor, compositeIndicatorTally);
            } else {
                for (MultiResultProcessor multiResultProcessor: multiResultProcessors) {
                    if (multiResultProcessor.canProcess(fieldNames.length, fieldNames)) {
                        uncondensedTallies = processMultipleTallies(multiResultProcessor, compositeIndicatorTally);
                    }
                }
            }
        }

        // Filter and only return what we expect
        if (compositeIndicatorTally.getExpectedIndicators() != null && compositeIndicatorTally.getExpectedIndicators().size() > 0) {
            uncondensedTallies = extractOnlyRequiredIndicatorTalliesAndProvideDefault(compositeIndicatorTally, uncondensedTallies);
        }

        if (uncondensedTallies != null) {
            for (IndicatorTally indicatorTally: uncondensedTallies) {
                tallyMap.put(indicatorTally.getIndicatorCode(), indicatorTally);
            }
        }
    }

    @VisibleForTesting
    @NonNull
    public List<IndicatorTally> extractOnlyRequiredIndicatorTalliesAndProvideDefault(
            @NonNull CompositeIndicatorTally compositeIndicatorTally,
            @Nullable List<IndicatorTally> uncondensedTallies) {

        List<String> expectedIndicators = compositeIndicatorTally.getExpectedIndicators();

        if (uncondensedTallies == null) {
            List<IndicatorTally> tallies = new ArrayList<>();

            for (String expectedIndicatorCode: expectedIndicators) {
                IndicatorTally indicatorTally = new IndicatorTally();
                indicatorTally.setCount(0F);
                indicatorTally.setIndicatorCode(expectedIndicatorCode);
                indicatorTally.setId(compositeIndicatorTally.getId());
                indicatorTally.setCreatedAt(compositeIndicatorTally.getCreatedAt());

                tallies.add(indicatorTally);
            }

            return tallies;
        } else {

            List<IndicatorTally> tallies = new ArrayList<>();
            HashMap<String, IndicatorTally> indicatorTallyHashMap = new HashMap<>();

            for (IndicatorTally indicatorTally: uncondensedTallies) {
                indicatorTallyHashMap.put(indicatorTally.getIndicatorCode(), indicatorTally);
            }

            for (String expectedIndicatorCode: expectedIndicators) {
                if (indicatorTallyHashMap.containsKey(expectedIndicatorCode)) {
                    tallies.add(indicatorTallyHashMap.get(expectedIndicatorCode));
                } else {
                    IndicatorTally indicatorTally = new IndicatorTally();
                    indicatorTally.setCount(0F);
                    indicatorTally.setIndicatorCode(expectedIndicatorCode);
                    indicatorTally.setId(compositeIndicatorTally.getId());
                    indicatorTally.setCreatedAt(compositeIndicatorTally.getCreatedAt());

                    tallies.add(indicatorTally);
                }
            }

            return tallies;
        }
    }

    @Nullable
    public static List<IndicatorTally> processMultipleTallies(@NonNull MultiResultProcessor multiResultProcessor
            , @NonNull CompositeIndicatorTally compositeIndicatorTally) {
        try {
            return multiResultProcessor.processMultiResultTally(compositeIndicatorTally);
        } catch (MultiResultProcessorException ex) {
            Timber.e(ex);
            return null;
        }
    }

    public Map<String, IndicatorTally> getDailyTallies(@NonNull Date date) {
        Map<String, IndicatorTally> tallyMap = new HashMap<>();

        SQLiteDatabase database = getReadableDatabase();

        Object[] queryArgs = {
                Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE, Constants.DailyIndicatorCountRepository.ID
                , Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE, Constants.DailyIndicatorCountRepository.INDICATOR_CODE
                , Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE, Constants.DailyIndicatorCountRepository.INDICATOR_VALUE
                , Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE, Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET
                , Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE, Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET_FLAG
                , Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE, Constants.DailyIndicatorCountRepository.DAY
                , Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE, Constants.IndicatorQueryRepository.INDICATOR_QUERY_EXPECTED_INDICATORS
                , Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE
                , Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE
                , Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE, Constants.DailyIndicatorCountRepository.INDICATOR_CODE
                , Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE, Constants.IndicatorQueryRepository.INDICATOR_CODE
                , Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE, Constants.DailyIndicatorCountRepository.DAY
                , Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE, Constants.DailyIndicatorCountRepository.DAY
        };

        Cursor cursor = null;

        Calendar dayStart = Calendar.getInstance();
        dayStart.setTime(date);
        dayStart.set(Calendar.HOUR_OF_DAY, 0);
        dayStart.set(Calendar.MINUTE, 0);
        dayStart.set(Calendar.SECOND, 0);
        dayStart.set(Calendar.MILLISECOND, 0);

        long dayStartMillis = dayStart.getTimeInMillis();

        dayStart.add(Calendar.DAY_OF_MONTH, 1);
        long dayEndMillis = dayStart.getTimeInMillis();

        try {
            cursor = database.rawQuery(String.format(
                    "SELECT %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s FROM %s INNER JOIN %s ON %s.%s = %s.%s WHERE %s.%s >= ? %s.%s AND < ?"
                    , queryArgs),
                    new String[]{String.valueOf(dayStartMillis), String.valueOf(dayEndMillis)});
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    CompositeIndicatorTally compositeIndicatorTally = processCursorRow(cursor);
                    tallyMap.put(compositeIndicatorTally.getIndicatorCode(), compositeIndicatorTally);
                    cursor.moveToNext();
                }
            }
        } catch (Exception ex) {
            Timber.e(ex.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tallyMap;
    }

    private CompositeIndicatorTally processCursorRow(@NonNull Cursor cursor) {
        CompositeIndicatorTally compositeIndicatorTally = new CompositeIndicatorTally();
        compositeIndicatorTally.setId(cursor.getLong(cursor.getColumnIndex(Constants.DailyIndicatorCountRepository.ID)));
        compositeIndicatorTally.setIndicatorCode(cursor.getString(cursor.getColumnIndex(Constants.DailyIndicatorCountRepository.INDICATOR_CODE)));
        compositeIndicatorTally.setValueSetFlag(cursor.getInt(cursor.getColumnIndex(Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET_FLAG)) == 1);

        if (compositeIndicatorTally.isValueSet()) {
            compositeIndicatorTally.setValueSet(cursor.getString(cursor.getColumnIndex(Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET)));
        } else {
            compositeIndicatorTally.setCount(cursor.getFloat(cursor.getColumnIndex(Constants.DailyIndicatorCountRepository.INDICATOR_VALUE)));
        }

        if (cursor.getColumnIndex(Constants.IndicatorQueryRepository.INDICATOR_QUERY_EXPECTED_INDICATORS) != -1) {
            compositeIndicatorTally.setExpectedIndicators(
                    (List<String>) new Gson().fromJson(cursor.getString(cursor.getColumnIndex(
                            Constants.IndicatorQueryRepository.INDICATOR_QUERY_EXPECTED_INDICATORS))
                            , new TypeToken<List<String>>() {}.getType())
            );
        }

        compositeIndicatorTally.setCreatedAt(new Date(cursor.getLong(cursor.getColumnIndex(Constants.DailyIndicatorCountRepository.DAY))));
        return compositeIndicatorTally;
    }

    public ContentValues createContentValues(@NonNull CompositeIndicatorTally compositeIndicatorTally) {
        ContentValues values = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat(ReportIndicatorDaoImpl.DAILY_TALLY_DATE_FORMAT, Locale.getDefault());
        values.put(Constants.DailyIndicatorCountRepository.INDICATOR_CODE, compositeIndicatorTally.getIndicatorCode());

        if (compositeIndicatorTally.isValueSet()) {
            values.put(Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET, compositeIndicatorTally.getValueSet());
        } else {
            values.put(Constants.DailyIndicatorCountRepository.INDICATOR_VALUE, compositeIndicatorTally.getFloatCount());
        }

        values.put(Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET_FLAG, compositeIndicatorTally.isValueSet());
        values.put(Constants.DailyIndicatorCountRepository.DAY, dateFormat.format(compositeIndicatorTally.getCreatedAt()));
        return values;
    }

    public static void addValueSetColumns(@NonNull SQLiteDatabase database) {
        // Change indicator_value to be nullable
        // Add the two fields
        // Set the default for the is_value_set to false
        database.execSQL("PRAGMA foreign_keys=off");
        database.beginTransaction();

        database.execSQL("ALTER TABLE " + Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE
                + " RENAME TO old_" + Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE);
        database.execSQL(CREATE_DAILY_TALLY_TABLE);

        String copyDataSQL = String.format("INSERT INTO %s(%s, %s, %s) SELECT %s, %s, %s FROM old_%s",
                Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE,
                Constants.DailyIndicatorCountRepository.INDICATOR_CODE,
                Constants.DailyIndicatorCountRepository.INDICATOR_VALUE,
                Constants.DailyIndicatorCountRepository.DAY,
                Constants.DailyIndicatorCountRepository.INDICATOR_CODE,
                Constants.DailyIndicatorCountRepository.INDICATOR_VALUE,
                Constants.DailyIndicatorCountRepository.DAY,
                Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE);

        // Copy over the data
        database.execSQL(copyDataSQL);
        database.execSQL("DROP TABLE old_" + Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE);

        database.setTransactionSuccessful();
        database.endTransaction();
        database.execSQL("PRAGMA foreign_keys=on;");
    }

    public static void aggregateDailyTallies(@NonNull SQLiteDatabase database) {
        // Code to migrate the code over from incremental tallies should be written here
        database.execSQL("PRAGMA foreign_keys=off");
        database.beginTransaction();

        // Read all the indicator counts & clear the table

        database.execSQL("CREATE TABLE indicator_daily_tally_old(_id INTEGER NOT NULL PRIMARY KEY, indicator_code TEXT NOT NULL, indicator_value INTEGER, indicator_value_set TEXT, indicator_is_value_set BOOLEAN NOT NULL default 0, day DATETIME NOT NULL DEFAULT (DATETIME('now')))");
        database.execSQL("INSERT INTO indicator_daily_tally_old SELECT * FROM indicator_daily_tally");

        // Delete the old values
        database.execSQL("DELETE FROM indicator_daily_tally");
        database.execSQL(String.format("INSERT INTO %s(%s, %s, %s, %s) SELECT %s, sum(%s), %s, strftime('%%Y-%%m-%%d', %s) FROM %s GROUP BY %s, strftime('%%Y-%%m-%%d', %s), %s"
                , Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE
                , Constants.DailyIndicatorCountRepository.INDICATOR_CODE
                , Constants.DailyIndicatorCountRepository.INDICATOR_VALUE
                , Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET_FLAG
                , Constants.DailyIndicatorCountRepository.DAY
                // SELECT args START HERE
                , Constants.DailyIndicatorCountRepository.INDICATOR_CODE
                , Constants.DailyIndicatorCountRepository.INDICATOR_VALUE
                , Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET_FLAG
                , Constants.DailyIndicatorCountRepository.DAY
                , "indicator_daily_tally_old"
                // GROUP BY args START HERE
                , Constants.DailyIndicatorCountRepository.INDICATOR_CODE
                , Constants.DailyIndicatorCountRepository.DAY
                , Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET_FLAG));

        database.execSQL("DROP TABLE indicator_daily_tally_old");

        database.setTransactionSuccessful();
        database.endTransaction();
        database.execSQL("PRAGMA foreign_keys=on;");
    }

}
