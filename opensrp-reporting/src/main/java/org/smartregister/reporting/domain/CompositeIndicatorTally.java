package org.smartregister.reporting.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2019-07-10
 */

public class CompositeIndicatorTally extends IndicatorTally {

    private String valueSet;
    private boolean isValueSet;

    @Nullable
    private List<String> expectedIndicators;

    public CompositeIndicatorTally() {
    }

    public CompositeIndicatorTally(Long id, int count, String indicatorCode, Date createdAt) {
        super(id, count, indicatorCode, createdAt);
    }


    public CompositeIndicatorTally(Long id, @NonNull String valueSet, String indicatorCode, Date createdAt) {
        super(id, 0, indicatorCode, createdAt);
        setValueSet(valueSet);
        setValueSetFlag(true);
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

    @Nullable
    public List<String> getExpectedIndicators() {
        return expectedIndicators;
    }

    public void setExpectedIndicators(@Nullable List<String> expectedIndicators) {
        this.expectedIndicators = expectedIndicators;
    }
}
