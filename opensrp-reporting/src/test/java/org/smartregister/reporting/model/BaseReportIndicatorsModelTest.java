package org.smartregister.reporting.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.dao.ReportIndicatorDaoImpl;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ReportingLibrary.class, BaseReportIndicatorsModel.class})
public class BaseReportIndicatorsModelTest {

    @Mock
    private ReportingLibrary reportingLibrary;

    @Mock
    private IndicatorRepository indicatorRepository;

    @Mock
    private IndicatorQueryRepository indicatorQueryRepository;

    @Mock
    private DailyIndicatorCountRepository dailyIndicatorCountRepository;

    private BaseReportIndicatorsModel model;

    private ReportIndicatorDaoImpl dao;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(ReportingLibrary.class);
        PowerMockito.when(ReportingLibrary.getInstance()).thenReturn(reportingLibrary);
        dao = PowerMockito.mock(ReportIndicatorDaoImpl.class);
        PowerMockito.whenNew(ReportIndicatorDaoImpl.class).withNoArguments().thenReturn(dao);
        model = new BaseReportIndicatorsModel();
    }

    @Test
    public void addIndicatorInvokesDaoWithCorrectParams() {
        ReportIndicator reportIndicator = Mockito.mock(ReportIndicator.class);
        ArgumentCaptor<ReportIndicator> argumentCaptor = ArgumentCaptor.forClass(ReportIndicator.class);
        Mockito.when(reportingLibrary.indicatorRepository()).thenReturn(indicatorRepository);
        Mockito.spy(model).addIndicator(reportIndicator);
        Mockito.verify(dao).addReportIndicator(argumentCaptor.capture());
        Assert.assertEquals(reportIndicator, argumentCaptor.getValue());
    }

    @Test
    public void addIndicatorQueryInvokesDaoWithCorrectParams() {
        IndicatorQuery indicatorQuery = Mockito.mock(IndicatorQuery.class);
        ArgumentCaptor<IndicatorQuery> argumentCaptor = ArgumentCaptor.forClass(IndicatorQuery.class);
        Mockito.when(reportingLibrary.indicatorQueryRepository()).thenReturn(indicatorQueryRepository);
        Mockito.spy(model).addIndicatorQuery(indicatorQuery);
        Mockito.verify(dao).addIndicatorQuery(argumentCaptor.capture());
        Assert.assertEquals(indicatorQuery, argumentCaptor.getValue());
    }

    @Test
    public void getIndicatorsDailyTalliesInvokesDao() {
        Mockito.when(reportingLibrary.dailyIndicatorCountRepository()).thenReturn(dailyIndicatorCountRepository);
        Mockito.spy(model).getIndicatorsDailyTallies();
        Mockito.verify(dao).getIndicatorsDailyTallies();
    }

}
