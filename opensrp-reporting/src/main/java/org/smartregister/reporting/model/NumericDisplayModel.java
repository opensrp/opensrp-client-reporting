package org.smartregister.reporting.model;


import org.smartregister.reporting.contract.ReportContract;

public class NumericDisplayModel {

    private ReportContract.IndicatorView.CountType countType;
    private String indicatorCode;
    private int labelStringResource;
    private float count;

    public NumericDisplayModel(ReportContract.IndicatorView.CountType countType, String indicatorCode, int labelStringResource, float count) {
        this.countType = countType;
        this.indicatorCode = indicatorCode;
        this.labelStringResource = labelStringResource;
        this.count = count;
    }

    public ReportContract.IndicatorView.CountType getCountType() {
        return countType;
    }

    public String getIndicatorCode() {
        return indicatorCode;
    }

    public int getLabelStringResource() {
        return labelStringResource;
    }

    public float getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
