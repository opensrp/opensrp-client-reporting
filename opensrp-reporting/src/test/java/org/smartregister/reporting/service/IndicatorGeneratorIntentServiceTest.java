package org.smartregister.reporting.service;

import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import org.smartregister.reporting.dao.ReportIndicatorDaoImpl;
import org.smartregister.repository.AllSharedPreferences;

@RunWith(MockitoJUnitRunner.class)
public class IndicatorGeneratorIntentServiceTest {

    @Mock
    private Intent intent;

    @Mock
    private ReportIndicatorDaoImpl reportIndicatorDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnHandleIntent() {
        IndicatorGeneratorIntentService service = Mockito.spy(new IndicatorGeneratorIntentService());

        AllSharedPreferences allSharedPreferences = Mockito.mock(AllSharedPreferences.class);

        Mockito.doReturn("123456").when(allSharedPreferences).getPreference(ReportIndicatorDaoImpl.REPORT_LAST_PROCESSED_DATE);

        Whitebox.setInternalState(service, "allSharedPreferences", allSharedPreferences);
        Whitebox.setInternalState(service, "reportIndicatorDao", reportIndicatorDao);
        service.onHandleIntent(intent);

        Mockito.verify(reportIndicatorDao).generateDailyIndicatorTallies("123456");
    }
}
