package org.smartregister.reporting.models;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.smartregister.reporting.R;
import org.smartregister.reporting.interfaces.IndicatorVisualisationFactory;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * The PieChartIndicatorFactory provides functionality to generate views that display a pie chart indicator
 *
 * @author allan
 */

public class PieChartIndicatorFactory implements IndicatorVisualisationFactory {
    @Override
    public View getIndicatorView(ReportingIndicatorVisualization visualization, Context context) {

        PieChartIndicatorVisualization indicatorVisualization = (PieChartIndicatorVisualization) visualization;

        ConstraintLayout rootLayout = (ConstraintLayout) LayoutInflater.from(context).inflate(R.layout.pie_chart_view, null);

        TextView chartLabelTextView = rootLayout.findViewById(R.id.pie_indicator_label);
        chartLabelTextView.setText(indicatorVisualization.getIndicatorLabel());

        PieChartView pieChartView = rootLayout.findViewById(R.id.pie_chart);

        // Retrieve chart data
        PieChartData chartData = new PieChartData();
        PieChartIndicatorData chartConfiguration = indicatorVisualization.getChartData();
        chartData.setHasCenterCircle(chartConfiguration.hasCenterCircle());
        chartData.setHasLabels(chartConfiguration.hasLabels());
        chartData.setHasLabelsOutside(chartConfiguration.hasLabelsOutside());

        // Retrieve slice values
        List<SliceValue> sliceValues = new ArrayList<>();
        SliceValue value;
        for (PieChartSlice slice : chartConfiguration.getSlices()) {
            value = new SliceValue();
            value.setColor(slice.getColor());
            value.setValue(slice.getValue());
            sliceValues.add(value);
        }
        chartData.setValues(sliceValues);

        pieChartView.setChartRotationEnabled(false);
        pieChartView.setCircleFillRatio(0.8f);
        pieChartView.setPieChartData(chartData);
        // pieChartView.setOnValueTouchListener(listener);

        return rootLayout;
    }
}
