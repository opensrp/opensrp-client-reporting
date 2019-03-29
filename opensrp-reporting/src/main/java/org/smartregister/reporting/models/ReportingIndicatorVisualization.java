package org.smartregister.reporting.models;

public class ReportingIndicatorVisualization {
    private String indicatorLabel;

    public ReportingIndicatorVisualization() {}

    public ReportingIndicatorVisualization(String indicatorLabel) {
        this.indicatorLabel = indicatorLabel;
    }

    public String getIndicatorLabel() {
        return indicatorLabel;
    }

    public void setIndicatorLabel(String indicatorLabel) {
        this.indicatorLabel = indicatorLabel;
    }
}
