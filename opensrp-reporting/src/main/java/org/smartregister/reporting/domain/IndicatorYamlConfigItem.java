package org.smartregister.reporting.domain;

public class IndicatorYamlConfigItem {

    public static String INDICATOR_PROPERTY = "indicatorData";

    private String key;
    private String description;
    private String indicatorQuery;
    private boolean isMultiResult;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIndicatorQuery() {
        return indicatorQuery;
    }

    public void setIndicatorQuery(String indicatorQuery) {
        this.indicatorQuery = indicatorQuery;
    }

    public boolean isMultiResult() {
        return isMultiResult;
    }

    public void setMultiResult(boolean multiResult) {
        isMultiResult = multiResult;
    }
}
