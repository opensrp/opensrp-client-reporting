package org.smartregister.reporting.repository;

import android.content.ContentValues;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.reporting.model.ReportIndicator;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a IndicatorRepository class to allow storing and retrieval of details for a defined indicator.
 * The details includes the ReportIndicator description and KEY/CODE
 *
 * @author allan
 */

public class IndicatorRepository extends BaseRepository {

    public static final String ID = "_id";
    public static String INDICATOR_CODE = "indicator_code";
    public static String INDICATOR_DESCRIPTION = "description";
    public static String INDICATOR_TYPE = "type";
    public static String INDICATOR_TABLE = "indicators";

    public static String CREATE_TABLE_INDICATOR = "CREATE TABLE " + INDICATOR_TABLE + " (" + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            INDICATOR_CODE + "TEXT NOT NULL, " + INDICATOR_DESCRIPTION + " TEXT," + INDICATOR_TYPE + " TEXT)";

    public IndicatorRepository(Repository repository) {
        super(repository);
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_INDICATOR);
    }

    public void add(ReportIndicator indicator) {
        if (indicator == null) {
            return;
        }

        SQLiteDatabase database = getWritableDatabase();
        database.insert(INDICATOR_TABLE, null, createContentValues(indicator));

    }

    public ReportIndicator getIndicatorByCode(String code) {

        SQLiteDatabase database = getReadableDatabase();
        String[] columns = {};
        String selection = INDICATOR_CODE + " = ?";
        String[] selectionArgs = {code};

        Cursor cursor = database.query(INDICATOR_TABLE, columns, selection, selectionArgs, null, null, null, null);

        return buildReportIndicatorsFromCursor(cursor).get(0); // We're expecting only one
    }

    public List<ReportIndicator> getAllIndicators() {
        List<ReportIndicator> indicators = new ArrayList<>();

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(INDICATOR_TABLE, null, null, null, null, null, null, null);

        return buildReportIndicatorsFromCursor(cursor);
    }

    public List<ReportIndicator> buildReportIndicatorsFromCursor(Cursor cursor) {

        List<ReportIndicator> reportIndicators = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ReportIndicator indicator = new ReportIndicator();
                indicator.setId(cursor.getLong(cursor.getColumnIndex(ID)));
                indicator.setKey(cursor.getString(cursor.getColumnIndex(INDICATOR_CODE)));
                indicator.setDescription(cursor.getString(cursor.getColumnIndex(INDICATOR_DESCRIPTION)));
                indicator.setType(cursor.getString(cursor.getColumnIndex(INDICATOR_TYPE)));
            }
        }

        return reportIndicators;
    }

    public ContentValues createContentValues(ReportIndicator indicator) {
        ContentValues values = new ContentValues();
        values.put(ID, indicator.getId());
        values.put(INDICATOR_CODE, indicator.getKey());
        values.put(INDICATOR_DESCRIPTION, indicator.getDescription());
        values.put(INDICATOR_TYPE, indicator.getType());
        return values;
    }

}
