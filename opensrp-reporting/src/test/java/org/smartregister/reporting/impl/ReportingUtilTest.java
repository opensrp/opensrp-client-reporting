package org.smartregister.reporting.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.domain.NumericIndicatorDisplayOptions;
import org.smartregister.reporting.util.ReportingUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.smartregister.reporting.BaseUnitTest.getDateTime;

@RunWith(MockitoJUnitRunner.class)
public class ReportingUtilTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTotalCount() {
        Map<String, IndicatorTally> tally1 = new HashMap<>();
        tally1.put("indicator1", new IndicatorTally(null, 3, "indicator1", null));
        Map<String, IndicatorTally> tally2 = new HashMap<>();
        tally2.put("indicator1", new IndicatorTally(null, 9, "indicator1", null));
        Map<String, IndicatorTally> tally3 = new HashMap<>();
        tally3.put("indicator2", new IndicatorTally(null, 7, "indicator2", null));
        List indicatorTallies = Collections.unmodifiableList(Collections.unmodifiableList(Arrays.asList(tally1, tally2, tally3)));
        float indicator1 = ReportingUtil.getTotalCount(indicatorTallies, "indicator1");
        float indicator2 = ReportingUtil.getTotalCount(indicatorTallies, "indicator2");
        assertEquals(12, indicator1, 0);
        assertEquals(7, indicator2, 0);

    }

    @Test
    public void testGetLatestCountBasedOnDate() {
        Map<String, IndicatorTally> tally1 = new HashMap<>();
        tally1.put("indicator2", new IndicatorTally(null, 13, "indicator2", getDateTime(2019, 3, 31, 10, 45, 20)));
        Map<String, IndicatorTally> tally2 = new HashMap<>();
        tally2.put("indicator2", new IndicatorTally(null, 9, "indicator2", getDateTime(2019, 3, 31, 10, 20, 20)));
        List indicatorTallies = Collections.unmodifiableList(Collections.unmodifiableList(Arrays.asList(tally1, tally2)));
        float indicator2 = ReportingUtil.getLatestCountBasedOnDate(indicatorTallies, "indicator2");
        assertEquals(13, indicator2, 0);
    }

    @Test
    public void testGetIndicatorDisplayModelTotalAndLatestCount() {
        Map<String, IndicatorTally> tally1 = new HashMap<>();
        tally1.put("indicator1", new IndicatorTally(null, 3, "indicator1", null));
        Map<String, IndicatorTally> tally2 = new HashMap<>();
        tally2.put("indicator1", new IndicatorTally(null, 9, "indicator1", null));
        Map<String, IndicatorTally> tally3 = new HashMap<>();
        tally3.put("indicator2", new IndicatorTally(null, 13, "indicator2", getDateTime(2019, 3, 31, 10, 45, 20)));
        Map<String, IndicatorTally> tally4 = new HashMap<>();
        tally4.put("indicator2", new IndicatorTally(null, 9, "indicator2", getDateTime(2019, 3, 31, 10, 20, 20)));

        List indicatorTallies = Collections.unmodifiableList(Collections.unmodifiableList(Arrays.asList(tally1, tally2, tally3, tally4)));

        //Test get model with total count
        NumericIndicatorDisplayOptions displayOptions1 = ReportingUtil.getNumericIndicatorDisplayOptions(ReportContract.IndicatorView.CountType.TOTAL_COUNT,
                "indicator1",
                "first indicator", indicatorTallies);
        assertNotNull(displayOptions1);
        assertEquals(12, displayOptions1.getValue(), 0);
        assertEquals("first indicator", displayOptions1.getIndicatorLabel());

        //Test get model with latest count
        NumericIndicatorDisplayOptions displayOptions2 = ReportingUtil.getNumericIndicatorDisplayOptions(ReportContract.IndicatorView.CountType.LATEST_COUNT,
                "indicator2",
                "second indicator", indicatorTallies);
        assertNotNull(displayOptions2);
        assertEquals(13, displayOptions2.getValue(), 0);
        assertEquals("second indicator", displayOptions2.getIndicatorLabel());
    }
}
