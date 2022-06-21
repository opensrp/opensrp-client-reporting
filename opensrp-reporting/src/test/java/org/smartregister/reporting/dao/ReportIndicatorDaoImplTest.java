package org.smartregister.reporting.dao;

import androidx.annotation.Nullable;

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
import org.powermock.reflect.Whitebox;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.domain.CompositeIndicatorTally;
import org.smartregister.reporting.domain.IndicatorQuery;
import org.smartregister.reporting.domain.ReportIndicator;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;
import org.smartregister.reporting.util.Constants;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.Repository;
import org.smartregister.util.AppProperties;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2019-08-14
 */

@RunWith(MockitoJUnitRunner.class)
public class ReportIndicatorDaoImplTest {
    @Mock
    private ReportIndicator reportIndicator;

    private ReportIndicatorDaoImpl reportIndicatorDao;

    @Mock
    private IndicatorRepository indicatorRepository;
    @Mock
    private DailyIndicatorCountRepository dailyIndicatorCountRepository;
    @Mock
    private IndicatorQueryRepository indicatorQueryRepository;
    @Mock
    private CoreLibrary coreLibrary;

    private AppProperties appProperties;
    @Mock
    private Context context;

    @Mock
    private Repository repository;

    @Mock
    private  AllSharedPreferences allSharedPreferences;

    @Mock
    private CommonFtsObject commonFtsObject;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        reportIndicatorDao = new ReportIndicatorDaoImpl(indicatorQueryRepository, dailyIndicatorCountRepository, indicatorRepository);
        ReflectionHelpers.setStaticField(CoreLibrary.class, "instance", coreLibrary);

        Mockito.when(coreLibrary.context()).thenReturn(context);
        appProperties = new AppProperties();
        appProperties.setProperty(Constants.ReportingConfig.SHOULD_ALLOW_ZERO_TALLIES, "true");
        Mockito.when(context.getAppProperties()).thenReturn(appProperties);
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

    @Test
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
    public void testGenerateDailyIndicatorTalliesCallsSaveTalliesEqualToNumberOfReportsMultipliedByDates() {
        reportIndicatorDao = Mockito.spy(reportIndicatorDao);

        SQLiteDatabase database = Mockito.mock(SQLiteDatabase.class);
        Repository repository = Mockito.mock(Repository.class);
        Mockito.doReturn(database).when(repository).getWritableDatabase();

        ReportingLibrary reportingLibrarySpy = Mockito.mock(ReportingLibrary.class);
        ReflectionHelpers.setStaticField(ReportingLibrary.class, "instance", reportingLibrarySpy);

        ReportingLibrary reportingLibrary = Mockito.mock(ReportingLibrary.class);
        Mockito.doReturn(repository).when(reportingLibrary).getRepository();

        Context context = Mockito.mock(Context.class);
        Mockito.doReturn(context).when(reportingLibrary).getContext();


        AllSharedPreferences allSharedPreferences = Mockito.mock(AllSharedPreferences.class);
        Mockito.doReturn(allSharedPreferences).when(context).allSharedPreferences();


        Whitebox.setInternalState(reportIndicatorDao, "reportingLibrary", reportingLibrary);

        LinkedHashMap<String, Date> dates = new LinkedHashMap<>();
        dates.put("2018-01-01", new Date());
        dates.put("2018-01-02", new Date());
        dates.put("2018-01-03", new Date());
        Mockito.doReturn(dates).when(reportIndicatorDao).getReportEventDates(Mockito.any(Date.class), Mockito.anyString(), Mockito.any(SQLiteDatabase.class));

        IndicatorQuery query = new IndicatorQuery();
        query.setQuery("select * from me");
        Map<String, IndicatorQuery> values = new HashMap<>();
        values.put("0001", query);
        values.put("0002", query);
        values.put("0003", query);

        Mockito.doReturn(values).when(indicatorQueryRepository).getAllIndicatorQueries();
        Mockito.doNothing().when(reportIndicatorDao).saveTallies(Mockito.<String, IndicatorQuery>anyMap(), Mockito.<Map.Entry<String, Date>>any(), Mockito.any(SQLiteDatabase.class),Mockito.<String>anySet());
        reportIndicatorDao.generateDailyIndicatorTallies("2019-12-11");

        Mockito.verify(reportIndicatorDao, Mockito.times(dates.size())).saveTallies(Mockito.<String, IndicatorQuery>anyMap(), Mockito.<Map.Entry<String, Date>>any(), Mockito.any(SQLiteDatabase.class), Mockito.<String>anySet());
    }

    @Test
    public void saveTalliesGeneratesTallyObject() {
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

        ReportingLibrary.init(context,repository,commonFtsObject,0,0);
        Mockito.when(context.allSharedPreferences()).thenReturn(allSharedPreferences);
        Mockito.when(allSharedPreferences.fetchPioneerUser()).thenReturn("testUser");
        Mockito.when(allSharedPreferences.fetchDefaultLocalityId(Mockito.anyString())).thenReturn("testLoc");

        IndicatorQuery query = new IndicatorQuery();
        query.setMultiResult(false);
        query.setIndicatorCode("1234");
        query.setQuery("select count(*) from ec_child");
        indicatorQueries.put("12345", query);

        Set<String> executedQueries = new HashSet<>();
        reportIndicatorDao.saveTallies(indicatorQueries, dates, database, executedQueries);

        Mockito.verify(dailyIndicatorCountRepository).add(Mockito.any(CompositeIndicatorTally.class));

    }

    @Test
    public void testCanAddReportIndicator() {
        reportIndicatorDao.addReportIndicator(reportIndicator);
        Mockito.verify(indicatorRepository).add(Mockito.any(ReportIndicator.class));
    }

    @Test
    public void testCanAddIndicatorQuery() {
        @Nullable List<String> expectedIndicators = new ArrayList<>();
        expectedIndicators.add(0, "indicator1");

        IndicatorQuery indicatorQuery = new IndicatorQuery();
        indicatorQuery.setDbVersion(2);
        indicatorQuery.setExpectedIndicators(expectedIndicators);
        indicatorQuery.setId(2L);
        indicatorQuery.setIndicatorCode("indicatorCode");

        reportIndicatorDao.addIndicatorQuery(indicatorQuery);
        Mockito.verify(indicatorQueryRepository).add(Mockito.any(IndicatorQuery.class));
    }

    @Test
    public void canGetLatestIndicatorTallies() {
        reportIndicatorDao.getLatestIndicatorTallies();
        Mockito.verify(dailyIndicatorCountRepository).getLatestIndicatorTallies();
    }
}