package org.smartregister.reporting.model;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.smartregister.reporting.R;
import org.smartregister.reporting.interfaces.IndicatorVisualisationFactory;
import org.smartregister.reporting.interfaces.PieChartSelectListener;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * The PieChartFactory provides functionality to generate views that display a pie chart indicator
 *
 * @author allan
 */

public class PieChartFactory implements IndicatorVisualisationFactory {
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
        pieChartView.setOnValueTouchListener(new ChartValueSelectListener(chartConfiguration.getListener()));

        return rootLayout;
    }

    private class ChartValueSelectListener implements PieChartOnValueSelectListener {
        private PieChartSelectListener listener;

        private ChartValueSelectListener(PieChartSelectListener listener) {
            this.listener = listener;
        }

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            PieChartSlice sliceValue = new PieChartSlice();
            sliceValue.setColor(value.getColor());
            sliceValue.setValue(value.getValue());
            listener.handleOnSelectEvent(sliceValue);
        }

        @Override
        public void onValueDeselected() {
            // Handle deselection.
            // No action required for now
        }
    }
}
