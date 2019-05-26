package org.smartregister.reporting.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.smartregister.reporting.R;
import org.smartregister.reporting.domain.PieChartIndicatorData;
import org.smartregister.reporting.domain.PieChartIndicatorVisualization;
import org.smartregister.reporting.domain.PieChartSlice;
import org.smartregister.reporting.domain.ReportingIndicatorVisualization;
import org.smartregister.reporting.listener.PieChartSelectListener;

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

        LinearLayout rootLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.pie_chart_view, null);
        TextView chartLabelTextView = rootLayout.findViewById(R.id.pie_indicator_label);
        TextView chartNoteTextView = rootLayout.findViewById(R.id.pie_note_label);
        TextView numericValueTextView = rootLayout.findViewById(R.id.numeric_indicator_value);

        PieChartIndicatorVisualization indicatorVisualization = (PieChartIndicatorVisualization) visualization;
        PieChartIndicatorData chartConfiguration = indicatorVisualization.getChartData();

        chartLabelTextView.setText(indicatorVisualization.getIndicatorLabel());
        if (indicatorVisualization.getIndicatorNote() != null) {
            chartNoteTextView.setText(indicatorVisualization.getIndicatorNote());
        } else {
            // Nothing to show
            chartNoteTextView.setVisibility(View.GONE);
        }

        // Retrieve slice values
        List<SliceValue> sliceValues = new ArrayList<>();
        List<PieChartSlice> chartSlices = chartConfiguration.getSlices();
        SliceValue value;
        boolean showPieChart = false;
        // Check if we have values to show in the chart
        for (PieChartSlice slice : chartSlices) {
            if (slice.getValue() > 0) {
                showPieChart = true;
            }
            value = new SliceValue();
            value.setColor(slice.getColor());
            value.setValue(slice.getValue());
            sliceValues.add(value);
        }

        PieChartView pieChartView = rootLayout.findViewById(R.id.pie_chart);
        if (!showPieChart) {
            pieChartView.setVisibility(View.GONE);
            return rootLayout;
        }

        // We have data to show so this isn't necessary
        numericValueTextView.setVisibility(View.GONE);

        // Retrieve chart data
        PieChartData chartData = new PieChartData();
        chartData.setHasCenterCircle(chartConfiguration.hasCenterCircle());
        chartData.setHasLabels(chartConfiguration.hasLabels());
        chartData.setHasLabelsOutside(chartConfiguration.hasLabelsOutside());
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
