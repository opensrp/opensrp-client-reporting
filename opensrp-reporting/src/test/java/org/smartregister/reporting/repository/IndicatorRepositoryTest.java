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
import org.smartregister.reporting.model.ReportIndicator;
import org.smartregister.repository.Repository;


public class IndicatorRepositoryTest extends BaseUnitTest {

    @Mock
    private SQLiteDatabase sqLiteDatabase;

    @Mock
    private Repository repository;

    @InjectMocks
    private IndicatorRepository indicatorRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addReportIndicatorInvokesWritableDBInsert() {
        ReportIndicator indicator = Mockito.mock(ReportIndicator.class);
        IndicatorRepository indicatorRepositorySpy = Mockito.spy(indicatorRepository);
        Mockito.when(indicatorRepositorySpy.getWritableDatabase()).thenReturn(sqLiteDatabase);
        indicatorRepositorySpy.add(indicator);
        Mockito.verify(sqLiteDatabase, Mockito.times(1)).insert(ArgumentMatchers.anyString(), ArgumentMatchers.isNull(String.class), ArgumentMatchers.any(ContentValues.class));
    }

}
