package org.smartregister.reporting.factory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.smartregister.reporting.R;
import org.smartregister.reporting.domain.ProgressIndicatorConfig;
import org.smartregister.reporting.domain.ProgressIndicatorDisplayOptions;
import org.smartregister.reporting.domain.ReportingIndicatorDisplayOptions;
import org.smartregister.reporting.view.ProgressIndicator;

public class ProgressIndicatorFactory implements IndicatorVisualisationFactory {
    @Override
    public View getIndicatorView(ReportingIndicatorDisplayOptions displayOptions, Context context) {
        ProgressIndicatorConfig config = ((ProgressIndicatorDisplayOptions) displayOptions).getConfig();
        LinearLayout rootLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.progress_indicator, null);
        ProgressIndicator progressWidget = rootLayout.findViewById(R.id.progressIndicatorView);
        progressWidget.setTitle(config.getTitle());
        progressWidget.setSubTitle(config.getSubtitle());
        progressWidget.setProgress(config.getProgressVal());
        progressWidget.setProgressBarForegroundColor(config.getForegroundColor());
        progressWidget.setProgressBarBackgroundColor(config.getBackgroundColor());

        return rootLayout;
    }
}
