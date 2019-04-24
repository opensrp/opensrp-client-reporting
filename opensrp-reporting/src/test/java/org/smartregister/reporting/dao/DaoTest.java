package org.smartregister.reporting.dao;

import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.smartregister.Context;
import org.smartregister.reporting.BaseUnitTest;
import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.model.IndicatorQuery;
import org.smartregister.reporting.model.ReportIndicator;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@PrepareForTest({ReportingLibrary.class, ReportIndicatorDaoImpl.class})
public class DaoTest extends BaseUnitTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();


    @Mock
    private Context context;

    @Mock
    private ReportingLibrary reportingLibrary;

    @Mock
    private Repository repository;

    @Mock
    private IndicatorQueryRepository indicatorQueryRepository;

    @Mock
    private IndicatorRepository indicatorRepository;

    @Mock
    private DailyIndicatorCountRepository dailyIndicatorCountRepository;

    @Mock
    SQLiteDatabase sqLiteDatabase;

    @Mock
    AllSharedPreferences sharedPreferences;

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

    @Test
    public void generateDailyIndicatorTalliesSetsLastProcessedDatePreference() throws Exception {
        // Test data
        String lastProcessedDate = "2019-01-01 00:00";
        ArrayList<HashMap<String, String>> reportEventDates = new ArrayList<>();
        HashMap<String, String> dateMap = new HashMap<>();
        dateMap.put("eventDate", "Mon Apr 15 03:00:00 GMT+03:00 2019");
        dateMap.put("updatedAt", "2019-04-16 12:19:37");
        reportEventDates.add(dateMap);

        Map<String, String> indicatorQueries = new HashMap<>();
        indicatorQueries.put("INDI-100", "select count(*) from table");

        PowerMockito.mockStatic(ReportingLibrary.class);
        reportIndicatorDaoSpy = PowerMockito.spy(reportIndicatorDao);

        PowerMockito.when(ReportingLibrary.getInstance()).thenReturn(reportingLibrary);
        Mockito.when(reportingLibrary.getRepository()).thenReturn(repository);
        Mockito.when(reportingLibrary.getContext()).thenReturn(context);
        Mockito.when(repository.getWritableDatabase()).thenReturn(sqLiteDatabase);
        PowerMockito.doReturn(reportEventDates).when(reportIndicatorDaoSpy, "getReportEventDates", ArgumentMatchers.anyString(), ArgumentMatchers.any(SQLiteDatabase.class));
        Mockito.when(indicatorQueryRepository.getAllIndicatorQueries()).thenReturn(indicatorQueries);
        Mockito.when(context.allSharedPreferences()).thenReturn(sharedPreferences);
        reportIndicatorDaoSpy.generateDailyIndicatorTallies(lastProcessedDate);
        Mockito.verify(sharedPreferences, Mockito.times(1)).savePreference(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

    }

}
