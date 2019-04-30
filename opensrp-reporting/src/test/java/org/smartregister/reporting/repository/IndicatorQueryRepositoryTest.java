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
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.reporting.domain.IndicatorQuery;
import org.smartregister.repository.Repository;

@RunWith(MockitoJUnitRunner.class)
public class IndicatorQueryRepositoryTest {
    @Mock
    private Repository repository;

    @Mock
    private SQLiteDatabase sqLiteDatabase;

    private IndicatorQueryRepository indicatorQueryRepositorySpy;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        indicatorQueryRepositorySpy = Mockito.spy(new IndicatorQueryRepository(repository));
    }

    @Test
    public void addIndicatorQueryInvokesWritableDBInsert() {
        IndicatorQuery indicatorQuery = Mockito.mock(IndicatorQuery.class);

        Mockito.when(indicatorQueryRepositorySpy.getWritableDatabase()).thenReturn(sqLiteDatabase);
        indicatorQueryRepositorySpy.add(indicatorQuery);
        Mockito.verify(sqLiteDatabase, Mockito.times(1)).insert(ArgumentMatchers.anyString(), ArgumentMatchers.isNull(String.class), ArgumentMatchers.any(ContentValues.class));
    }

    @Test
    public void getAllIndicatorQueriesInvokesReadableDBQuery() {
        Mockito.when(indicatorQueryRepositorySpy.getReadableDatabase()).thenReturn(sqLiteDatabase);
        indicatorQueryRepositorySpy.getAllIndicatorQueries();
        Mockito.verify(sqLiteDatabase, Mockito.times(1)).query(ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class),
                ArgumentMatchers.isNull(String.class), ArgumentMatchers.isNull(String[].class), ArgumentMatchers.isNull(String.class), ArgumentMatchers.isNull(String.class), ArgumentMatchers.isNull(String.class), ArgumentMatchers.isNull(String.class));
    }
}
