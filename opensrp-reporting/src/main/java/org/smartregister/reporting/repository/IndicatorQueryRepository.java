package org.smartregister.reporting.repository;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.reporting.domain.IndicatorQuery;
import org.smartregister.reporting.util.Utils;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Repository for the different indicators. The repository will store and allow retrieval of a query associated
 * with an ReportIndicator
 *
 * @author allan
 */
public class IndicatorQueryRepository extends BaseRepository {

    public static final String ID = "_id";
    public static String QUERY = "indicator_query";
    public static String INDICATOR_CODE = "indicator_code";
    public static String DB_VERSION = "db_version";
    public static String INDICATOR_QUERY_TABLE = "indicator_queries";
    public static String INDICATOR_QUERY_IS_MULTI_RESULT = "indicator_is_multi_result";

    public static String CREATE_TABLE_INDICATOR_QUERY = "CREATE TABLE " + INDICATOR_QUERY_TABLE + "(" + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            QUERY + " TEXT NOT NULL, " + INDICATOR_CODE + " TEXT NOT NULL, " + INDICATOR_QUERY_IS_MULTI_RESULT + " BOOLEAN NOT NULL DEFAULT 0, " + DB_VERSION + " INTEGER)";

    public IndicatorQueryRepository(Repository repository) {
        super(repository);
    }

    public static void performMigrations(@NonNull SQLiteDatabase database) {
        // Perform migrations
        if (Utils.isTableExists(database, INDICATOR_QUERY_TABLE) && !Utils.isColumnExists(database, INDICATOR_QUERY_TABLE, INDICATOR_QUERY_IS_MULTI_RESULT)) {
            addMultiResultFlagField(database);
            aggregateDailyTallies(database);
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
        sqLiteDatabase.insert(INDICATOR_QUERY_TABLE, null, createContentValues(indicatorQuery));
    }

    public void truncateTable() {
        truncateTable(getWritableDatabase());
    }

    public void truncateTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + INDICATOR_QUERY_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE_INDICATOR_QUERY);
        sqLiteDatabase.delete("sqlite_sequence", "name = ?", new String[]{INDICATOR_QUERY_TABLE});
    }

    public Map<String, IndicatorQuery> getAllIndicatorQueries() {
        Map<String, IndicatorQuery> queries = new HashMap<>();
        SQLiteDatabase database = getReadableDatabase();
        String[] columns = {ID, INDICATOR_CODE, QUERY, INDICATOR_QUERY_IS_MULTI_RESULT, DB_VERSION};
        Cursor cursor = database.query(INDICATOR_QUERY_TABLE, columns, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                queries.put(cursor.getString(cursor.getColumnIndex(INDICATOR_CODE)), processCursorRow(cursor));
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
        String[] columns = {ID, INDICATOR_CODE, QUERY, INDICATOR_QUERY_IS_MULTI_RESULT, DB_VERSION};
        String selection = QUERY + " = ?";
        String[] selectionArgs = {indicatorCode};

        Cursor cursor = database.query(INDICATOR_QUERY_TABLE, columns, selection, selectionArgs, null, null, null, null);

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
        values.put(ID, indicatorQuery.getId());
        values.put(QUERY, indicatorQuery.getQuery());
        values.put(INDICATOR_CODE, indicatorQuery.getIndicatorCode());
        values.put(DB_VERSION, indicatorQuery.getDbVersion());
        values.put(INDICATOR_QUERY_IS_MULTI_RESULT, indicatorQuery.isMultiResult());
        return values;
    }

    private IndicatorQuery processCursorRow(@NonNull Cursor cursor) {
        IndicatorQuery indicatorQuery = new IndicatorQuery();
        indicatorQuery.setId(cursor.getLong(cursor.getColumnIndex(ID)));
        indicatorQuery.setIndicatorCode(cursor.getString(cursor.getColumnIndex(INDICATOR_CODE)));
        indicatorQuery.setQuery(cursor.getString(cursor.getColumnIndex(QUERY)));
        indicatorQuery.setMultiResult(cursor.getInt(cursor.getColumnIndex(INDICATOR_QUERY_IS_MULTI_RESULT)) == 1);
        indicatorQuery.setDbVersion(cursor.getInt(cursor.getColumnIndex(DB_VERSION)));

        return indicatorQuery;
    }


    public static void addMultiResultFlagField(@NonNull SQLiteDatabase database) {
        // Change indicator_value to be nullable
        // Add the two fields
        // Set the default for the is_value_set to false

        // DROP THE TABLE, CREATE A NEW TABLE
        database.execSQL("PRAGMA foreign_keys=off");
        database.beginTransaction();

        database.execSQL("ALTER TABLE " + INDICATOR_QUERY_TABLE + " ADD COLUMN " + INDICATOR_QUERY_IS_MULTI_RESULT + " BOOLEAN NOT NULL DEFAULT 0");

        database.setTransactionSuccessful();
        database.endTransaction();
        database.execSQL("PRAGMA foreign_keys=on;");
    }

    public static void aggregateDailyTallies(@NonNull SQLiteDatabase database) {
        // Code to migrate the code over from incremental tallies should be written here
    }

}
