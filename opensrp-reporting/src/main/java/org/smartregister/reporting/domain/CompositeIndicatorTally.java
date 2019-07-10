package org.smartregister.reporting.domain;

import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2019-07-10
 */

public class CompositeIndicatorTally extends IndicatorTally {

    private String valueSet;
    private boolean isValueSet;

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
}
