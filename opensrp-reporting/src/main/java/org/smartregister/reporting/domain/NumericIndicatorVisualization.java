package org.smartregister.reporting.domain;

/**
 * NumericIndicatorVisualization is the base class for indicator visualizations that display
 * numeric data e.g. tallies
 * The numeric indicator visualization consists the indicator label (text description) and the value (count/amount) to be displayed
 *
 * @author allan
 */
public class NumericIndicatorVisualization extends ReportingIndicatorVisualization {
    private float value;

    public NumericIndicatorVisualization(String indicatorLabel, float value) {
        super(indicatorLabel);
        this.value = value;
    }

    public NumericIndicatorVisualization() {

    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
