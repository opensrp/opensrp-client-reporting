package org.smartregister.reporting.listener;

import org.smartregister.reporting.model.PieChartSlice;

public interface PieChartSelectListener {
    void handleOnSelectEvent(PieChartSlice selectValue);
}
