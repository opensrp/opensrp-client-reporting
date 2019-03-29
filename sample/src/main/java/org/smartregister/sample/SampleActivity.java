package org.smartregister.sample;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.reporting.models.NumericIndicatorFactory;
import org.smartregister.reporting.models.NumericIndicatorVisualization;
import org.smartregister.reporting.models.PieChartIndicatorData;
import org.smartregister.reporting.models.PieChartIndicatorFactory;
import org.smartregister.reporting.models.PieChartIndicatorVisualization;
import org.smartregister.reporting.models.PieChartSlice;

import java.util.ArrayList;
import java.util.List;

public class SampleActivity extends AppCompatActivity {

    private View pieChartView;
    private View numericIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity);

        generateCharts();

        ViewGroup viewGroup = findViewById(R.id.content);
        viewGroup.addView(numericIndicatorView);
        viewGroup.addView(pieChartView);
    }

    private void generateCharts() {

        // Generate pie chart
        PieChartIndicatorVisualization pieChartIndicatorVisualization = new PieChartIndicatorVisualization();
        pieChartIndicatorVisualization.setIndicatorLabel(getResources().getString(R.string.num_of_lieterate_children_0_60_label));

        PieChartIndicatorData chartData = new PieChartIndicatorData();
        chartData.setHasLabels(true);
        chartData.setHasLabelsOutside(true);
        chartData.setHasCenterCircle(false);

        List<PieChartSlice> slices = new ArrayList<>();


        PieChartSlice yesSlice = new PieChartSlice(58, Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(), R.color.colorPieChartGreen))));
        PieChartSlice noSlice = new PieChartSlice(42, Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getApplicationContext(), R.color.colorPieChartRed))));

        slices.add(yesSlice);
        slices.add(noSlice);

        chartData.setSlices(slices);

        pieChartIndicatorVisualization.setChartData(chartData);

        PieChartIndicatorFactory pieChartFactory = new PieChartIndicatorFactory();
        pieChartView = pieChartFactory.getIndicatorView(pieChartIndicatorVisualization, this);

        // Generate numeric indicator
        NumericIndicatorVisualization numericIndicatorData = new NumericIndicatorVisualization(
                getResources().getString(R.string.total_under_5_count), 199);

        NumericIndicatorFactory numericIndicatorFactory = new NumericIndicatorFactory();
        numericIndicatorView = numericIndicatorFactory.getIndicatorView(numericIndicatorData, this);
    }
}
