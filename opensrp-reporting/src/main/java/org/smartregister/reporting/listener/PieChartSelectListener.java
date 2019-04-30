package org.smartregister.reporting.listener;

import org.smartregister.reporting.domain.PieChartSlice;

public interface PieChartSelectListener {
    void handleOnSelectEvent(PieChartSlice selectValue);
}
