package org.smartregister.reporting.dao;


import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.reporting.model.IndicatorTally;

import java.util.List;

public interface ReportIndicatorDao {

    List<IndicatorTally> getIndicatorsDailyTallies();

    void generateDailyIndicatorTallies(SQLiteDatabase database, String lastProcessedDate); // For all persisted indicators
}
