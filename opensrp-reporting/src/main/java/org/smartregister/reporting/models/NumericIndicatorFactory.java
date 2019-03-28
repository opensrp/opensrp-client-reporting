package org.smartregister.reporting.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.smartregister.reporting.R;
import org.smartregister.reporting.interfaces.CommonReportingVisualisationListener;
import org.smartregister.reporting.interfaces.IndicatorVisualisationFactory;

public class NumericIndicatorFactory implements IndicatorVisualisationFactory {
    @Override
    public View getIndicatorView(ReportingIndicatorData data, Context context, CommonReportingVisualisationListener listener) {
        NumericIndicatorData indicatorData = (NumericIndicatorData) data;

        RelativeLayout rootLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.numeric_indicator_view, null);

        TextView chartLabelTextView = rootLayout.findViewById(R.id.numeric_indicator_label);
        TextView chartValueTextView = rootLayout.findViewById(R.id.numeric_indicator_value);
        chartLabelTextView.setText(indicatorData.getIndicatorLabel());
        chartValueTextView.setText(String.valueOf(indicatorData.getValue()));

        return rootLayout;
    }
}
