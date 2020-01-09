package org.smartregister.reporting.repository;

import android.content.ContentValues;

import net.sqlcipher.Cursor;
import net.sqlcipher.MatrixCursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.hamcrest.collection.IsArrayWithSize;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.hamcrest.MockitoHamcrest;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.dao.ReportIndicatorDaoImpl;
import org.smartregister.reporting.domain.CompositeIndicatorTally;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.processor.DefaultMultiResultProcessor;
import org.smartregister.reporting.processor.MultiResultProcessor;
import org.smartregister.reporting.util.Constants;
import org.smartregister.repository.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@RunWith(RobolectricTestRunner.class)
public class DailyIndicatorCountRepositoryTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SQLiteDatabase sqLiteDatabase;

    @Mock
    private Repository repository;

    private ReportingLibrary reportingLibraryInstance;

    private DailyIndicatorCountRepository dailyIndicatorCountRepositorySpy;

    @Before
    public void setUp() {

        ReportingLibrary.init(Mockito.mock(Context.class), Mockito.mock(Repository.class), Mockito.mock(CommonFtsObject.class), 1, 1);
        reportingLibraryInstance = Mockito.spy(ReportingLibrary.getInstance());
        ReflectionHelpers.setStaticField(ReportingLibrary.class, "instance", reportingLibraryInstance);

        dailyIndicatorCountRepositorySpy = Mockito.spy(new DailyIndicatorCountRepository(repository));
        Mockito.when(dailyIndicatorCountRepositorySpy.getWritableDatabase()).thenReturn(sqLiteDatabase);
        Mockito.when(dailyIndicatorCountRepositorySpy.getReadableDatabase()).thenReturn(sqLiteDatabase);
    }

    @After
    public void tearDown() throws Exception {
        ReflectionHelpers.setStaticField(ReportingLibrary.class, "instance", null);
    }

    @Test
    public void addIndicatorTallyInvokesWritableDBInsert() throws Exception {
        String dateFormat = "yyyyMMdd";
        CompositeIndicatorTally indicatorTally = Mockito.mock(CompositeIndicatorTally.class);

        Mockito.when(reportingLibraryInstance.getDateFormat()).thenReturn(dateFormat);
        Mockito.when(indicatorTally.getCreatedAt()).thenReturn(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).parse("2019-04-13 12:19:37"));

        dailyIndicatorCountRepositorySpy.add(indicatorTally);

        Mockito.verify(sqLiteDatabase, Mockito.times(1)).delete(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class));
        Mockito.verify(sqLiteDatabase, Mockito.times(1)).insert(ArgumentMatchers.anyString(), ArgumentMatchers.isNull(String.class), ArgumentMatchers.any(ContentValues.class));
    }

    @Test
    public void getAllDailyTalliesInvokesReadableDBQuery() {
        dailyIndicatorCountRepositorySpy.getAllDailyTallies();
        Mockito.verify(sqLiteDatabase, Mockito.times(1))
                .rawQuery(ArgumentMatchers.anyString(), ArgumentMatchers.isNull(String[].class));
    }

    @Test
    public void getAllDailyTalliesShouldProcessAndReturnIndicatorTallies() {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"_id", "indicator_code", "indicator_value"
                , "indicator_value_set", "indicator_is_value_set", "day", "expected_indicators"}, 1);
        matrixCursor.addRow(new Object[]{1, "ME_Child_HIV_Status_Under2_Gender", null, "[[\"hiv_status\",\"gender\",\"counter\"]]", 1, "2017-03-01", "[\"ME_Child_HIV_Status_Under2_Gender_HIV Exposed_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Exposed_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Negative_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Negative_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Positive_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Positive_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Male\"]"});
        matrixCursor.addRow(new Object[]{1, "ME_Child_HIV_Status_Under2_Gender", null, "[[\"hiv_status\",\"gender\",\"counter\"],[\"HIV Unknown\",\"Male\",1]]", 1, "2017-03-02", "[\"ME_Child_HIV_Status_Under2_Gender_HIV Exposed_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Exposed_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Negative_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Negative_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Positive_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Positive_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Male\"]"});
        matrixCursor.addRow(new Object[]{1, "ME_Child_HIV_Status_Under2_Gender", null, "[[\"hiv_status\",\"gender\",\"counter\"],[\"HIV Exposed\",\"Male\",3]]", 1, "2017-03-03", "[\"ME_Child_HIV_Status_Under2_Gender_HIV Exposed_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Exposed_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Negative_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Negative_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Positive_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Positive_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Male\"]"});
        matrixCursor.addRow(new Object[]{1, "ME_Child_Total", null, 23, 0, "2017-03-03", "[\"ME_Child_HIV_Status_Under2_Gender_HIV Exposed_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Exposed_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Negative_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Negative_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Positive_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Positive_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Male\"]"});

        ReportingLibrary.init(Mockito.mock(Context.class), Mockito.mock(Repository.class), Mockito.mock(CommonFtsObject.class), 1, 1);
        ReportingLibrary reportingLibrarySpy = Mockito.spy(ReportingLibrary.getInstance());
        ReflectionHelpers.setStaticField(ReportingLibrary.class, "instance", reportingLibrarySpy);
        ReportingLibrary.getInstance().setDefaultMultiResultProcessor(new DefaultMultiResultProcessor());

        Mockito.doReturn(matrixCursor)
                .when(sqLiteDatabase)
                .rawQuery(
                        ArgumentMatchers.eq("SELECT indicator_daily_tally._id, indicator_daily_tally.indicator_code, indicator_daily_tally.indicator_value, indicator_daily_tally.indicator_value_set, indicator_daily_tally.indicator_is_value_set, indicator_daily_tally.day, indicator_queries.expected_indicators FROM indicator_daily_tally INNER JOIN indicator_queries ON indicator_daily_tally.indicator_code = indicator_queries.indicator_code")
                        , Mockito.nullable(String[].class));

        List<Map<String, IndicatorTally>> dailyTallies =  dailyIndicatorCountRepositorySpy.getAllDailyTallies();
        Assert.assertEquals(4, dailyTallies.size());

        HashMap<String, List<IndicatorTally>> summaryTallies = new HashMap<>();

        for (Map<String, IndicatorTally> indicatorTallyMap: dailyTallies) {
            for (String indicatorCode: indicatorTallyMap.keySet()) {
                List<IndicatorTally> indicatorTallyList = summaryTallies.get(indicatorCode);
                if (indicatorTallyList == null) {
                    indicatorTallyList = new ArrayList<>();
                    summaryTallies.put(indicatorCode, indicatorTallyList);
                }

                indicatorTallyList.add(indicatorTallyMap.get(indicatorCode));
            }
        }

        Assert.assertEquals(3, summaryTallies.get("ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Male").size());
        Assert.assertEquals(1, summaryTallies.get("ME_Child_Total").size());
    }

    @Test
    public void getDailyTalliesWhenGivenCurrentDateShouldInvokeQueryAndReturnEmptyHashMap() {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{Constants.DailyIndicatorCountRepository.ID
                , Constants.DailyIndicatorCountRepository.INDICATOR_CODE
                , Constants.DailyIndicatorCountRepository.INDICATOR_VALUE
                , Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET
                , Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET_FLAG
                , Constants.DailyIndicatorCountRepository.DAY}
                , 0);

        Mockito.doReturn(sqLiteDatabase)
                .when(dailyIndicatorCountRepositorySpy)
                .getReadableDatabase();

        Mockito.doReturn(matrixCursor)
                .when(sqLiteDatabase)
                .query(
                        ArgumentMatchers.eq(Constants.DailyIndicatorCountRepository.INDICATOR_DAILY_TALLY_TABLE)
                        , MockitoHamcrest.argThat(IsArrayWithSize.<String>arrayWithSize(6))
                        , ArgumentMatchers.eq(Constants.DailyIndicatorCountRepository.DAY + " = ? AND " + " < ?")
                        , MockitoHamcrest.argThat(IsArrayWithSize.<String>arrayWithSize(2))
                        , ArgumentMatchers.isNull(String.class)
                        , ArgumentMatchers.isNull(String.class)
                        , ArgumentMatchers.isNull(String.class)
                        , ArgumentMatchers.isNull(String.class));

        Map<String, IndicatorTally> tallyMap = dailyIndicatorCountRepositorySpy.getDailyTallies(new Date(System.currentTimeMillis()));

        Mockito.verify(sqLiteDatabase, Mockito.times(1))
                .rawQuery(ArgumentMatchers.anyString()
                        , MockitoHamcrest.argThat(IsArrayWithSize.<String>arrayWithSize(2)));

        Assert.assertEquals(0, tallyMap.size());
    }

    @Test
    public void performMigrationsShouldInvokeWriteMethods() {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"name"}, 1);
        matrixCursor.addRow(new Object[]{"indicator_daily_tally"});

        Mockito.doReturn(matrixCursor)
                .when(sqLiteDatabase)
                .rawQuery(
                        ArgumentMatchers.eq("SELECT name FROM sqlite_master WHERE type='table' AND name='indicator_daily_tally'")
                        , Mockito.nullable(String[].class));

        Mockito.doReturn(new MatrixCursor(new String[]{"name"}))
                .when(sqLiteDatabase)
                .rawQuery(ArgumentMatchers.eq("PRAGMA table_info(indicator_daily_tally)")
                        , Mockito.nullable(String[].class));

        dailyIndicatorCountRepositorySpy.performMigrations(sqLiteDatabase);
        Mockito.verify(sqLiteDatabase, Mockito.times(1))
                .execSQL(Mockito.eq("PRAGMA foreign_keys=off"));

        Mockito.verify(sqLiteDatabase, Mockito.times(1))
                .endTransaction();
    }

    @Test
    public void processCursorRowShouldReturnValueSetWhenCursorIsIndicatorRowWithValueSet() {
        String[] columns = {Constants.DailyIndicatorCountRepository.ID
                , Constants.DailyIndicatorCountRepository.INDICATOR_CODE
                , Constants.DailyIndicatorCountRepository.INDICATOR_VALUE
                , Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET
                , Constants.DailyIndicatorCountRepository.INDICATOR_VALUE_SET_FLAG
                , Constants.DailyIndicatorCountRepository.DAY};

        long id = 89;
        String indicatorCode = "SND_100";
        String valueSet = "[['male', 90], ['female', 100]";

        MatrixCursor matrixCursor = new MatrixCursor(columns, 1);
        matrixCursor.addRow(new Object[]{id, indicatorCode, 0L, valueSet, 1, System.currentTimeMillis()});

        matrixCursor.moveToNext();

        CompositeIndicatorTally indicatorTally = ReflectionHelpers.callInstanceMethod(dailyIndicatorCountRepositorySpy, "processCursorRow"
                , ReflectionHelpers.ClassParameter.from(Cursor.class, matrixCursor));


        Assert.assertTrue(indicatorTally.isValueSet());
        Assert.assertEquals(valueSet, indicatorTally.getValueSet());
        Assert.assertEquals(indicatorCode, indicatorTally.getIndicatorCode());
        Assert.assertEquals(id, indicatorTally.getId().longValue());
    }

    @Test
    public void extractOnlyRequiredIndicatorTalliesAndProvideDefaultShouldReturnZeroValuesForUnlocatedIndicatorsWhenGivenExpectedIndicatorsAndUnwantedIndicators() {
        java.util.Date timeNow = Calendar.getInstance().getTime();

        List<IndicatorTally> indicatorTally = new ArrayList<>();
        indicatorTally.add(new IndicatorTally(0L, 54, "ISN_OPV", timeNow));
        indicatorTally.add(new IndicatorTally(0L, 6, "ISN_HEPB", timeNow));
        indicatorTally.add(new IndicatorTally(0L, 9, "ISN_PCV", timeNow));
        indicatorTally.add(new IndicatorTally(0L, 5, "ISN_ROTA", timeNow));

        ArrayList<String> expectedIndicators = new ArrayList<>();
        expectedIndicators.add("ISN_BCG");
        expectedIndicators.add("ISN_OPV");
        expectedIndicators.add("ISN_PENTA");

        CompositeIndicatorTally compositeIndicatorTally = new CompositeIndicatorTally();
        compositeIndicatorTally.setValueSetFlag(true);
        compositeIndicatorTally.setExpectedIndicators(expectedIndicators);
        compositeIndicatorTally.setIndicatorCode("ISN");
        compositeIndicatorTally.setCreatedAt(timeNow);

        List<IndicatorTally> finalTallies =  dailyIndicatorCountRepositorySpy.extractOnlyRequiredIndicatorTalliesAndProvideDefault(compositeIndicatorTally, indicatorTally);

        Assert.assertEquals(3, finalTallies.size());
        Assert.assertEquals("ISN_BCG", finalTallies.get(0).getIndicatorCode());
        Assert.assertEquals("ISN_OPV", finalTallies.get(1).getIndicatorCode());
        Assert.assertEquals("ISN_PENTA", finalTallies.get(2).getIndicatorCode());

        Assert.assertEquals(0, finalTallies.get(0).getCount());
        Assert.assertEquals(54, finalTallies.get(1).getCount());
        Assert.assertEquals(0, finalTallies.get(2).getCount());
    }

    @Test
    public void extractOnlyRequiredIndicatorTalliesAndProvideDefaultShouldReturnZeroValuesForAllUnlocatedIndicatorsWhenGivenNull() {
        java.util.Date timeNow = Calendar.getInstance().getTime();

        ArrayList<String> expectedIndicators = new ArrayList<>();
        expectedIndicators.add("ISN_BCG");
        expectedIndicators.add("ISN_OPV");
        expectedIndicators.add("ISN_PENTA");

        CompositeIndicatorTally compositeIndicatorTally = new CompositeIndicatorTally();
        compositeIndicatorTally.setValueSetFlag(true);
        compositeIndicatorTally.setExpectedIndicators(expectedIndicators);
        compositeIndicatorTally.setIndicatorCode("ISN");
        compositeIndicatorTally.setCreatedAt(timeNow);

        List<IndicatorTally> finalTallies =  dailyIndicatorCountRepositorySpy.extractOnlyRequiredIndicatorTalliesAndProvideDefault(compositeIndicatorTally, null);

        Assert.assertEquals(3, finalTallies.size());
        Assert.assertEquals("ISN_BCG", finalTallies.get(0).getIndicatorCode());
        Assert.assertEquals("ISN_OPV", finalTallies.get(1).getIndicatorCode());
        Assert.assertEquals("ISN_PENTA", finalTallies.get(2).getIndicatorCode());

        Assert.assertEquals(0, finalTallies.get(0).getCount());
        Assert.assertEquals(0, finalTallies.get(1).getCount());
        Assert.assertEquals(0, finalTallies.get(2).getCount());
    }

    @Test
    public void extractIndicatorTalliesFromMultiResultWhenGivenComposeIndicatorTallyAndMultiProcessorsShouldAddSingleIndicatorTalliesToMapWhenGivenCompositeIndicatorTally() {
        DefaultMultiResultProcessor defaultMultiResultProcessor = new DefaultMultiResultProcessor();
        ArrayList<MultiResultProcessor> multiResultProcessors = new ArrayList<>();
        CompositeIndicatorTally compositeIndicatorTally = new CompositeIndicatorTally();
        compositeIndicatorTally.setIndicatorCode("ISN");
        compositeIndicatorTally.setCreatedAt(Calendar.getInstance().getTime());
        compositeIndicatorTally.setValueSetFlag(true);
        compositeIndicatorTally.setValueSet("[['gender', 'count'], ['Male', 98.56], ['Female', 89]]");

        HashMap<String, IndicatorTally> tallyMap = new HashMap<>();

        ReflectionHelpers.callInstanceMethod(dailyIndicatorCountRepositorySpy, "extractIndicatorTalliesFromMultiResult"
                , ReflectionHelpers.ClassParameter.from(Map.class, tallyMap)
                , ReflectionHelpers.ClassParameter.from(MultiResultProcessor.class, defaultMultiResultProcessor)
                , ReflectionHelpers.ClassParameter.from(ArrayList.class, multiResultProcessors)
                , ReflectionHelpers.ClassParameter.from(CompositeIndicatorTally.class, compositeIndicatorTally));

        Assert.assertEquals(2, tallyMap.size());
        Assert.assertTrue(tallyMap.containsKey("ISN_Female"));
        Assert.assertTrue(tallyMap.containsKey("ISN_Male"));
    }

    @Test
    public void findTalliesInMonthWhenGivenValidMonthDateInYYYYMMShouldReturn10Tallies() throws ParseException {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"_id", "indicator_code", "indicator_value"
                , "indicator_value_set", "indicator_is_value_set", "day", "expected_indicators"}, 1);
        matrixCursor.addRow(new Object[]{1, "ME_Child_HIV_Status_Under2_Gender", null, "[[\"hiv_status\",\"gender\",\"counter\"]]", 1, "2017-03-01", "[\"ME_Child_HIV_Status_Under2_Gender_HIV Exposed_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Exposed_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Negative_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Negative_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Positive_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Positive_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Male\"]"});
        matrixCursor.addRow(new Object[]{1, "ME_Child_HIV_Status_Under2_Gender", null, "[[\"hiv_status\",\"gender\",\"counter\"],[\"HIV Unknown\",\"Male\",1]]", 1, "2017-03-02", "[\"ME_Child_HIV_Status_Under2_Gender_HIV Exposed_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Exposed_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Negative_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Negative_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Positive_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Positive_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Male\"]"});
        matrixCursor.addRow(new Object[]{1, "ME_Child_HIV_Status_Under2_Gender", null, "[[\"hiv_status\",\"gender\",\"counter\"],[\"HIV Exposed\",\"Male\",3]]", 1, "2017-03-03", "[\"ME_Child_HIV_Status_Under2_Gender_HIV Exposed_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Exposed_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Negative_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Negative_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Positive_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Positive_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Male\"]"});

        ArgumentCaptor<String[]> argumentCaptor = ArgumentCaptor.forClass(String[].class);
        ReportingLibrary.getInstance().setDefaultMultiResultProcessor(new DefaultMultiResultProcessor());

        Mockito.doReturn(matrixCursor)
                .when(sqLiteDatabase)
                .rawQuery(
                        ArgumentMatchers.eq("SELECT indicator_daily_tally._id, indicator_daily_tally.indicator_code, indicator_daily_tally.indicator_value, indicator_daily_tally.indicator_value_set, indicator_daily_tally.indicator_is_value_set, indicator_daily_tally.day, indicator_queries.expected_indicators FROM indicator_daily_tally INNER JOIN indicator_queries ON indicator_daily_tally.indicator_code = indicator_queries.indicator_code WHERE indicator_daily_tally.day >= ? AND indicator_daily_tally.day <= ?")
                        , argumentCaptor.capture());

        Map<String, List<IndicatorTally>> talliesFromMonth = dailyIndicatorCountRepositorySpy.findTalliesInMonth(new SimpleDateFormat("yyyy-MM", Locale.ENGLISH).parse("2017-03"));

        String[] queryArgs = argumentCaptor.getValue();

        Assert.assertEquals("2017-03-01", queryArgs[0]);
        Assert.assertEquals("2017-03-31", queryArgs[1]);

        Assert.assertEquals(8, talliesFromMonth.size());

        Assert.assertTrue(talliesFromMonth.containsKey("ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Male"));
        Assert.assertTrue(talliesFromMonth.containsKey("ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Female"));
        Assert.assertTrue(talliesFromMonth.containsKey("ME_Child_HIV_Status_Under2_Gender_HIV Positive_Male"));
        Assert.assertTrue(talliesFromMonth.containsKey("ME_Child_HIV_Status_Under2_Gender_HIV Positive_Female"));
        Assert.assertTrue(talliesFromMonth.containsKey("ME_Child_HIV_Status_Under2_Gender_HIV Negative_Male"));
        Assert.assertTrue(talliesFromMonth.containsKey("ME_Child_HIV_Status_Under2_Gender_HIV Negative_Female"));
        Assert.assertTrue(talliesFromMonth.containsKey("ME_Child_HIV_Status_Under2_Gender_HIV Exposed_Male"));
        Assert.assertTrue(talliesFromMonth.containsKey("ME_Child_HIV_Status_Under2_Gender_HIV Exposed_Female"));
    }

    @Test
    public void findDaysWithIndicatorCountsWhenGivenValidDateRangeAndDateFormatShouldReturn10DatesFromDb() throws ParseException {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"day"}, 0);
        matrixCursor.addRow(new Object[]{"2017-03-01"});
        matrixCursor.addRow(new Object[]{"2017-03-03"});
        matrixCursor.addRow(new Object[]{"2017-03-05"});
        matrixCursor.addRow(new Object[]{"2017-03-06"});
        matrixCursor.addRow(new Object[]{"2017-03-07"});
        matrixCursor.addRow(new Object[]{"2017-03-08"});
        matrixCursor.addRow(new Object[]{"2017-03-09"});
        matrixCursor.addRow(new Object[]{"2017-03-10"});
        matrixCursor.addRow(new Object[]{"2017-03-11"});
        matrixCursor.addRow(new Object[]{"2017-03-12"});

        ReportingLibrary.getInstance().setDefaultMultiResultProcessor(new DefaultMultiResultProcessor());

        Mockito.doReturn(matrixCursor)
                .when(sqLiteDatabase)
                .rawQuery(
                        ArgumentMatchers.eq("SELECT DISTINCT day FROM indicator_daily_tally WHERE day >= '2017-03-01' AND day <= '2017-03-31'")
                        , Mockito.nullable(String[].class));

        SimpleDateFormat yyyMMdd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date startDate = yyyMMdd.parse("2017-03-01");
        Date endDate = yyyMMdd.parse("2017-03-31");

        ArrayList<Date> dates =  dailyIndicatorCountRepositorySpy.findDaysWithIndicatorCounts(new SimpleDateFormat(ReportIndicatorDaoImpl.DAILY_TALLY_DATE_FORMAT), startDate, endDate);

        Assert.assertEquals(10, dates.size());
        Assert.assertEquals(yyyMMdd.parse("2017-03-01").getTime(), dates.get(0).getTime());
        Assert.assertEquals(yyyMMdd.parse("2017-03-07").getTime(), dates.get(4).getTime());
        Assert.assertEquals(yyyMMdd.parse("2017-03-12").getTime(), dates.get(9).getTime());
    }

    @Test
    public void getIndicatorTalliesForDayShouldReturn8TalliesWhenGivenValidDateWithTallies() throws ParseException {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"_id", "indicator_code", "indicator_value"
                , "indicator_value_set", "indicator_is_value_set", "day", "expected_indicators"}, 1);
        matrixCursor.addRow(new Object[]{1, "ME_Child_HIV_Status_Under2_Gender", null, "[[\"hiv_status\",\"gender\",\"counter\"],[\"HIV Unknown\",\"Male\",1]]", 1, "2017-03-10", "[\"ME_Child_HIV_Status_Under2_Gender_HIV Exposed_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Exposed_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Negative_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Negative_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Positive_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Positive_Male\",\"ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Female\",\"ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Male\"]"});
        matrixCursor.addRow(new Object[]{1, "ME_Child_Total", 4, null, 0, "2017-03-10", null});

        ReportingLibrary.getInstance().setDefaultMultiResultProcessor(new DefaultMultiResultProcessor());

        Mockito.doReturn(matrixCursor)
                .when(sqLiteDatabase)
                .rawQuery(
                        ArgumentMatchers.eq("SELECT indicator_daily_tally._id, indicator_daily_tally.indicator_code, indicator_daily_tally.indicator_value, indicator_daily_tally.indicator_value_set, indicator_daily_tally.indicator_is_value_set, indicator_daily_tally.day, indicator_queries.expected_indicators FROM indicator_daily_tally INNER JOIN indicator_queries ON indicator_daily_tally.indicator_code = indicator_queries.indicator_code WHERE indicator_daily_tally.day = '2017-03-10'")
                        , ArgumentMatchers.nullable(String[].class));

        ArrayList<IndicatorTally> talliesForDay = dailyIndicatorCountRepositorySpy.getIndicatorTalliesForDay(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse("2017-03-10"));
        
        HashMap<String, IndicatorTally> indicatorTallyHashMap = new HashMap<>();
        for (IndicatorTally indicatorTally: talliesForDay) {
            indicatorTallyHashMap.put(indicatorTally.getIndicatorCode(),indicatorTally);
        }

        Set<String> indicatorCodes = indicatorTallyHashMap.keySet();

        Assert.assertTrue(indicatorCodes.contains("ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Male"));
        Assert.assertTrue(indicatorCodes.contains("ME_Child_HIV_Status_Under2_Gender_HIV Unknown_Female"));
        Assert.assertTrue(indicatorCodes.contains("ME_Child_HIV_Status_Under2_Gender_HIV Positive_Male"));
        Assert.assertTrue(indicatorCodes.contains("ME_Child_HIV_Status_Under2_Gender_HIV Positive_Female"));
        Assert.assertTrue(indicatorCodes.contains("ME_Child_HIV_Status_Under2_Gender_HIV Negative_Male"));
        Assert.assertTrue(indicatorCodes.contains("ME_Child_HIV_Status_Under2_Gender_HIV Negative_Female"));
        Assert.assertTrue(indicatorCodes.contains("ME_Child_HIV_Status_Under2_Gender_HIV Exposed_Male"));
        Assert.assertTrue(indicatorCodes.contains("ME_Child_HIV_Status_Under2_Gender_HIV Exposed_Female"));

        Assert.assertEquals(4, indicatorTallyHashMap.get("ME_Child_Total").getCount());
    }
}
