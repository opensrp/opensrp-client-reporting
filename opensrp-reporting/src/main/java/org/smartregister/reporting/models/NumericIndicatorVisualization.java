package org.smartregister.reporting.models;

public class NumericIndicatorVisualization extends ReportingIndicatorVisualization {
    private int value;

    public NumericIndicatorVisualization(String indicatorLabel, int value) {
        super(indicatorLabel);
        this.value = value;
    }

    public NumericIndicatorVisualization() {

    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
