package org.smartregister.reporting.domain;

import java.util.Date;
import java.util.Map;

public class MultiValueIndicatorTally extends IndicatorTally {

    private Map<String, String> multiValuesMap;

    public MultiValueIndicatorTally(Long id, int count, String indicatorCode, Date createdAt, Map<String, String> multiResults) {
        super(id, count, indicatorCode, createdAt);
        this.multiValuesMap = multiResults;
    }

    public MultiValueIndicatorTally() {
    }

    public Map<String, String> getMultiValuesMap() {
        return multiValuesMap;
    }

    public void setMultiValuesMap(Map<String, String> multiValuesMap) {
        this.multiValuesMap = multiValuesMap;
    }
}
