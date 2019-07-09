package org.smartregister.reporting.util;

import android.support.annotation.NonNull;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2019-07-09
 */

public class Utils {

    public static final String TAG = Utils.class.getName();

    /**
     * Checks if a column exists on the table. An {@link Exception} is expected to be thrown by the sqlite
     * database in case of anything weird such as the query being wrongly executed. This method is used
     * to determine critical operations such as migrations that if not done will case data corruption.
     * It is therefore safe to let the app crash instead of handling the error. So that the developer/user
     * can fix the issue before continuing with any other operations.
     *
     * @param sqliteDatabase
     * @param tableName
     * @param columnToFind
     * @return
     */
    public static boolean isColumnExists(@NonNull SQLiteDatabase sqliteDatabase,
                                         @NonNull String tableName,
                                         @NonNull String columnToFind) {
        Cursor cursor = null;
        cursor = sqliteDatabase.rawQuery(
                "PRAGMA table_info(" + tableName + ")",
                null
        );

        int nameColumnIndex = cursor.getColumnIndexOrThrow("name");
        while (cursor.moveToNext()) {
            String name = cursor.getString(nameColumnIndex);

            if (name.equals(columnToFind)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a table exists on the table. An {@link Exception} is expected to be thrown by the sqlite
     * database in case of anything weird such as the query being wrongly executed. This method is used
     * to determine critical operations such as migrations that if not done will case data corruption.
     * It is therefore safe to let the app crash instead of handling the error. So that the developer/user
     * can fix the issue before continuing with any other operations.
     *
     * @param sqliteDatabase
     * @param tableName
     * @return
     */
    public static boolean isTableExists(@NonNull SQLiteDatabase sqliteDatabase, @NonNull String tableName) {
        Cursor cursor = sqliteDatabase.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'",
                null
        );

        int nameColumnIndex = cursor.getColumnIndexOrThrow("name");
        while (cursor.moveToNext()) {
            String name = cursor.getString(nameColumnIndex);

            if (name.equals(tableName)) {
                return true;
            }
        }

        return false;
    }
}
