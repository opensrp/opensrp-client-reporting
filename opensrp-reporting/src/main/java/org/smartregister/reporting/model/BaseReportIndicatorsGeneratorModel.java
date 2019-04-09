package org.smartregister.reporting.model;

import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.contract.ReportIndicatorGeneratorContract;
import org.smartregister.reporting.dao.ReportIndicatorDaoImpl;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;

import java.util.List;

public class BaseReportIndicatorsGeneratorModel implements ReportIndicatorGeneratorContract.Model {
    @Override
    public List<IndicatorTally> getIndicatorsDailyTallies() {
        DailyIndicatorCountRepository dailyIndicatorCountRepository = ReportingLibrary.getInstance().dailyIndicatorCountRepository();
        ReportIndicatorDaoImpl dao = new ReportIndicatorDaoImpl();
        dao.setDailyIndicatorCountRepository(dailyIndicatorCountRepository);
        return new ReportIndicatorDaoImpl().getIndicatorsDailyTallies();
    }
}
