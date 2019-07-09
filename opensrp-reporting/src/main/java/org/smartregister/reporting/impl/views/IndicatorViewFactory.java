package org.smartregister.reporting.impl.views;

import android.view.View;

public class IndicatorViewFactory {
    public static View createView(IndicatorView indicatorView) {
        return indicatorView.createView();
    }
}
