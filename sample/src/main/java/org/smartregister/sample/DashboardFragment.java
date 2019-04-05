package org.smartregister.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.smartregister.reporting.listener.PieChartSelectListener;
import org.smartregister.reporting.view.NumericDisplayFactory;
import org.smartregister.reporting.model.NumericIndicatorVisualization;
import org.smartregister.reporting.view.PieChartFactory;
import org.smartregister.reporting.model.PieChartIndicatorData;
import org.smartregister.reporting.model.PieChartIndicatorVisualization;
import org.smartregister.reporting.model.PieChartSlice;
import org.smartregister.sample.utils.ChartUtil;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private View pieChartView;
    private View numericIndicatorView;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generateCharts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup viewGroup = getView().findViewById(R.id.dashboard_content);
        viewGroup.addView(numericIndicatorView);
        viewGroup.addView(pieChartView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        pieChartView = pieChartFactory.getIndicatorView(pieChartIndicatorVisualization, getContext());

        // Generate numeric indicator
        NumericIndicatorVisualization numericIndicatorData = new NumericIndicatorVisualization(
                getResources().getString(R.string.total_under_5_count), 199);

        NumericDisplayFactory numericIndicatorFactory = new NumericDisplayFactory();
        numericIndicatorView = numericIndicatorFactory.getIndicatorView(numericIndicatorData, getContext());
    }

    private class ChartListener implements PieChartSelectListener {

        @Override
        public void handleOnSelectEvent(PieChartSlice sliceValue) {
            Toast.makeText(getContext(), ChartUtil.getPieSelectionValue(sliceValue, getContext()), Toast.LENGTH_SHORT).show();
        }
    }
}
