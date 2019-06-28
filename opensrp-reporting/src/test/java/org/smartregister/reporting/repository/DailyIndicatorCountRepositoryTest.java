package org.smartregister.reporting.repository;

import android.content.ContentValues;

import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.repository.Repository;

import java.text.SimpleDateFormat;
import java.util.Locale;

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
        IndicatorTally indicatorTally = Mockito.mock(IndicatorTally.class);
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
        Mockito.verify(sqLiteDatabase, Mockito.times(1)).query(ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class),
                ArgumentMatchers.isNull(String.class), ArgumentMatchers.isNull(String[].class), ArgumentMatchers.isNull(String.class), ArgumentMatchers.isNull(String.class), ArgumentMatchers.isNull(String.class), ArgumentMatchers.isNull(String.class));
    }
}
