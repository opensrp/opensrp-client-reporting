package org.smartregister.reporting.impl.views;

import android.view.View;

public interface IndicatorView {
    View createView();

    enum CountType {
        STATIC_COUNT,
        LATEST_COUNT
    }
}
