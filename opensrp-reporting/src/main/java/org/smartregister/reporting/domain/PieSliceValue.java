package org.smartregister.reporting.domain;

import lecho.lib.hellocharts.model.SliceValue;

public class PieSliceValue extends SliceValue {

    private String callOutLabel;

    public String getCallOutLabel() {
        return callOutLabel;
    }

    public SliceValue setCallOutLabel(String callOutLabel) {
        this.callOutLabel = callOutLabel;
        return this;
    }
}
