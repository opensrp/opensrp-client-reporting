package org.smartregister.reporting.domain;

/**
 * NumericIndicatorVisualization is the base class for indicator visualizations that display
 * numeric data e.g. tallies
 * The numeric indicator visualization consists the indicator label (text description) and the value (count/amount) to be displayed
 *
 * @author allan
 */
public class NumericIndicatorVisualization extends ReportingIndicatorVisualization {
    private long value;

    public NumericIndicatorVisualization(String indicatorLabel, long value) {
        super(indicatorLabel);
        this.value = value;
    }

    public NumericIndicatorVisualization() {

    }

    public long getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
