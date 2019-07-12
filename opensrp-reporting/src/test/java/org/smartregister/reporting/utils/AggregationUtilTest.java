package org.smartregister.reporting.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.util.AggregationUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.smartregister.reporting.BaseUnitTest.getDate;
import static org.smartregister.reporting.BaseUnitTest.getDateTime;

@RunWith(JUnit4.class)
public class AggregationUtilTest {
    @Test
    public void tesStaticIndicatorCountWithEmptyIndicatorsMap() {
        Map<String, IndicatorTally> tally1 = Collections.emptyMap();
        List indicatorTallies = Collections.unmodifiableList(Collections.singletonList(tally1));
        long indicator1 = AggregationUtil.getStaticIndicatorCount(indicatorTallies, "indicator1");
        Assert.assertEquals(0, indicator1);

    }

    @Test
    public void testStaticIndicatorCountWithNullList() {
        long indicator1 = AggregationUtil.getStaticIndicatorCount(null, "indicator1");
        Assert.assertEquals(0, indicator1);
    }

    @Test
    public void testStaticIndicatorCount() {
        Map<String, IndicatorTally> tally1 = new HashMap<>();
        tally1.put("indicator1", new IndicatorTally(null, 3, "indicator1", null));
        Map<String, IndicatorTally> tally2 = new HashMap<>();
        tally2.put("indicator1", new IndicatorTally(null, 9, "indicator1", null));
        Map<String, IndicatorTally> tally3 = new HashMap<>();
        tally3.put("indicator2", new IndicatorTally(null, 7, "indicator2", null));
        List indicatorTallies = Collections.unmodifiableList(Collections.unmodifiableList(Arrays.asList(tally1, tally2, tally3)));
        long indicator1 = AggregationUtil.getStaticIndicatorCount(indicatorTallies, "indicator1");
        long indicator2 = AggregationUtil.getStaticIndicatorCount(indicatorTallies, "indicator2");
        Assert.assertEquals(12, indicator1);
        Assert.assertEquals(7, indicator2);

    }

    @Test
    public void testLatestIndicatorCount() {
        Map<String, IndicatorTally> tally1 = new HashMap<>();

        tally1.put("indicator1", new IndicatorTally(null, 3, "indicator1", getDate(2019, 3, 31)));
        Map<String, IndicatorTally> tally2 = new HashMap<>();
        tally2.put("indicator1", new IndicatorTally(null, 19, "indicator1", getDate(2019, 4, 30)));
        Map<String, IndicatorTally> tally3 = new HashMap<>();
        tally3.put("indicator1", new IndicatorTally(null, 27, "indicator1", getDate(2019, 4, 30)));
        Map<String, IndicatorTally> tally4 = new HashMap<>();
        tally4.put("indicator1", new IndicatorTally(null, 3, "indicator1", getDate(2019, 5, 31)));
        Map<String, IndicatorTally> tally5 = new HashMap<>();
        tally5.put("indicator1", new IndicatorTally(null, 9, "indicator1", getDate(2019, 1, 31)));
        Map<String, IndicatorTally> tally6 = new HashMap<>();
        tally6.put("indicator2", new IndicatorTally(null, 7, "indicator2", getDateTime(2019, 3, 31, 9,54,4)));
        Map<String, IndicatorTally> tally7 = new HashMap<>();
        tally7.put("indicator2", new IndicatorTally(null, 13, "indicator2", getDateTime(2019, 3, 31,10, 45, 20)));
        Map<String, IndicatorTally> tally8 = new HashMap<>();
        tally8.put("indicator2", new IndicatorTally(null, 9, "indicator2", getDateTime(2019, 3, 31, 10, 20, 20)));
        Map<String, IndicatorTally> tally9 = new HashMap<>();
        tally9.put("indicator2", new IndicatorTally(null, 7, "indicator2", getDate(2019, 2, 21)));
        Map<String, IndicatorTally> tally10 = new HashMap<>();
        tally10.put("indicator2", new IndicatorTally(null, 7, "indicator2", getDate(2019, 2, 23)));
        List indicatorTallies = Collections.unmodifiableList(Collections.unmodifiableList(
                Arrays.asList(tally1, tally2, tally3, tally4, tally5, tally6, tally7, tally8, tally9, tally10)));
        long indicator1 = AggregationUtil.getLatestIndicatorCount(indicatorTallies, "indicator1");
        long indicator2 = AggregationUtil.getLatestIndicatorCount(indicatorTallies, "indicator2");
        Assert.assertEquals(3, indicator1);
        Assert.assertEquals(13, indicator2);
    }


}
