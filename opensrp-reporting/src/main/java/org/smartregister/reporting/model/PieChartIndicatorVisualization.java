package org.smartregister.reporting.model;

import org.smartregister.reporting.listener.PieChartSelectListener;

import java.util.List;

/**
 * PieChartIndicatorVisualization is the base class for indicator visualizations that display
 * pie chart data
 * The pie chart indicator visualization consists the indicator label (text description) and the chart data that
 * defines configuration of the chart that is to be displayed
 *
 * @author allan
 */

public class PieChartIndicatorVisualization extends ReportingIndicatorVisualization {

    private PieChartIndicatorData chartData;

    private PieChartIndicatorVisualization(PieChartIndicatorData chartData) {
        this.chartData = chartData;
    }

    public PieChartIndicatorData getChartData() {
        return chartData;
    }

    public void setChartData(PieChartIndicatorData chartData) {
        this.chartData = chartData;
    }

    public static class PieChartIndicatorVisualizationBuilder {

        private String indicatorLabel;
        private boolean hasLabels;
        private boolean hasLabelsOutside;
        private boolean hasCenterCircle;
        private List<PieChartSlice> slices;
        private PieChartSelectListener listener;

        public PieChartIndicatorVisualizationBuilder indicatorLabel(String label) {
            this.indicatorLabel = label;
            return this;
        }

        public PieChartIndicatorVisualizationBuilder chartHasLabels(boolean hasLabels) {
            this.hasLabels = hasLabels;
            return this;
        }

        public PieChartIndicatorVisualizationBuilder chartHasLabelsOutside(boolean hasLabelsOutside) {
            this.hasLabelsOutside = hasLabelsOutside;
            return this;
        }

        public PieChartIndicatorVisualizationBuilder chartHasCenterCircle(boolean hasCenterCircle) {
            this.hasCenterCircle = hasCenterCircle;
            return this;
        }

        public PieChartIndicatorVisualizationBuilder chartSlices(List<PieChartSlice> slices) {
            this.slices = slices;
            return this;
        }

        public PieChartIndicatorVisualizationBuilder chartListener(PieChartSelectListener listener) {
            this.listener = listener;
            return this;
        }

        public PieChartIndicatorVisualization build() {
            PieChartIndicatorData chartData = new PieChartIndicatorData();
            chartData.setHasLabels(this.hasLabels);
            chartData.setHasLabelsOutside(this.hasLabelsOutside);
            chartData.setHasCenterCircle(this.hasCenterCircle);
            chartData.setSlices(this.slices);
            chartData.setListener(this.listener);
            PieChartIndicatorVisualization pieChartIndicatorVisualization = new PieChartIndicatorVisualization(chartData);
            pieChartIndicatorVisualization.setIndicatorLabel(this.indicatorLabel);
            return pieChartIndicatorVisualization;
        }

    }

}
