package org.smartregister.reporting.domain;

import android.support.annotation.Nullable;

import java.util.List;

public class IndicatorQuery {
    private Long id;
    private String indicatorCode;
    private String query;
    private String grouping;
    private int dbVersion;
    private boolean isMultiResult;
    private List<String> expectedIndicators;

    public IndicatorQuery(Long id, String indicatorCode, String query, int dbVersion, boolean isMultiResult, @Nullable List<String> expectedIndicators) {
        this(id, indicatorCode, query, dbVersion, isMultiResult, expectedIndicators, null);
    }

    public IndicatorQuery(Long id, String indicatorCode, String query, int dbVersion, boolean isMultiResult, @Nullable List<String> expectedIndicators, @Nullable String grouping) {
        this.id = id;
        this.indicatorCode = indicatorCode;
        this.query = query;
        this.dbVersion = dbVersion;
        this.isMultiResult = isMultiResult;
        this.expectedIndicators = expectedIndicators;
        this.grouping = grouping;
    }

    public IndicatorQuery() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndicatorCode() {
        return indicatorCode;
    }

    public void setIndicatorCode(String indicatorCode) {
        this.indicatorCode = indicatorCode;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(int dbVersion) {
        this.dbVersion = dbVersion;
    }

    public boolean isMultiResult() {
        return isMultiResult;
    }

    public void setMultiResult(boolean multiResult) {
        isMultiResult = multiResult;
    }

    public List<String> getExpectedIndicators() {
        return expectedIndicators;
    }

    public void setExpectedIndicators(List<String> expectedIndicators) {
        this.expectedIndicators = expectedIndicators;
    }

    public String getGrouping() {
        return grouping;
    }

    public void setGrouping(String grouping) {
        this.grouping = grouping;
    }
}
