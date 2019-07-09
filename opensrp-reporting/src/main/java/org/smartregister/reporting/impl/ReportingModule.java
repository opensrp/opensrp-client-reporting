package org.smartregister.reporting.impl;

import android.view.ViewGroup;

import org.smartregister.reporting.domain.IndicatorTally;

import java.util.List;
import java.util.Map;

public interface ReportingModule {
    void generateReport(ViewGroup mainLayout);

    List<Map<String, IndicatorTally>> getIndicatorTallies();

    void setIndicatorTallies(List<Map<String, IndicatorTally>> indicatorTallies);
}
