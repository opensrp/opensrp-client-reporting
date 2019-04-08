package org.smartregister.reporting.dao;


import net.sqlcipher.database.SQLiteDatabase;

import java.util.List;
import java.util.Map;

public interface ReportIndicatorDao {

    List<Map<String, Integer>> getIndicatorsDailyTallies();

    void generateDailyIndicatorTallies(SQLiteDatabase database, String lastProcessedDate); // For all persisted indicators
}
