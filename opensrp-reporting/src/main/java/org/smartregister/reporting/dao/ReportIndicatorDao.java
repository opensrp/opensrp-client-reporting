package org.smartregister.reporting.dao;

import org.smartregister.reporting.model.ReportIndicator;

public interface ReportIndicatorDao {

    int getIndicatorDailyTotalCount();

    void generateIndicatorTallies(); // For all persisted indicators
}
