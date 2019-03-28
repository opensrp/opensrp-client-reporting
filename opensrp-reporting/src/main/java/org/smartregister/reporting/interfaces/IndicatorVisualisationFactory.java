package org.smartregister.reporting.interfaces;

import android.content.Context;
import android.view.View;

import org.smartregister.reporting.models.ReportingIndicatorData;

public interface IndicatorVisualisationFactory {
    View getIndicatorView(ReportingIndicatorData data, Context context, CommonReportingVisualisationListener listener);
}
