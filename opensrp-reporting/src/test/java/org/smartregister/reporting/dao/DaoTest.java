package org.smartregister.reporting.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.reporting.BaseUnitTest;
import org.smartregister.reporting.model.IndicatorQuery;
import org.smartregister.reporting.model.ReportIndicator;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;


public class DaoTest extends BaseUnitTest {

    @Mock
    private IndicatorQueryRepository indicatorQueryRepository;

    @Mock
    private DailyIndicatorCountRepository dailyIndicatorCountRepository;

    @Mock
    private IndicatorRepository indicatorRepository;

    @InjectMocks
    private ReportIndicatorDaoImpl reportIndicatorDao;

    private ReportIndicatorDaoImpl reportIndicatorDaoSpy;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        reportIndicatorDaoSpy = Mockito.spy(reportIndicatorDao);
    }

    @Test
    public void addReportIndicatorInvokesIndicatorRepositoryAdd() {
        ReportIndicator reportIndicator = Mockito.mock(ReportIndicator.class);
        reportIndicatorDaoSpy.addReportIndicator(reportIndicator);
        Mockito.verify(indicatorRepository, Mockito.times(1)).add(reportIndicator);
    }

    @Test
    public void addIndicatorQueryInvokesQueryRepositoryAdd() {
        IndicatorQuery indicatorQuery = Mockito.mock(IndicatorQuery.class);
        reportIndicatorDaoSpy.addIndicatorQuery(indicatorQuery);
        Mockito.verify(indicatorQueryRepository, Mockito.times(1)).add(indicatorQuery);
    }

}
