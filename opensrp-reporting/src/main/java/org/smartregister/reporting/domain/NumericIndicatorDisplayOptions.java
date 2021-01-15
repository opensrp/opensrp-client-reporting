package org.smartregister.reporting.domain;

public class NumericIndicatorDisplayOptions extends ReportingIndicatorDisplayOptions {
    private float value;

    public NumericIndicatorDisplayOptions(String label, float value) {
        super(label);
        this.value = value;
    }

    public NumericIndicatorDisplayOptions() {
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
