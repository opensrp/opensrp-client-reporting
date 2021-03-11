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


        PieChartSlice yesSlice = new PieChartSlice(pieChartYesValue.get(indicatorKey).getCount(),"Yes", Color.parseColor("#99CC00"), indicatorKey);
        PieChartSlice noSlice = new PieChartSlice(pieChartNoValue.get(indicatorKey).getCount(),"No", Color.parseColor("#FF4444"), indicatorKey);
        chartSlices.add(yesSlice);
        chartSlices.add(noSlice);

        // Build the chart
        PieChartIndicatorDisplayOptions pieChartIndicatorVisualization = new PieChartIndicatorDisplayOptions.PieChartIndicatorVisualizationBuilder()
                .indicatorLabel(indicatorLabel)
                .chartHasLabels(true)
                .indicatorNote("Note")
                .chartHasLabelsOutside(true)
                .chartHasCenterCircle(false)
                .chartSlices(chartSlices)
                .chartListener(null).build();

        Assert.assertEquals(indicatorLabel, pieChartIndicatorVisualization.getIndicatorLabel());
        Assert.assertTrue(pieChartIndicatorVisualization.getPieChartConfig().hasLabels());
        Assert.assertTrue(pieChartIndicatorVisualization.getPieChartConfig().hasLabelsOutside());
        Assert.assertFalse(pieChartIndicatorVisualization.getPieChartConfig().hasCenterCircle());
        Assert.assertEquals(indicatorValue1, pieChartIndicatorVisualization.getPieChartConfig().getSlices().get(0).getValue(), 0.0001);
        Assert.assertEquals(indicatorValue2, pieChartIndicatorVisualization.getPieChartConfig().getSlices().get(1).getValue(), 0.0001);
    }

}
