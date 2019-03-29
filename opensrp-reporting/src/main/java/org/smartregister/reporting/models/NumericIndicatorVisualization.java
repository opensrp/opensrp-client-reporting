package org.smartregister.reporting.models;

/**
 * NumericIndicatorVisualization is the base class for indicator visualizations that display
 * numeric data e.g. tallies
 * The numeric indicator visualization consists the label (text description) and the value (count/amount) to be displayed
 *
 * @author allan
 *
 *
 */
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
