package org.smartregister.reporting.interfaces;

import org.smartregister.reporting.model.PieChartSlice;

public interface PieChartSelectListener {
    void handleOnSelectEvent(PieChartSlice selectValue);
}
