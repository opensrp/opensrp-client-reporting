package org.smartregister.reporting.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import org.smartregister.reporting.R;
import org.smartregister.reporting.models.PieChartIndicatorData;
import org.smartregister.reporting.models.PieChartIndicatorFactory;

import lecho.lib.hellocharts.view.PieChartView;

public class ReportsActivity extends AppCompatActivity {

    private PieChartView chartView;
    private PieChartIndicatorFactory pieChartFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        generateChart();

        chartView.setChartRotationEnabled(false);
        LinearLayout linearLayout = this.findViewById(R.id.content);
        linearLayout.addView(chartView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void generateChart() {
        // TODO :: Use dummy data instead
        PieChartIndicatorData data =
                new PieChartIndicatorData(
                        getResources().getString(R.string.num_of_lieterate_children_0_60_label), 58, 42);
        pieChartFactory = new PieChartIndicatorFactory();
        chartView = (PieChartView) pieChartFactory.getIndicatorView(data, this);
    }

}
