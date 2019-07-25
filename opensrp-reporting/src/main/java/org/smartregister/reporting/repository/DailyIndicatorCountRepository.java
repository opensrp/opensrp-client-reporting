package org.smartregister.reporting.repository;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.util.Constants;
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

    public static String CREATE_DAILY_TALLY_TABLE = "CREATE TABLE " + Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE
            + "(" + Constants.DailyIndicatorCountRepository.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + Constants.DailyIndicatorCountRepository.INDICATOR_CODE + " TEXT NOT NULL, " + Constants.DailyIndicatorCountRepository.INDICATOR_VALUE
            + " INTEGER, " + Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET + " TEXT, "
            + Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET_FLAG + " BOOLEAN NOT NULL default 0, "
            + Constants.DailyIndicatorCountRepository.DAY + " DATETIME NOT NULL DEFAULT (DATETIME('now')))";

    private static String CREATE_UNIQUE_CONSTRAINT = "CREATE UNIQUE INDEX indicator_daily_tally_ix ON "
            + Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE + " ( " + Constants.DailyIndicatorCountRepository.INDICATOR_CODE
            + " , " + Constants.DailyIndicatorCountRepository.DAY + " ) ";

    public DailyIndicatorCountRepository(Repository repository) {
        super(repository);
    }

    public static void performMigrations(@NonNull SQLiteDatabase database) {
        // Perform migrations
        if (Utils.isTableExists(database, Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE)
                && !Utils.isColumnExists(database, Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE
                , Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET)) {
            addValueSetColumns(database);
            aggregateDailyTallies(database);
        }
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_DAILY_TALLY_TABLE);
        database.execSQL(CREATE_UNIQUE_CONSTRAINT);
    }

    public void add(IndicatorTally indicatorTally) {
        if (indicatorTally == null) {
            return;
        }

        SQLiteDatabase database = getWritableDatabase();
        database.delete(Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE
                , Constants.DailyIndicatorCountRepository.INDICATOR_CODE + " = ? AND " + Constants.DailyIndicatorCountRepository.DAY + " = ? "
                , new String[]{indicatorTally.getIndicatorCode(), new SimpleDateFormat(ReportingLibrary.getInstance().getDateFormat(), Locale.getDefault()).format(indicatorTally.getCreatedAt())});
        database.insert(Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE, null, createContentValues(indicatorTally));
    }

    public List<Map<String, IndicatorTally>> getAllDailyTallies() {
        List<Map<String, IndicatorTally>> indicatorTallies = new ArrayList<>();
        Map<String, IndicatorTally> tallyMap;

        SQLiteDatabase database = getReadableDatabase();
        String[] columns = {Constants.DailyIndicatorCountRepository.ID
                , Constants.DailyIndicatorCountRepository.INDICATOR_CODE
                , Constants.DailyIndicatorCountRepository.INDICATOR_VALUE
                , Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET
                , Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET_FLAG
                , Constants.DailyIndicatorCountRepository.DAY};

        Cursor cursor = null;
        try {
            cursor = database.query(Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE
                    , columns, null, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    tallyMap = new HashMap<>();
                    IndicatorTally indicatorTally = processCursorRow(cursor);
                    tallyMap.put(indicatorTally.getIndicatorCode(), indicatorTally);
                    indicatorTallies.add(tallyMap);
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

    public Map<String, IndicatorTally> getDailyTallies(@NonNull Date date) {
        Map<String, IndicatorTally> tallyMap = new HashMap<>();

        SQLiteDatabase database = getReadableDatabase();
        String[] columns = {Constants.DailyIndicatorCountRepository.ID
                , Constants.DailyIndicatorCountRepository.INDICATOR_CODE
                , Constants.DailyIndicatorCountRepository.INDICATOR_VALUE
                , Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET
                , Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET_FLAG
                , Constants.DailyIndicatorCountRepository.DAY};

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
            cursor = database.query(Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE
                    , columns
                    , Constants.DailyIndicatorCountRepository.DAY + " >= ? AND " + Constants.DailyIndicatorCountRepository.DAY + " < ?",
                    new String[]{String.valueOf(dayStartMillis), String.valueOf(dayEndMillis)}
                    , null,null, null, null);
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    IndicatorTally indicatorTally = processCursorRow(cursor);
                    tallyMap.put(indicatorTally.getIndicatorCode(), indicatorTally);
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

    private IndicatorTally processCursorRow(@NonNull Cursor cursor) {
        IndicatorTally indicatorTally = new IndicatorTally();
        indicatorTally.setId(cursor.getLong(cursor.getColumnIndex(Constants.DailyIndicatorCountRepository.ID)));
        indicatorTally.setIndicatorCode(cursor.getString(cursor.getColumnIndex(Constants.DailyIndicatorCountRepository.INDICATOR_CODE)));
        indicatorTally.setValueSetFlag(cursor.getInt(cursor.getColumnIndex(Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET_FLAG)) == 1);

        if (indicatorTally.isValueSet()) {
            indicatorTally.setValueSet(cursor.getString(cursor.getColumnIndex(Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET)));
        } else {
            indicatorTally.setCount(cursor.getInt(cursor.getColumnIndex(Constants.DailyIndicatorCountRepository.INDICATOR_VALUE)));
        }

        indicatorTally.setCreatedAt(new Date(cursor.getLong(cursor.getColumnIndex(Constants.DailyIndicatorCountRepository.DAY))));
        return indicatorTally;
    }

    public ContentValues createContentValues(IndicatorTally indicatorTally) {
        ContentValues values = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat(ReportingLibrary.getInstance().getDateFormat(), Locale.getDefault());
        values.put(Constants.DailyIndicatorCountRepository.INDICATOR_CODE, indicatorTally.getIndicatorCode());

        if (indicatorTally.isValueSet()) {
            values.put(Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET, indicatorTally.getValueSet());
        } else {
            values.put(Constants.DailyIndicatorCountRepository.INDICATOR_VALUE, indicatorTally.getCount());
        }

        values.put(Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET_FLAG, indicatorTally.isValueSet());
        values.put(Constants.DailyIndicatorCountRepository.DAY, dateFormat.format(indicatorTally.getCreatedAt()));
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
    }

}
