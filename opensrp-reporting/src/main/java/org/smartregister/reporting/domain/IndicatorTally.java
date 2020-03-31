package org.smartregister.reporting.domain;

import java.util.Date;

public class IndicatorTally {
    private Long id;
    private float count = 0;
    private String indicatorCode;
    private String grouping;
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
        return (int) count;
    }

    public float getFloatCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
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

    public String getGrouping() {
        return grouping;
    }

    public void setGrouping(String grouping) {
        this.grouping = grouping;
    }
}
