package org.smartregister.reporting.domain;

import java.util.Date;

public class IndicatorTally {
    private Long id;
    private int count = 0;
    private String indicatorCode;
    private String valueSet;
    private boolean isValueSet;
    private Date createdAt;

    public IndicatorTally(Long id, int count, String indicatorCode, Date createdAt) {
        this.id = id;
        this.count = count;
        this.indicatorCode = indicatorCode;
        this.createdAt = createdAt;
    }

    public IndicatorTally() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getIndicatorCode() {
        return indicatorCode;
    }

    public void setIndicatorCode(String indicatorCode) {
        this.indicatorCode = indicatorCode;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getValueSet() {
        return valueSet;
    }

    public void setValueSet(String valueSet) {
        this.valueSet = valueSet;
    }

    public boolean isValueSet() {
        return isValueSet;
    }

    public void setValueSetFlag(boolean valueSet) {
        isValueSet = valueSet;
    }
}
