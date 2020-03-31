package org.smartregister.reporting.util;

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

import java.util.ArrayList;


/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2019-08-15
 */

@RunWith(MockitoJUnitRunner.class)
public class UtilsTest {

    @Mock
    private SQLiteDatabase database;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void performQueryShouldReturnArrayListWithResultsWhenGivenValidQuery() {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"id", "first_name", "last_name", "id_number", "credit_score"});
        matrixCursor.addRow(new Object[]{1, "Adam", "Doe", "909092323", 89.56F});
        matrixCursor.addRow(new Object[]{1, "Jane", "Doe", "929300000", 3.2F});

        Mockito.doReturn(matrixCursor)
                .when(database)
                .rawQuery(Mockito.anyString(), Mockito.isNull(String[].class));

        ArrayList<Object[]> actualResults = ReportingUtils.performQuery(database, "SELECT * FROM persons");

        Assert.assertEquals(3, actualResults.size());
        Assert.assertEquals(5, actualResults.get(1).length);
        Assert.assertEquals(3.2F, (Float) actualResults.get(2)[4], 0);
    }
}