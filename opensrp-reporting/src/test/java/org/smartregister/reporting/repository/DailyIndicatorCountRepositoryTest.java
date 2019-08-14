package org.smartregister.reporting.repository;

import android.content.ContentValues;

import net.sqlcipher.Cursor;
import net.sqlcipher.MatrixCursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.hamcrest.collection.IsArrayWithSize;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.hamcrest.MockitoHamcrest;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.domain.CompositeIndicatorTally;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.util.Constants;
import org.smartregister.repository.Repository;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ReportingLibrary.class)
public class DailyIndicatorCountRepositoryTest {

    @Mock
    private SQLiteDatabase sqLiteDatabase;

    @Mock
    private Repository repository;

    @Mock
    private ReportingLibrary reportingLibraryInstance;

    private DailyIndicatorCountRepository dailyIndicatorCountRepositorySpy;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dailyIndicatorCountRepositorySpy = Mockito.spy(new DailyIndicatorCountRepository(repository));
    }

    @Test
    public void addIndicatorTallyInvokesWritableDBInsert() throws Exception {
        String dateFormat = "yyyyMMdd";
        CompositeIndicatorTally indicatorTally = Mockito.mock(CompositeIndicatorTally.class);
        PowerMockito.mockStatic(ReportingLibrary.class);
        PowerMockito.when(ReportingLibrary.getInstance()).thenReturn(reportingLibraryInstance);
        PowerMockito.when(reportingLibraryInstance.getDateFormat()).thenReturn(dateFormat);
        Mockito.when(dailyIndicatorCountRepositorySpy.getWritableDatabase()).thenReturn(sqLiteDatabase);
        Mockito.when(indicatorTally.getCreatedAt()).thenReturn(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).parse("2019-04-13 12:19:37"));
        dailyIndicatorCountRepositorySpy.add(indicatorTally);
        Mockito.verify(sqLiteDatabase, Mockito.times(1)).delete(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class));
        Mockito.verify(sqLiteDatabase, Mockito.times(1)).insert(ArgumentMatchers.anyString(), ArgumentMatchers.isNull(String.class), ArgumentMatchers.any(ContentValues.class));
    }

    @Test
    public void getAllDailyTalliesInvokesReadableDBQuery() {
        Mockito.when(dailyIndicatorCountRepositorySpy.getReadableDatabase()).thenReturn(sqLiteDatabase);
        dailyIndicatorCountRepositorySpy.getAllDailyTallies();
        Mockito.verify(sqLiteDatabase, Mockito.times(1)).rawQuery(ArgumentMatchers.anyString(), ArgumentMatchers.isNull(String[].class));
    }

    @Test
    public void getDailyTalliesShouldInvokeQueryAndReturnEmptyHashMap() {
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
    public void processCursorRowShouldReturnValueSetWhenCursorIsValueSetRow() {
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
}
