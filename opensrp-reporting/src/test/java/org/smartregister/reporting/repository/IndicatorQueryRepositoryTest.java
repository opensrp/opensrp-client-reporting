package org.smartregister.reporting.repository;

import android.content.ContentValues;

import net.sqlcipher.MatrixCursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.hamcrest.collection.IsArrayWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.hamcrest.MockitoHamcrest;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.reporting.domain.IndicatorQuery;
import org.smartregister.reporting.util.Constants;
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

    @Test
    public void findQueryByIndicatorCodeInvokeDbWithCorrectParams() {
        String indicatorCode = "SN_009";
        Mockito.when(indicatorQueryRepositorySpy.getReadableDatabase()).thenReturn(sqLiteDatabase);

        indicatorQueryRepositorySpy.findQueryByIndicatorCode(indicatorCode);

        Mockito.verify(sqLiteDatabase, Mockito.times(1)).query(
                ArgumentMatchers.eq(Constants.IndicatorQueryRepository.INDICATOR_QUERY_TABLE)
                , MockitoHamcrest.argThat(IsArrayWithSize.<String>arrayWithSize(5))
                , ArgumentMatchers.eq(Constants.IndicatorQueryRepository.QUERY + " = ?")
                , MockitoHamcrest.argThat(IsArrayWithSize.<String>arrayWithSize(1))
                , ArgumentMatchers.isNull(String.class)
                , ArgumentMatchers.isNull(String.class)
                , ArgumentMatchers.isNull(String.class)
                , ArgumentMatchers.isNull(String.class));
    }

    @Test
    public void performMigrationsShouldInvokeWriteMethods() {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"name"}, 1);
        matrixCursor.addRow(new Object[]{"indicator_queries"});

        Mockito.doReturn(matrixCursor)
                .when(sqLiteDatabase)
                .rawQuery(
                        ArgumentMatchers.eq("SELECT name FROM sqlite_master WHERE type='table' AND name='indicator_queries'")
                        , Mockito.nullable(String[].class));

        Mockito.doReturn(new MatrixCursor(new String[]{"name"}))
                .when(sqLiteDatabase)
                .rawQuery(ArgumentMatchers.eq("PRAGMA table_info(indicator_queries)")
                        , Mockito.nullable(String[].class));

        IndicatorQueryRepository.performMigrations(sqLiteDatabase);
        Mockito.verify(sqLiteDatabase, Mockito.times(1))
                .execSQL(Mockito.eq("PRAGMA foreign_keys=off"));

        Mockito.verify(sqLiteDatabase, Mockito.times(1))
                .endTransaction();
    }
}
