package org.smartregister.reporting.models;

import android.content.Context;
import android.view.View;

import org.smartregister.reporting.interfaces.IndicatorVisualisationFactory;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class PieChartIndicatorFactory implements IndicatorVisualisationFactory {
    @Override
    public View getIndicatorView(ReportingIndicatorData data, Context context) {

        PieChartIndicatorData indicatorData = (PieChartIndicatorData) data;
        PieChartView pieChartView = new PieChartView(context);

        List<SliceValue> values = new ArrayList<>();

        SliceValue yesValue = new SliceValue(indicatorData.getYesValue(), ChartUtils.COLOR_GREEN);
        SliceValue noValue = new SliceValue(indicatorData.getNoValue(), ChartUtils.COLOR_RED);

        values.add(yesValue);
        values.add(noValue);

        PieChartData chartData = new PieChartData(values);
        // TODO :: Make this configurable
        chartData.setHasLabels(true);
        chartData.setHasLabelsOutside(true);
        chartData.setHasCenterCircle(false);
        // TODO :: Handle adding listener
        pieChartView.setPieChartData(chartData);

        return pieChartView;
    }
}
