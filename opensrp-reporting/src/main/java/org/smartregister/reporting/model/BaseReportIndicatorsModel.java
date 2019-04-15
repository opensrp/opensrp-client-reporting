package org.smartregister.reporting.model;

import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.dao.ReportIndicatorDaoImpl;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;

import java.util.List;
import java.util.Map;

public class BaseReportIndicatorsModel implements ReportContract.Model {

    private IndicatorRepository indicatorRepository;
    private IndicatorQueryRepository indicatorQueryRepository;
    private DailyIndicatorCountRepository dailyIndicatorCountRepository;
    private ReportIndicatorDaoImpl dao;

    public BaseReportIndicatorsModel() {
        initialiseRepositories();
        dao = new ReportIndicatorDaoImpl(indicatorQueryRepository, dailyIndicatorCountRepository, indicatorRepository);
    }

    @Override
    public void addIndicator(ReportIndicator indicator) {
        dao.addReportIndicator(indicator);
    }

    @Override
    public void addIndicatorQuery(IndicatorQuery indicatorQuery) {
        dao.addIndicatorQuery(indicatorQuery);
    }

    @Override
    public List<Map<String, IndicatorTally>> getIndicatorsDailyTallies() {
        return dao.getIndicatorsDailyTallies();
    }

    private void initialiseRepositories() {
        ReportingLibrary reportingLibrary = ReportingLibrary.getInstance();
        indicatorRepository = reportingLibrary.indicatorRepository();
        indicatorQueryRepository = reportingLibrary.indicatorQueryRepository();
        dailyIndicatorCountRepository = reportingLibrary.dailyIndicatorCountRepository();
    }
}
