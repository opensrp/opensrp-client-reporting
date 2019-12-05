package org.smartregister.reporting.dao;

import net.sqlcipher.MatrixCursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.domain.CompositeIndicatorTally;
import org.smartregister.reporting.domain.IndicatorQuery;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;
import org.smartregister.repository.Repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2019-08-14
 */

@RunWith(MockitoJUnitRunner.class)
public class ReportIndicatorDaoImplTest {

    private ReportIndicatorDaoImpl reportIndicatorDao;

    @Mock
    private IndicatorRepository indicatorRepository;

    @Mock
    private DailyIndicatorCountRepository dailyIndicatorCountRepository;

    @Mock
    private IndicatorQueryRepository indicatorQueryRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        reportIndicatorDao = new ReportIndicatorDaoImpl(indicatorQueryRepository, dailyIndicatorCountRepository, indicatorRepository);
    }

    @Test
    public void executeQueryAndReturnCountShouldReturnCountFromQuery() {
        String query = "SELECT count(*) FROM persons";
        // String date = "2019-07-01";
        SQLiteDatabase database = Mockito.mock(SQLiteDatabase.class);

        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"count(*)"});
        matrixCursor.addRow(new Object[]{67F});

        ReportingLibrary.init(Mockito.mock(Context.class), Mockito.mock(Repository.class), Mockito.mock(CommonFtsObject.class), 1, 1);
        ReportingLibrary reportingLibrarySpy = Mockito.mock(ReportingLibrary.class);
        ReflectionHelpers.setStaticField(ReportingLibrary.class, "instance", reportingLibrarySpy);
        Mockito.doReturn(matrixCursor).when(database).rawQuery(Mockito.anyString(), Mockito.isNull(String[].class));

        Float actualResult = ReflectionHelpers.callInstanceMethod(reportIndicatorDao, "executeQueryAndReturnCount"
                , ReflectionHelpers.ClassParameter.from(String.class, query)
                , ReflectionHelpers.ClassParameter.from(SQLiteDatabase.class, database));

        Assert.assertEquals(67F, actualResult, 0);
    }

    public void getReportEventDatesShouldReturnCurrentDateWhenNoDatesRetrievedFromEventTable() {
        Date timeNow = Calendar.getInstance().getTime();
        SQLiteDatabase database = Mockito.mock(SQLiteDatabase.class);

        Mockito.doReturn(new ArrayList<HashMap<String, String>>())
                .when(dailyIndicatorCountRepository)
                .rawQuery(Mockito.any(SQLiteDatabase.class), Mockito.anyString());

        LinkedHashMap<String, Date> reportEventDates = reportIndicatorDao.getReportEventDates(timeNow, null, database);

        Assert.assertEquals(1, reportEventDates.size());
    }

    @Test
    public void saveTalliesExecutesUniqueQueries() {
        SQLiteDatabase database = Mockito.mock(SQLiteDatabase.class);
        Map<String, IndicatorQuery> indicatorQueries = new HashMap<>();
        Map.Entry<String, Date> dates = new Map.Entry<String, Date>() {
            Date date = new Date();

            @Override
            public String getKey() {
                return "12345";
            }

            @Override
            public Date getValue() {
                return date;
            }

            @Override
            public Date setValue(Date value) {
                return date = value;
            }
        };

        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"count(*)"});
        matrixCursor.addRow(new Object[]{67F});
        Mockito.doReturn(matrixCursor).when(database).rawQuery(Mockito.anyString(), Mockito.isNull(String[].class));

        IndicatorQuery query = new IndicatorQuery();
        query.setMultiResult(false);
        query.setIndicatorCode("1234");
        query.setQuery("select count(*) from ec_child");
        indicatorQueries.put("12345", query);

        reportIndicatorDao.saveTallies(indicatorQueries, dates, database);

        Mockito.verify(dailyIndicatorCountRepository).add(Mockito.any(CompositeIndicatorTally.class));

    }
}