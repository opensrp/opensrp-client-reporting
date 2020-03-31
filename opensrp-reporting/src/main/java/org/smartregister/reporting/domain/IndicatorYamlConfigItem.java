package org.smartregister.reporting.domain;

import java.util.List;

public class IndicatorYamlConfigItem {

    public static String INDICATOR_PROPERTY = "indicatorData";

    private String key;
    private String description;
    private String indicatorQuery;
    private boolean isMultiResult;
    private List<String> expectedIndicators;
    private String grouping;

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
