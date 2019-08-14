package org.smartregister.reporting.dao;

import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.domain.CompositeIndicatorTally;
import org.smartregister.reporting.domain.IndicatorQuery;
import org.smartregister.reporting.processor.DefaultMultiResultProcessor;
import org.smartregister.repository.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2019-08-13
 */

@RunWith(MockitoJUnitRunner.class)
public class ReportIndicatorDaoImplTest {

    private ReportIndicatorDaoImpl reportIndicatorDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        reportIndicatorDao = new ReportIndicatorDaoImpl();

        // STRONG NOTE ! This init instance works every time
        ReflectionHelpers.setStaticField(ReportingLibrary.class, "instance", null);
        ReportingLibrary.init(Mockito.mock(Context.class), Mockito.mock(Repository.class), Mockito.mock(CommonFtsObject.class), 9, 1);
    }

    @Test
    public void extractIndicatorTalliesShouldReturnEmptyMapWhenGivenMultiResultWithOnlyColumnName() {
        ArrayList<Object> tallies = new ArrayList<>();
        tallies.add(new Object[]{"gender", "counter"});

        HashMap<String, Float> actualResult = reportIndicatorDao.extractIndicatorTallies(tallies, new DefaultMultiResultProcessor(), null, new CompositeIndicatorTally(9L, 5, "ISN", new Date()));
        Assert.assertEquals(0, actualResult.size());
    }

    @Test
    public void extractIndicatorTalliesShouldReturnMapWithIndicatorTalliesInResultSetWhenMultiResultContainsData() {

        ArrayList<Object> tallies = new ArrayList<>();
        tallies.add(new Object[]{"gender", "counter"});
        tallies.add(new Object[]{"male", 9F});
        tallies.add(new Object[]{"Female", 30F});

        CompositeIndicatorTally compositeIndicatorTally = new CompositeIndicatorTally(9L, 5, "ISN", new Date());
        compositeIndicatorTally.setValueSet(new Gson().toJson(tallies));
        HashMap<String, Float> actualResult = reportIndicatorDao.extractIndicatorTallies(tallies, new DefaultMultiResultProcessor(), null, compositeIndicatorTally);
        Assert.assertEquals(2, actualResult.size());
        Assert.assertTrue(actualResult.containsKey("ISN_male"));
        Assert.assertTrue(actualResult.containsKey("ISN_Female"));

        Assert.assertEquals(9F, actualResult.get("ISN_male"), 0);
        Assert.assertEquals(30F, actualResult.get("ISN_Female"), 0);
    }

    @Test
    public void extractExpectedIndicatorsFromMultiResultShouldReturnZeroValuesForUnlocatedIndicatorCodes() {
        ArrayList<String> expectedIndicators = new ArrayList<>();
        expectedIndicators.add("ISN_IVN");
        expectedIndicators.add("ISN_OPV");
        expectedIndicators.add("ISN_PCV");
        IndicatorQuery indicatorQuery = new IndicatorQuery(8L, "ISN", "SOme query", 1, true, expectedIndicators);

        CompositeIndicatorTally compositeIndicatorTally = new CompositeIndicatorTally(9L, 5, "ISN", new Date());
        ArrayList<Object> tallies = new ArrayList<>();
        tallies.add(new Object[]{"gender", "counter"});
        tallies.add(new Object[]{"BCG", 9F});
        tallies.add(new Object[]{"Polio", 30F});
        tallies.add(new Object[]{"IVN", 30F});
        compositeIndicatorTally.setValueSet(new Gson().toJson(tallies));

        ArrayList<Object> finalMultiResult = reportIndicatorDao.extractExpectedIndicatorsFromMultiResult(indicatorQuery, compositeIndicatorTally, tallies);

        Assert.assertEquals(3, finalMultiResult.size());
        Assert.assertEquals(30D, (Float) ((Object[]) finalMultiResult.get(0))[1], 0F);
        Assert.assertEquals(0D, (Float) ((Object[]) finalMultiResult.get(1))[1], 0F);
        Assert.assertEquals(0D, (Float) ((Object[]) finalMultiResult.get(2))[1], 0F);

        Assert.assertEquals("ISN_OPV", (String) ((Object[]) finalMultiResult.get(1))[0]);
        Assert.assertEquals("ISN_PCV", (String) ((Object[]) finalMultiResult.get(2))[0]);
    }
}
