package org.smartregister.reporting.repository;

import android.content.ContentValues;

import net.sqlcipher.MatrixCursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.hamcrest.collection.IsArrayWithSize;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.hamcrest.MockitoHamcrest;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.reporting.BaseUnitTest;
import org.smartregister.reporting.TestApplication;
import org.smartregister.reporting.domain.IndicatorQuery;
import org.smartregister.reporting.util.Constants;
import org.smartregister.repository.Repository;
import org.smartregister.view.activity.DrishtiApplication;

public class IndicatorQueryRepositoryTest extends BaseUnitTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Repository repository;

    @Mock
    private DrishtiApplication application;

    @Mock
    private SQLiteDatabase sqLiteDatabase;

    private IndicatorQueryRepository indicatorQueryRepositorySpy;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Mockito.when(application.getRepository()).thenReturn(repository);
        TestApplication.setInstance(application);

        indicatorQueryRepositorySpy = Mockito.spy(new IndicatorQueryRepository());
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
                , MockitoHamcrest.argThat(IsArrayWithSize.<String>arrayWithSize(7))
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

        MatrixCursor matrixCursor2 = new MatrixCursor(new String[]{"name"}, 1);
        matrixCursor2.addRow(new Object[]{"indicator_queries"});

        MatrixCursor matrixCursor3 = new MatrixCursor(new String[]{"name"}, 1);
        matrixCursor3.addRow(new Object[]{"indicator_queries"});

        Mockito.doReturn(matrixCursor, matrixCursor2, matrixCursor3)
                .when(sqLiteDatabase)
                .rawQuery(
                        ArgumentMatchers.eq("SELECT name FROM sqlite_master WHERE type='table' AND name='indicator_queries'")
                        , Mockito.nullable(String[].class));

        MatrixCursor tableColumns = new MatrixCursor(new String[]{"name"});
        MatrixCursor tableColumns2 = new MatrixCursor(new String[]{"name"});
        MatrixCursor tableColumns3 = new MatrixCursor(new String[]{"name"});
        Mockito.doReturn(tableColumns, tableColumns2, tableColumns3)
                .when(sqLiteDatabase)
                .rawQuery(ArgumentMatchers.eq("PRAGMA table_info(indicator_queries)")
                        , Mockito.nullable(String[].class));

        IndicatorQueryRepository.performMigrations(sqLiteDatabase);
        Mockito.verify(sqLiteDatabase, Mockito.times(3))
                .execSQL(Mockito.eq("PRAGMA foreign_keys=off"));

        Mockito.verify(sqLiteDatabase, Mockito.times(3))
                .endTransaction();

        Mockito.verify(sqLiteDatabase).execSQL(Mockito.contains("ADD COLUMN grouping TEXT"));
        Mockito.verify(sqLiteDatabase).execSQL(Mockito.contains("ADD COLUMN expected_indicators TEXT"));
    }

    @Test
    public void createContentValuesShouldReturnContentValuesWithAllProperties() {
        String query = "SELECT count(*) FROM ec_client";
        IndicatorQuery indicatorQuery = new IndicatorQuery(9L, "CHN_01", query, 2, false, null, "opd");
        ContentValues contentValues = indicatorQueryRepositorySpy.createContentValues(indicatorQuery);

        Assert.assertEquals(9L, contentValues.getAsLong(Constants.IndicatorQueryRepository.ID), 0L);
        Assert.assertEquals("CHN_01", contentValues.getAsString(Constants.IndicatorQueryRepository.INDICATOR_CODE));
        Assert.assertEquals(query, contentValues.getAsString(Constants.IndicatorQueryRepository.QUERY));
        Assert.assertEquals(2, (int) contentValues.getAsInteger(Constants.IndicatorQueryRepository.DB_VERSION));
        Assert.assertFalse(contentValues.getAsBoolean(Constants.IndicatorQueryRepository.INDICATOR_QUERY_IS_MULTI_RESULT));
        Assert.assertNull(contentValues.getAsBoolean(Constants.IndicatorQueryRepository.INDICATOR_QUERY_EXPECTED_INDICATORS));
        Assert.assertEquals("opd", contentValues.getAsString(Constants.IndicatorQueryRepository.INDICATOR_GROUPING));
    }
}
