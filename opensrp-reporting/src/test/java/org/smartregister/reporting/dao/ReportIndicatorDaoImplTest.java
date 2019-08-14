package org.smartregister.reporting.dao;

import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2019-08-14
 */

@RunWith(MockitoJUnitRunner.class)
public class ReportIndicatorDaoImplTest {

    private ReportIndicatorDaoImpl reportIndicatorDao;

    @Mock
    private IndicatorQueryRepository indicatorQueryRepository;

    @Mock
    private DailyIndicatorCountRepository dailyIndicatorCountRepository;

    @Mock
    private IndicatorRepository indicatorRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        reportIndicatorDao = new ReportIndicatorDaoImpl(indicatorQueryRepository, dailyIndicatorCountRepository, indicatorRepository);
    }

    @Test
    public void getReportEventDatesShouldReturnCurrentDateWhenNoDatesRetrievedFromEventTable() {
        Date timeNow = Calendar.getInstance().getTime();
        SQLiteDatabase database = Mockito.mock(SQLiteDatabase.class);

        Mockito.doReturn(new ArrayList<HashMap<String, String>>())
                .when(dailyIndicatorCountRepository)
                .rawQuery(Mockito.any(SQLiteDatabase.class), Mockito.anyString());

        LinkedHashMap<String, Date> reportEventDates = reportIndicatorDao.getReportEventDates(timeNow, null, database);

        assertEquals(1, reportEventDates.size());
    }
}