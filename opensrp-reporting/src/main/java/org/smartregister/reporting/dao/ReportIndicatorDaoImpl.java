package org.smartregister.reporting.dao;

import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;

/**
 * The ReportIndicatorDao allows for processing of Indicators. This class acts as the Interactor for
 * Presenters and also allows for use in other Services
 *
 * @author allan
 */

public class ReportIndicatorDaoImpl implements ReportIndicatorDao {

    private IndicatorQueryRepository indicatorQueryRepository;
    private DailyIndicatorCountRepository dailyIndicatorCountRepository;
    private IndicatorRepository indicatorRepository;
    private String lastProcessedDate;

    public ReportIndicatorDaoImpl() {
    }

    @Override
    public int getIndicatorDailyTotalCount() {
        return 0;
    }

    @Override
    public void generateIndicatorTallies() {

    }
}
