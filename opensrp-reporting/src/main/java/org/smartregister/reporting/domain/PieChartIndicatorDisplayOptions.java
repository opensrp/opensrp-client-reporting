package org.smartregister.reporting.domain;

import org.smartregister.reporting.listener.PieChartSelectListener;

import java.util.List;

public class PieChartIndicatorDisplayOptions extends ReportingIndicatorDisplayOptions {

    private PieChartConfig pieChartConfig;

    public PieChartIndicatorDisplayOptions(PieChartConfig pieChartConfig) {
        this.pieChartConfig = pieChartConfig;
    }

    public PieChartConfig getPieChartConfig() {
        return pieChartConfig;
    }

    public void setPieChartConfig(PieChartConfig pieChartConfig) {
        this.pieChartConfig = pieChartConfig;
    }

    public static class PieChartIndicatorVisualizationBuilder {

        private String indicatorLabel;
        private String indicatorNote;
        private boolean hasLabels;
        private boolean hasLabelsOutside;
        private boolean hasCenterCircle;
        private List<PieChartSlice> slices;
        private PieChartSelectListener listener;

        public PieChartIndicatorVisualizationBuilder indicatorLabel(String label) {
            this.indicatorLabel = label;
            return this;
        }

        public PieChartIndicatorVisualizationBuilder indicatorNote(String note) {
            this.indicatorNote = note;
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

        public PieChartIndicatorDisplayOptions build() {
            PieChartConfig chartConfig = new PieChartConfig();
            chartConfig.setHasLabels(this.hasLabels);
            chartConfig.setHasLabelsOutside(this.hasLabelsOutside);
            chartConfig.setHasCenterCircle(this.hasCenterCircle);
            chartConfig.setSlices(this.slices);
            chartConfig.setListener(this.listener);
            PieChartIndicatorDisplayOptions pieChartIndicatorDisplayOptions = new PieChartIndicatorDisplayOptions(chartConfig);
            pieChartIndicatorDisplayOptions.setIndicatorLabel(this.indicatorLabel);
            pieChartIndicatorDisplayOptions.setIndicatorNote(this.indicatorNote);
            return pieChartIndicatorDisplayOptions;
        }

    }

}
