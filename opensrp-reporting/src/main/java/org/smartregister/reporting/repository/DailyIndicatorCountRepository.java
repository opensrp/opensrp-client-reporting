package org.smartregister.reporting.repository;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.domain.CompositeIndicatorTally;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.exception.MultiResultProcessorException;
import org.smartregister.reporting.processor.MultiResultProcessor;
import org.smartregister.reporting.util.Utils;
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

    public static String ID = "_id";
    public static String INDICATOR_CODE = "indicator_code";
    public static String INDICATOR_VALUE = "indicator_value";
    public static String DAY = "day";
    public static String INDICATOR_VALUE_SET = "indicator_value_set";
    public static String INDICATOR_VALUE_SET_FLAG = "indicator_is_value_set";
    public static String INDICATOR_DAILY_TALLY_TABLE = "indicator_daily_tally";

    public static String CREATE_DAILY_TALLY_TABLE = "CREATE TABLE " + INDICATOR_DAILY_TALLY_TABLE + "(" + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            INDICATOR_CODE + " TEXT NOT NULL, " + INDICATOR_VALUE + " REAL, " +
            INDICATOR_VALUE_SET + " TEXT, " + INDICATOR_VALUE_SET_FLAG + " BOOLEAN NOT NULL default 0, " + DAY + " DATETIME NOT NULL DEFAULT (DATETIME('now')))";

    private static String CREATE_UNIQUE_CONSTRAINT = "CREATE UNIQUE INDEX indicator_daily_tally_ix ON " + INDICATOR_DAILY_TALLY_TABLE + " ( " + INDICATOR_CODE + " , " + DAY + " ) ";

    public DailyIndicatorCountRepository(Repository repository) {
        super(repository);
    }

    public static void performMigrations(@NonNull SQLiteDatabase database) {
        // Perform migrations
        if (Utils.isTableExists(database, INDICATOR_DAILY_TALLY_TABLE) && !Utils.isColumnExists(database, INDICATOR_DAILY_TALLY_TABLE, INDICATOR_VALUE_SET)) {
            addValueSetColumns(database);
            aggregateDailyTallies(database);
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
        database.delete(INDICATOR_DAILY_TALLY_TABLE, INDICATOR_CODE + " = ? AND " + DAY + " = ? ",
                new String[]{indicatorTally.getIndicatorCode(), new SimpleDateFormat(ReportingLibrary.getInstance().getDateFormat(), Locale.getDefault()).format(indicatorTally.getCreatedAt())});
        database.insert(INDICATOR_DAILY_TALLY_TABLE, null, createContentValues(indicatorTally));
    }

    public List<Map<String, IndicatorTally>> getAllDailyTallies() {
        List<Map<String, IndicatorTally>> indicatorTallies = new ArrayList<>();
        Map<String, IndicatorTally> tallyMap;

        SQLiteDatabase database = getReadableDatabase();
        String[] columns = {ID, INDICATOR_CODE, INDICATOR_VALUE, INDICATOR_VALUE_SET, INDICATOR_VALUE_SET_FLAG, DAY};

        Cursor cursor = null;
        try {
            cursor = database.query(INDICATOR_DAILY_TALLY_TABLE, columns, null, null, null, null, null, null);
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

        if (uncondensedTallies != null) {
            for (IndicatorTally indicatorTally: uncondensedTallies) {
                tallyMap.put(indicatorTally.getIndicatorCode(), indicatorTally);
            }
        }
    }

    @Nullable
    private List<IndicatorTally> processMultipleTallies(@NonNull MultiResultProcessor defaultMultiResultProcessor
            , @NonNull CompositeIndicatorTally compositeIndicatorTally) {
        try {
            return defaultMultiResultProcessor.processMultiResultTally(compositeIndicatorTally);
        } catch (MultiResultProcessorException ex) {
            Timber.e(ex);
            return null;
        }
    }

    public Map<String, IndicatorTally> getDailyTallies(@NonNull Date date) {
        Map<String, IndicatorTally> tallyMap = new HashMap<>();

        SQLiteDatabase database = getReadableDatabase();
        String[] columns = {ID, INDICATOR_CODE, INDICATOR_VALUE, INDICATOR_VALUE_SET, INDICATOR_VALUE_SET_FLAG, DAY};

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
            cursor = database.query(INDICATOR_DAILY_TALLY_TABLE, columns, DAY + " >= ? AND " + DAY + " < ?",
                    new String[]{String.valueOf(dayStartMillis), String.valueOf(dayEndMillis)}, null,
                    null, null, null);
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
        compositeIndicatorTally.setId(cursor.getLong(cursor.getColumnIndex(ID)));
        compositeIndicatorTally.setIndicatorCode(cursor.getString(cursor.getColumnIndex(INDICATOR_CODE)));
        compositeIndicatorTally.setValueSetFlag(cursor.getInt(cursor.getColumnIndex(INDICATOR_VALUE_SET_FLAG)) == 1);

        if (compositeIndicatorTally.isValueSet()) {
            compositeIndicatorTally.setValueSet(cursor.getString(cursor.getColumnIndex(INDICATOR_VALUE_SET)));
        } else {
            compositeIndicatorTally.setCount(cursor.getFloat(cursor.getColumnIndex(INDICATOR_VALUE)));
        }

        compositeIndicatorTally.setCreatedAt(new Date(cursor.getLong(cursor.getColumnIndex(DAY))));
        return compositeIndicatorTally;
    }

    public ContentValues createContentValues(@NonNull CompositeIndicatorTally compositeIndicatorTally) {
        ContentValues values = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat(ReportingLibrary.getInstance().getDateFormat(), Locale.getDefault());
        values.put(INDICATOR_CODE, compositeIndicatorTally.getIndicatorCode());

        if (compositeIndicatorTally.isValueSet()) {
            values.put(INDICATOR_VALUE_SET, compositeIndicatorTally.getValueSet());
        } else {
            values.put(INDICATOR_VALUE, compositeIndicatorTally.getFloatCount());
        }

        values.put(INDICATOR_VALUE_SET_FLAG, compositeIndicatorTally.isValueSet());
        values.put(DAY, dateFormat.format(compositeIndicatorTally.getCreatedAt()));
        return values;
    }

    public static void addValueSetColumns(@NonNull SQLiteDatabase database) {
        // Change indicator_value to be nullable
        // Add the two fields
        // Set the default for the is_value_set to false
        database.execSQL("PRAGMA foreign_keys=off");
        database.beginTransaction();

        database.execSQL("ALTER TABLE " + INDICATOR_DAILY_TALLY_TABLE + " RENAME TO old_" + INDICATOR_DAILY_TALLY_TABLE);
        database.execSQL(CREATE_DAILY_TALLY_TABLE);

        String copyDataSQL = String.format("INSERT INTO %s(%s, %s, %s) SELECT %s, %s, %s FROM old_%s",
                INDICATOR_DAILY_TALLY_TABLE,
                INDICATOR_CODE,
                INDICATOR_VALUE,
                DAY,
                INDICATOR_CODE,
                INDICATOR_VALUE,
                DAY,
                INDICATOR_DAILY_TALLY_TABLE);

        // Copy over the data
        database.execSQL(copyDataSQL);
        database.execSQL("DROP TABLE old_" + INDICATOR_DAILY_TALLY_TABLE);

        database.setTransactionSuccessful();
        database.endTransaction();
        database.execSQL("PRAGMA foreign_keys=on;");
    }

    public static void aggregateDailyTallies(@NonNull SQLiteDatabase database) {
        // Code to migrate the code over from incremental tallies should be written here
    }

}
