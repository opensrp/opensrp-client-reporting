package org.smartregister.reporting.model;


import org.smartregister.reporting.contract.ReportContract;

public class IndicatorDisplayModel {

    private String indicatorCode;
    private int labelStringResource;
    private ReportContract.IndicatorView.CountType countType;
    private long totalCount;


    public IndicatorDisplayModel(ReportContract.IndicatorView.CountType countType, String indicatorCode, int labelStringResource, long count) {
        this.countType = countType;
        this.indicatorCode = indicatorCode;
        this.labelStringResource = labelStringResource;
        this.totalCount = count;
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

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
