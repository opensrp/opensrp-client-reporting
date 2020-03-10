package org.smartregister.reporting.repository;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.reporting.domain.IndicatorQuery;
import org.smartregister.reporting.util.Constants;
import org.smartregister.reporting.util.ReportingUtil;
import org.smartregister.reporting.util.ReportingUtils;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository for the different indicators. The repository will store and allow retrieval of a query associated
 * with an ReportIndicator
 *
 * @author allan
 */
public class IndicatorQueryRepository extends BaseRepository {

    public static String CREATE_TABLE_INDICATOR_QUERY = "CREATE TABLE " + Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE
            + "(" + Constants.IndicatorQueryRepository.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + Constants.IndicatorQueryRepository.QUERY + " TEXT NOT NULL, "
            + Constants.IndicatorQueryRepository.INDICATOR_CODE + " TEXT NOT NULL, "
            + Constants.IndicatorQueryRepository.INDICATOR_QUERY_IS_MULTI_RESULT + " BOOLEAN NOT NULL DEFAULT 0, "
            + Constants.IndicatorQueryRepository.INDICATOR_QUERY_EXPECTED_INDICATORS + " TEXT, "
            + Constants.IndicatorQueryRepository.INDICATOR_GROUPING + " TEXT, "
            + Constants.IndicatorQueryRepository.DB_VERSION + " INTEGER)";

    public static void performMigrations(@NonNull SQLiteDatabase database) {
        // Perform migrations
        if (ReportingUtils.isTableExists(database, Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE)
                && !ReportingUtils.isColumnExists(database, Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE, Constants.IndicatorQueryRepository.INDICATOR_QUERY_IS_MULTI_RESULT)) {
            addMultiResultFlagField(database);
        }

        if (ReportingUtils.isTableExists(database, Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE)
                && !ReportingUtils.isColumnExists(database, Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE
                , Constants.IndicatorQueryRepository.INDICATOR_QUERY_EXPECTED_INDICATORS)) {
            addExpectedIndicatorColumn(database);
        }

        if (ReportingUtils.isTableExists(database, Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE)
                && !ReportingUtils.isColumnExists(database, Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE, Constants.IndicatorQueryRepository.INDICATOR_GROUPING)) {
            addGroupingColumn(database);
        }
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_INDICATOR_QUERY);
    }

    public void add(IndicatorQuery indicatorQuery) {
        add(indicatorQuery, getWritableDatabase());
    }

    public void add(IndicatorQuery indicatorQuery, SQLiteDatabase sqLiteDatabase) {
        if (indicatorQuery == null) {
            return;
        }
        sqLiteDatabase.insert(Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE, null, createContentValues(indicatorQuery));
    }

    public void truncateTable() {
        truncateTable(getWritableDatabase());
    }

    public void truncateTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE_INDICATOR_QUERY);
        sqLiteDatabase.delete("sqlite_sequence", "name = ?", new String[]{Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE});
    }

    public Map<String, IndicatorQuery> getAllIndicatorQueries() {
        Map<String, IndicatorQuery> queries = new HashMap<>();
        SQLiteDatabase database = getReadableDatabase();
        String[] columns = {Constants.IndicatorQueryRepository.ID, Constants.IndicatorQueryRepository.INDICATOR_CODE
                , Constants.IndicatorQueryRepository.QUERY, Constants.IndicatorQueryRepository.INDICATOR_QUERY_IS_MULTI_RESULT
                , Constants.IndicatorQueryRepository.DB_VERSION, Constants.IndicatorQueryRepository.INDICATOR_QUERY_EXPECTED_INDICATORS};
        Cursor cursor = database.query(Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE, columns
                , null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                queries.put(cursor.getString(cursor.getColumnIndex(Constants.IndicatorQueryRepository.INDICATOR_CODE)), processCursorRow(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return queries;
    }

    @Nullable
    public IndicatorQuery findQueryByIndicatorCode(String indicatorCode) {
        SQLiteDatabase database = getReadableDatabase();
        IndicatorQuery indicatorQuery = null;
        String[] columns = {Constants.IndicatorQueryRepository.ID, Constants.IndicatorQueryRepository.INDICATOR_CODE
                , Constants.IndicatorQueryRepository.QUERY, Constants.IndicatorQueryRepository.INDICATOR_QUERY_IS_MULTI_RESULT
                , Constants.IndicatorQueryRepository.DB_VERSION, Constants.IndicatorQueryRepository.INDICATOR_QUERY_EXPECTED_INDICATORS};
        String selection = Constants.IndicatorQueryRepository.QUERY + " = ?";
        String[] selectionArgs = {indicatorCode};

        Cursor cursor = database.query(Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE, columns, selection, selectionArgs
                , null, null, null, null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                indicatorQuery = processCursorRow(cursor);
            }
            cursor.close();
        }

        return indicatorQuery;
    }

    public ContentValues createContentValues(IndicatorQuery indicatorQuery) {
        ContentValues values = new ContentValues();
        values.put(Constants.IndicatorQueryRepository.ID, indicatorQuery.getId());
        values.put(Constants.IndicatorQueryRepository.QUERY, indicatorQuery.getQuery());
        values.put(Constants.IndicatorQueryRepository.INDICATOR_CODE, indicatorQuery.getIndicatorCode());
        values.put(Constants.IndicatorQueryRepository.DB_VERSION, indicatorQuery.getDbVersion());
        values.put(Constants.IndicatorQueryRepository.INDICATOR_QUERY_IS_MULTI_RESULT, indicatorQuery.isMultiResult());
        values.put(Constants.IndicatorQueryRepository.INDICATOR_GROUPING, indicatorQuery.getGrouping());
        values.put(Constants.IndicatorQueryRepository.INDICATOR_QUERY_EXPECTED_INDICATORS,
                indicatorQuery.getExpectedIndicators() != null ?
                        new Gson().toJson(indicatorQuery.getExpectedIndicators()) : null);
        return values;
    }

    private IndicatorQuery processCursorRow(@NonNull Cursor cursor) {
        IndicatorQuery indicatorQuery = new IndicatorQuery();
        indicatorQuery.setId(cursor.getLong(cursor.getColumnIndex(Constants.IndicatorQueryRepository.ID)));
        indicatorQuery.setIndicatorCode(cursor.getString(cursor.getColumnIndex(Constants.IndicatorQueryRepository.INDICATOR_CODE)));
        indicatorQuery.setQuery(cursor.getString(cursor.getColumnIndex(Constants.IndicatorQueryRepository.QUERY)));
        indicatorQuery.setMultiResult(cursor.getInt(cursor.getColumnIndex(Constants.IndicatorQueryRepository.INDICATOR_QUERY_IS_MULTI_RESULT)) == 1);
        indicatorQuery.setGrouping(cursor.getString(cursor.getColumnIndex(Constants.IndicatorQueryRepository.INDICATOR_GROUPING)));
        indicatorQuery.setDbVersion(cursor.getInt(cursor.getColumnIndex(Constants.IndicatorQueryRepository.DB_VERSION)));

        String expectedResults = cursor.getString(cursor.getColumnIndex(Constants.IndicatorQueryRepository.INDICATOR_QUERY_EXPECTED_INDICATORS));

        indicatorQuery.setExpectedIndicators(TextUtils.isEmpty(expectedResults) ? null :
                (List<String>) new Gson().fromJson(expectedResults, new TypeToken<List<String>>() {}.getType()));

        return indicatorQuery;
    }


    public static void addMultiResultFlagField(@NonNull SQLiteDatabase database) {
        // Change indicator_value to be nullable
        // Add the two fields
        // Set the default for the is_value_set to false

        // DROP THE TABLE, CREATE A NEW TABLE
        database.execSQL("PRAGMA foreign_keys=off");
        database.beginTransaction();

        database.execSQL("ALTER TABLE " + Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE
                + " ADD COLUMN " + Constants.IndicatorQueryRepository.INDICATOR_QUERY_IS_MULTI_RESULT + " BOOLEAN NOT NULL DEFAULT 0");

        database.setTransactionSuccessful();
        database.endTransaction();
        database.execSQL("PRAGMA foreign_keys=on;");
    }

    public static void addExpectedIndicatorColumn(@NonNull SQLiteDatabase database) {
        database.execSQL("PRAGMA foreign_keys=off");
        database.beginTransaction();

        database.execSQL("ALTER TABLE " + Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE
                + " ADD COLUMN " + Constants.IndicatorQueryRepository.INDICATOR_QUERY_EXPECTED_INDICATORS + " TEXT");
        database.setTransactionSuccessful();
        database.endTransaction();
        database.execSQL("PRAGMA foreign_keys=on;");
    }

    public static void addGroupingColumn(@NonNull SQLiteDatabase database) {
        database.execSQL("PRAGMA foreign_keys=off");
        database.beginTransaction();

        database.execSQL("ALTER TABLE " + Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE
                + " ADD COLUMN " + Constants.IndicatorQueryRepository.INDICATOR_GROUPING + " TEXT");
        database.setTransactionSuccessful();
        database.endTransaction();
        database.execSQL("PRAGMA foreign_keys=on;");
    }

}
