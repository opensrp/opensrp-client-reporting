package org.smartregister.reporting.model;

import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.dao.ReportIndicatorDaoImpl;

import java.util.List;
import java.util.Map;

public class BaseReportIndicatorsModel implements ReportContract.Model {

    private ReportingLibrary reportingLibrary = ReportingLibrary.getInstance();
    private ReportIndicatorDaoImpl dao = new ReportIndicatorDaoImpl();

    @Override
    public void addIndicator(ReportIndicator indicator) {
        dao.setIndicatorRepository(reportingLibrary.indicatorRepository());
        dao.addReportIndicator(indicator);
    }

    @Override
    public void addIndicatorQuery(IndicatorQuery indicatorQuery) {
        dao.setIndicatorQueryRepository(reportingLibrary.indicatorQueryRepository());
        dao.addIndicatorQuery(indicatorQuery);
    }

    @Override
    public List<Map<String, IndicatorTally>> getIndicatorsDailyTallies() {
        dao.setDailyIndicatorCountRepository(reportingLibrary.dailyIndicatorCountRepository());
        return dao.getIndicatorsDailyTallies();
    }
}
