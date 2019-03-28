package org.smartregister.reporting.models;

public class NumericIndicatorData extends ReportingIndicatorData {
    private int value;

    public NumericIndicatorData(String indicatorLabel, int value) {
        super(indicatorLabel);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
