package org.smartregister.reporting.models;

public class PieChartIndicatorData extends ReportingIndicatorData {

    private int yesValue;
    private int noValue;

    public PieChartIndicatorData(String indicatorLabel, int yesValue, int noValue) {
        super(indicatorLabel);
        this.yesValue = yesValue;
        this.noValue = noValue;
    }

    public int getYesValue() {
        return yesValue;
    }

    public void setYesValue(int yesValue) {
        this.yesValue = yesValue;
    }

    public int getNoValue() {
        return noValue;
    }

    public void setNoValue(int noValue) {
        this.noValue = noValue;
    }

}
