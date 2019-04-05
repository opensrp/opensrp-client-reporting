package org.smartregister.reporting.repository;

import android.content.ContentValues;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.reporting.model.IndicatorTally;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    public static String INDICATOR_DAILY_TALLY_TABLE = "indicator_daily_tally";

    public static String CREATE_DAILY_TALLY_TABLE = "CREATE TABLE " + INDICATOR_DAILY_TALLY_TABLE + " (" + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            INDICATOR_CODE + " TEXT NOT NULL, " + INDICATOR_VALUE + " INTEGER NOT NULL, " + DAY + "DATETIME NOT NULL DEFAULT (DATETIME('now')))";

    public DailyIndicatorCountRepository(Repository repository) {
        super(repository);
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_DAILY_TALLY_TABLE);
    }

    public void add(IndicatorTally indicatorTally) {
        if (indicatorTally == null) {
            return;
        }

        SQLiteDatabase database = getWritableDatabase();
        database.insert(INDICATOR_DAILY_TALLY_TABLE, null, createContentValues(indicatorTally));
    }

    public List<IndicatorTally> getDailyTalliesForIndicator(String indicatorCode) {
        List<IndicatorTally> tallies = new ArrayList<>();

        SQLiteDatabase database = getReadableDatabase();
        String[] columns = {ID, INDICATOR_CODE, INDICATOR_VALUE, DAY};
        String selection = INDICATOR_CODE + " = ?";
        String[] selectionArgs = {indicatorCode};

        Cursor cursor = database.query(INDICATOR_DAILY_TALLY_TABLE, columns, selection, selectionArgs, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                IndicatorTally indicatorTally = new IndicatorTally();
                indicatorTally.setId(cursor.getLong(cursor.getColumnIndex(ID)));
                indicatorTally.setCount(cursor.getInt(cursor.getColumnIndex(INDICATOR_VALUE)));
                indicatorTally.setIndicatorCode(cursor.getString(cursor.getColumnIndex(INDICATOR_CODE)));
                indicatorTally.setCreatedAt(new Date(cursor.getLong(cursor.getColumnIndex(DAY))));
                tallies.add(indicatorTally);
            }
        }
        return tallies;
    }

    public ContentValues createContentValues(IndicatorTally indicatorTally) {
        ContentValues values = new ContentValues();
        values.put(INDICATOR_CODE, indicatorTally.getIndicatorCode());
        values.put(INDICATOR_VALUE, indicatorTally.getIndicatorCode());
        values.put(DAY, Calendar.getInstance().getTimeInMillis());
        return values;
    }
}
