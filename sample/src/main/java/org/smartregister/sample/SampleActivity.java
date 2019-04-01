package org.smartregister.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.smartregister.reporting.interfaces.PieChartSelectListener;
import org.smartregister.reporting.model.NumericDisplayFactory;
import org.smartregister.reporting.model.NumericIndicatorVisualization;
import org.smartregister.reporting.model.PieChartIndicatorData;
import org.smartregister.reporting.model.PieChartFactory;
import org.smartregister.reporting.model.PieChartIndicatorVisualization;
import org.smartregister.reporting.model.PieChartSlice;
import org.smartregister.sample.utils.ChartUtil;

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

        int yesColor = ChartUtil.YES_GREEN_SLICE_COLOR;
        int noColor = ChartUtil.NO_RED_SLICE_COLOR;
        PieChartSlice yesSlice = new PieChartSlice(58, yesColor);
        PieChartSlice noSlice = new PieChartSlice(42, noColor);

        slices.add(yesSlice);
        slices.add(noSlice);

        chartData.setSlices(slices);
        chartData.setListener(new ChartListener());

        pieChartIndicatorVisualization.setChartData(chartData);

        PieChartFactory pieChartFactory = new PieChartFactory();
        pieChartView = pieChartFactory.getIndicatorView(pieChartIndicatorVisualization, this);

        // Generate numeric indicator
        NumericIndicatorVisualization numericIndicatorData = new NumericIndicatorVisualization(
                getResources().getString(R.string.total_under_5_count), 199);

        NumericDisplayFactory numericIndicatorFactory = new NumericDisplayFactory();
        numericIndicatorView = numericIndicatorFactory.getIndicatorView(numericIndicatorData, this);
    }

    private class ChartListener implements PieChartSelectListener {

        @Override
        public void handleOnSelectEvent(PieChartSlice sliceValue) {
            Toast.makeText(getApplicationContext(), ChartUtil.getPieSelectionValue(sliceValue, getApplicationContext()), Toast.LENGTH_SHORT).show();
        }
    }


}
