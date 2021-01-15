package org.smartregister.reporting.factory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.smartregister.reporting.R;
import org.smartregister.reporting.domain.NumericIndicatorDisplayOptions;
import org.smartregister.reporting.domain.ReportingIndicatorDisplayOptions;
import org.smartregister.reporting.util.ReportingUtil;

/**
 * The NumericDisplayFactory provides functionality to generate views that display a numeric indicator
 *
 * @author allan
 */

public class NumericDisplayFactory implements IndicatorVisualisationFactory {
    @Override
    public View getIndicatorView(ReportingIndicatorDisplayOptions displayOptions, Context context) {
        NumericIndicatorDisplayOptions numericIndicatorDisplay = (NumericIndicatorDisplayOptions) displayOptions;

        View rootLayout = LayoutInflater.from(context).inflate(R.layout.numeric_indicator_view, null);

        TextView chartLabelTextView = rootLayout.findViewById(R.id.numeric_indicator_label);
        chartLabelTextView.setText(numericIndicatorDisplay.getIndicatorLabel());
        TextView chartValueTextView = rootLayout.findViewById(R.id.numeric_indicator_value);
        // Only show the required decimal points
        chartValueTextView.setText(ReportingUtil.formatDecimal(numericIndicatorDisplay.getValue()));

        return rootLayout;
    }
}