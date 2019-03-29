package org.smartregister.reporting.models;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.smartregister.reporting.R;
import org.smartregister.reporting.interfaces.CommonReportingVisualisationListener;
import org.smartregister.reporting.interfaces.IndicatorVisualisationFactory;

import lecho.lib.hellocharts.view.PieChartView;

public class PieChartIndicatorFactory implements IndicatorVisualisationFactory {
    @Override
    public View getIndicatorView(ReportingIndicatorVisualization visualization, Context context, CommonReportingVisualisationListener listener) {

        PieChartIndicatorVisualization indicatorVisualization = (PieChartIndicatorVisualization) visualization;

        ConstraintLayout rootLayout = (ConstraintLayout) LayoutInflater.from(context).inflate(R.layout.pie_chart_view, null);

        TextView chartLabelTextView = rootLayout.findViewById(R.id.pie_indicator_label);
        chartLabelTextView.setText(indicatorVisualization.getIndicatorLabel());

        PieChartView pieChartView = rootLayout.findViewById(R.id.pie_chart);

        pieChartView.setChartRotationEnabled(false);
        pieChartView.setCircleFillRatio(0.8f);
        pieChartView.setPieChartData(indicatorVisualization.getChartData());
        pieChartView.setOnValueTouchListener(listener);

        return rootLayout;
    }
}
