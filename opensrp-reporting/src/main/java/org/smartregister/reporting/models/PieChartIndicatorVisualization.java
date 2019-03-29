package org.smartregister.reporting.models;

import lecho.lib.hellocharts.model.PieChartData;

public class PieChartIndicatorVisualization extends ReportingIndicatorVisualization {

    private PieChartData chartData;

    public PieChartIndicatorVisualization(PieChartData chartData) {
        this.chartData = chartData;
    }

    public PieChartIndicatorVisualization() {
    }

    public PieChartData getChartData() {
        return chartData;
    }

    public void setChartData(PieChartData chartData) {
        this.chartData = chartData;
    }
}
