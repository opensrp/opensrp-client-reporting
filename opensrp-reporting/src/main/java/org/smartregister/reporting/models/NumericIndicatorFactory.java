package org.smartregister.reporting.models;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.smartregister.reporting.R;
import org.smartregister.reporting.interfaces.IndicatorVisualisationFactory;

/**
 * The NumericIndicatorFactory provides functionality to generate views that display a numeric indicator
 *
 * @author allan
 */

public class NumericIndicatorFactory implements IndicatorVisualisationFactory {
    @Override
    public View getIndicatorView(ReportingIndicatorVisualization data, Context context) {
        NumericIndicatorVisualization indicatorData = (NumericIndicatorVisualization) data;

        ConstraintLayout rootLayout = (ConstraintLayout) LayoutInflater.from(context).inflate(R.layout.numeric_indicator_view, null);

        TextView chartLabelTextView = rootLayout.findViewById(R.id.numeric_indicator_label);
        TextView chartValueTextView = rootLayout.findViewById(R.id.numeric_indicator_value);
        chartLabelTextView.setText(indicatorData.getIndicatorLabel());
        chartValueTextView.setText(String.valueOf(indicatorData.getValue()));

        return rootLayout;
    }
}
