package org.smartregister.sample.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.smartregister.reporting.contract.ReportIndicatorGeneratorContract;
import org.smartregister.reporting.listener.PieChartSelectListener;
import org.smartregister.reporting.model.IndicatorTally;
import org.smartregister.reporting.view.NumericDisplayFactory;
import org.smartregister.reporting.model.NumericIndicatorVisualization;
import org.smartregister.reporting.view.PieChartFactory;
import org.smartregister.reporting.model.PieChartIndicatorData;
import org.smartregister.reporting.model.PieChartIndicatorVisualization;
import org.smartregister.reporting.model.PieChartSlice;
import org.smartregister.sample.R;
import org.smartregister.sample.presenter.SamplePresenter;
import org.smartregister.sample.utils.ChartUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment implements ReportIndicatorGeneratorContract.View, LoaderManager.LoaderCallbacks<List<Map<String, IndicatorTally>>> {

    private ViewGroup visualizationsViewGroup;
    private View pieChartView;
    private View numericIndicatorView;
    private static ReportIndicatorGeneratorContract.Presenter presenter;
    private List<Map<String, IndicatorTally>> indicatorTallies;

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
        // fetch Indicator data
        presenter = new SamplePresenter(this);
        getLoaderManager().initLoader(0, null, this).forceLoad();
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
        visualizationsViewGroup = getView().findViewById(R.id.dashboard_content);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void buildVisualisations() {

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

    @Override
    public void refreshUI() {
        buildVisualisations();
        visualizationsViewGroup.addView(numericIndicatorView);
        visualizationsViewGroup.addView(pieChartView);
    }

    @NonNull
    @Override
    public Loader<List<Map<String, IndicatorTally>>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new ReportIndicatorsLoader(getContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Map<String, IndicatorTally>>> loader, List<Map<String, IndicatorTally>> indicatorTallies) {
        indicatorTallies = indicatorTallies;
        refreshUI();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Map<String, IndicatorTally>>> loader) {
    }

    private class ChartListener implements PieChartSelectListener {

        @Override
        public void handleOnSelectEvent(PieChartSlice sliceValue) {
            Toast.makeText(getContext(), ChartUtil.getPieSelectionValue(sliceValue, getContext()), Toast.LENGTH_SHORT).show();
        }
    }

    private static class ReportIndicatorsLoader extends AsyncTaskLoader<List<Map<String, IndicatorTally>>> {

        public ReportIndicatorsLoader(Context context) {
            super(context);
        }

        @Nullable
        @Override
        public List<Map<String, IndicatorTally>> loadInBackground() {
            return presenter.fetchIndicatorsDailytallies();
        }
    }
}
