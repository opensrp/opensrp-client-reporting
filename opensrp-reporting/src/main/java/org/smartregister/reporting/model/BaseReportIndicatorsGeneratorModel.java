package org.smartregister.reporting.model;

import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.contract.ReportIndicatorGeneratorContract;
import org.smartregister.reporting.dao.ReportIndicatorDaoImpl;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;

import java.util.Map;

public class BaseReportIndicatorsGeneratorModel implements ReportIndicatorGeneratorContract.Model {

    private IndicatorRepository indicatorRepository;
    private IndicatorQueryRepository indicatorQueryRepository;
    private DailyIndicatorCountRepository dailyIndicatorCountRepository;
    private ReportIndicatorDaoImpl dao;

    public BaseReportIndicatorsGeneratorModel() {
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
    public Map<String, IndicatorTally> getIndicatorsDailyTallies() {
        return dao.getIndicatorsDailyTallies();
    }

    private void initialiseRepositories() {
        ReportingLibrary reportingLibrary = ReportingLibrary.getInstance();
        indicatorRepository = reportingLibrary.indicatorRepository();
        indicatorQueryRepository = reportingLibrary.indicatorQueryRepository();
        dailyIndicatorCountRepository = reportingLibrary.dailyIndicatorCountRepository();
    }
}
