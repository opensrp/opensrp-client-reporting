package org.smartregister.reporting.models;

/**
 * PieChartIndicatorVisualization is the base class for indicator visualizations that display
 * pie chart data
 * The pie chart indicator visualization consists the label (text description) and the chart data that
 * defines configuration of the chart that is to be displayed
 *
 * @author allan
 *
 *
 */

public class PieChartIndicatorVisualization extends ReportingIndicatorVisualization {

    private PieChartIndicatorData chartData;

    public PieChartIndicatorVisualization(PieChartIndicatorData chartData) {
        this.chartData = chartData;
    }

    public PieChartIndicatorVisualization() {
    }

    public PieChartIndicatorData getChartData() {
        return chartData;
    }

    public void setChartData(PieChartIndicatorData chartData) {
        this.chartData = chartData;
    }
}
