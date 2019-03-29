package org.smartregister.reporting.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.smartregister.reporting.R;
import org.smartregister.reporting.interfaces.CommonReportingVisualisationListener;
import org.smartregister.reporting.models.NumericIndicatorVisualization;
import org.smartregister.reporting.models.NumericIndicatorFactory;
import org.smartregister.reporting.models.PieChartIndicatorVisualization;
import org.smartregister.reporting.models.PieChartIndicatorFactory;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;

public class ReportsActivity extends AppCompatActivity {

    private View pieChartView;
    private View numericIndicatorView;
    private PieChartIndicatorFactory pieChartFactory;
    private NumericIndicatorFactory numericIndicatorFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        generateCharts();

        ViewGroup groupLayout = (ViewGroup) this.findViewById(R.id.content);
        groupLayout.addView(numericIndicatorView);
        groupLayout.addView(pieChartView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void generateCharts() {
        // TODO :: Use dummy data instead

        // Generate pie chart
        PieChartIndicatorVisualization pieChartIndicatorVisualization = new PieChartIndicatorVisualization();
        pieChartIndicatorVisualization.setIndicatorLabel(getResources().getString(R.string.num_of_lieterate_children_0_60_label));

        PieChartData chartData = new PieChartData();
        chartData.setHasLabels(true);
        chartData.setHasLabelsOutside(true);
        chartData.setHasCenterCircle(false);

        List<SliceValue> slices = new ArrayList<>();

        SliceValue yesSlice = new SliceValue(58, ChartUtils.COLOR_GREEN);
        SliceValue noSlice = new SliceValue(42, ChartUtils.COLOR_RED);

        slices.add(yesSlice);
        slices.add(noSlice);

        chartData.setValues(slices);

        pieChartIndicatorVisualization.setChartData(chartData);

        pieChartFactory = new PieChartIndicatorFactory();
        pieChartView = pieChartFactory.getIndicatorView(pieChartIndicatorVisualization, this, new PieChartOnValueTouchListener());

        // Generate numeric indicator
        NumericIndicatorVisualization numericIndicatorData = new NumericIndicatorVisualization(
                getResources().getString(R.string.total_under_5_count), 199);

        numericIndicatorFactory = new NumericIndicatorFactory();
        numericIndicatorView = numericIndicatorFactory.getIndicatorView(numericIndicatorData, this, null);
    }

    private class PieChartOnValueTouchListener implements CommonReportingVisualisationListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            Toast.makeText(getApplicationContext(), getSelection(value), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {

        }

        // TODO :: Does this helper method need to exist somewhere else?
        private String getSelection(SliceValue value) {
            String selection = "";
            if (value.getColor() == ChartUtils.COLOR_RED) {
                selection = "No";
            } else if (value.getColor() == ChartUtils.COLOR_GREEN) {
                selection = "Yes";
            }
            return selection;
        }
    }

}
