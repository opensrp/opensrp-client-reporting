package org.smartregister.reporting.view;

import android.content.Context;
import android.view.View;

import org.smartregister.reporting.domain.ReportingIndicatorVisualization;

public interface IndicatorVisualisationFactory {
    View getIndicatorView(ReportingIndicatorVisualization data, Context context);
}
