package org.smartregister.reporting.domain;

import android.graphics.Color;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class PieChartIndicatorVisualizationTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void pieChartIndicatorVisualizationBuilderInitsVisualization() {
        // Define pie chart chartSlices
        String indicatorLabel = "Cool Viz";
        String indicatorKey = "IND1";
        int indicatorValue1 = 20;
        int indicatorValue2 = 80;

        List<PieChartSlice> chartSlices = new ArrayList<>();

        Map<String, IndicatorTally> pieChartYesValue = new HashMap<>();
        Map<String, IndicatorTally> pieChartNoValue = new HashMap<>();

        pieChartYesValue.put(indicatorKey, new IndicatorTally(null, indicatorValue1, indicatorKey, new Date()));
        pieChartNoValue.put(indicatorKey, new IndicatorTally(null, indicatorValue2, indicatorKey, new Date()));


        PieChartSlice yesSlice = new PieChartSlice(pieChartYesValue.get(indicatorKey).getCount(), Color.parseColor("#99CC00"));
        PieChartSlice noSlice = new PieChartSlice(pieChartNoValue.get(indicatorKey).getCount(), Color.parseColor("#FF4444"));
        chartSlices.add(yesSlice);
        chartSlices.add(noSlice);

        // Build the chart
        PieChartIndicatorVisualization pieChartIndicatorVisualization = new PieChartIndicatorVisualization.PieChartIndicatorVisualizationBuilder()
                .indicatorLabel(indicatorLabel)
                .chartHasLabels(true)
                .chartHasLabelsOutside(true)
                .chartHasCenterCircle(false)
                .chartSlices(chartSlices)
                .chartListener(null).build();

        Assert.assertEquals(indicatorLabel, pieChartIndicatorVisualization.getIndicatorLabel());
        Assert.assertTrue(pieChartIndicatorVisualization.getChartData().hasLabels());
        Assert.assertTrue(pieChartIndicatorVisualization.getChartData().hasLabelsOutside());
        Assert.assertFalse(pieChartIndicatorVisualization.getChartData().hasCenterCircle());
        Assert.assertEquals(indicatorValue1, pieChartIndicatorVisualization.getChartData().getSlices().get(0).getValue(), 0.0001);
        Assert.assertEquals(indicatorValue2, pieChartIndicatorVisualization.getChartData().getSlices().get(1).getValue(), 0.0001);
    }

}
