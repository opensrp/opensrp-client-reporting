package org.smartregister.reporting.repository;

import android.content.ContentValues;

import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.reporting.BaseUnitTest;
import org.smartregister.reporting.model.IndicatorTally;
import org.smartregister.repository.Repository;

public class DailyIndicatorCountRepositoryTest extends BaseUnitTest {

    @Mock
    private SQLiteDatabase sqLiteDatabase;

    @Mock
    private Repository repository;

    @InjectMocks
    private DailyIndicatorCountRepository dailyIndicatorCountRepository;

    private DailyIndicatorCountRepository dailyIndicatorCountRepositorySpy;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dailyIndicatorCountRepositorySpy = Mockito.spy(dailyIndicatorCountRepository);
    }

    @Test
    public void addIndicatorTallyInvokesWritableDBInsert() {
        IndicatorTally indicatorTally = Mockito.mock(IndicatorTally.class);
        Mockito.when(dailyIndicatorCountRepositorySpy.getWritableDatabase()).thenReturn(sqLiteDatabase);
        dailyIndicatorCountRepositorySpy.add(indicatorTally);
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
