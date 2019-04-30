package org.smartregister.reporting.dao;

import org.smartregister.reporting.domain.IndicatorQuery;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.domain.ReportIndicator;

import java.util.List;
import java.util.Map;

public interface ReportIndicatorDao {

    void addReportIndicator(ReportIndicator indicator);

    void addIndicatorQuery(IndicatorQuery indicatorQuery);

    List<Map<String, IndicatorTally>> getIndicatorsDailyTallies();

    void generateDailyIndicatorTallies(String lastProcessedDate); // For all persisted indicators
}
