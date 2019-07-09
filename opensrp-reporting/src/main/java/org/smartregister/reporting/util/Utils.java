package org.smartregister.reporting.util;

import android.support.annotation.NonNull;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2019-07-09
 */

public class Utils {

    public static boolean isColumnExists(@NonNull SQLiteDatabase sqliteDatabase,
                                         @NonNull String tableName,
                                         @NonNull String columnToFind) {
        Cursor cursor = null;

        try {
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
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
