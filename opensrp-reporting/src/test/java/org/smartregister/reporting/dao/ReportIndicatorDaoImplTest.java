package org.smartregister.reporting.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.reporting.domain.CompositeIndicatorTally;
import org.smartregister.reporting.processor.DefaultMultiResultProcessor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2019-08-13
 */

@RunWith(MockitoJUnitRunner.class)
public class ReportIndicatorDaoImplTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void extractIndicatorTalliesShouldReturnEmptyMapIfCompositeTalliesContainsOnlyColumnName() {
        ReportIndicatorDaoImpl reportIndicatorDao = new ReportIndicatorDaoImpl();

        ArrayList<Object> tallies = new ArrayList<>();
        tallies.add(new Object[]{"gender", "counter"});

        HashMap<String, Float> actualResult = reportIndicatorDao.extractIndicatorTallies(tallies, new DefaultMultiResultProcessor(), null, new CompositeIndicatorTally(9L, 5, "ISN", new Date()));
        Assert.assertEquals(0, actualResult.size());
    }
}
