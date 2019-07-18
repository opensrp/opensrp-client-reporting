package org.smartregister.reporting.factory;

import android.content.Context;
import android.view.View;

import org.smartregister.reporting.domain.ReportingIndicatorVisualization;

public interface IndicatorVisualisationFactory {
    View getIndicatorView(ReportingIndicatorVisualization data, Context context);
}
