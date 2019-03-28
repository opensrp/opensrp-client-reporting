package org.smartregister.reporting.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.smartregister.reporting.R;
import org.smartregister.reporting.interfaces.CommonReportingVisualisationListener;
import org.smartregister.reporting.interfaces.IndicatorVisualisationFactory;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class PieChartIndicatorFactory implements IndicatorVisualisationFactory {
    @Override
    public View getIndicatorView(ReportingIndicatorData data, Context context, CommonReportingVisualisationListener listener) {

        PieChartIndicatorData indicatorData = (PieChartIndicatorData) data;

        LinearLayout rootLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.pie_chart_view, null);

        TextView chartLabelTextView = rootLayout.findViewById(R.id.pie_indicator_label);
        chartLabelTextView.setText(indicatorData.getIndicatorLabel());

        PieChartView pieChartView = rootLayout.findViewById(R.id.pie_chart);

        List<SliceValue> values = new ArrayList<>();

        SliceValue yesValue = new SliceValue(indicatorData.getYesValue(), ChartUtils.COLOR_GREEN);
        SliceValue noValue = new SliceValue(indicatorData.getNoValue(), ChartUtils.COLOR_RED);

        values.add(yesValue);
        values.add(noValue);

        // TODO :: Defaults -> Does this need configurability
        PieChartData chartData = new PieChartData(values);
        chartData.setHasLabels(true);
        chartData.setHasLabelsOutside(true);
        chartData.setHasCenterCircle(false);
        pieChartView.setChartRotationEnabled(false);
        pieChartView.setCircleFillRatio(0.7f);
        pieChartView.setPieChartData(chartData);
        pieChartView.setOnValueTouchListener(listener);

        return rootLayout;
    }
}
