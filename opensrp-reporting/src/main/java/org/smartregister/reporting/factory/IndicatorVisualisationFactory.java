package org.smartregister.reporting.factory;

import android.content.Context;
import android.view.View;

import org.smartregister.reporting.domain.ReportingIndicatorDisplayOptions;

public interface IndicatorVisualisationFactory {
    View getIndicatorView(ReportingIndicatorDisplayOptions displayOptions, Context context);
}
