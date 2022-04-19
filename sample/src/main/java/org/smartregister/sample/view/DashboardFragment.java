package org.smartregister.sample.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.domain.PieChartSlice;
import org.smartregister.reporting.domain.TabularVisualization;
import org.smartregister.reporting.model.NumericDisplayModel;
import org.smartregister.reporting.view.NumericIndicatorView;
import org.smartregister.reporting.view.PieChartIndicatorView;
import org.smartregister.reporting.view.ReportingTableView;
import org.smartregister.sample.R;
import org.smartregister.sample.presenter.SamplePresenter;
import org.smartregister.sample.utils.ChartUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.smartregister.reporting.contract.ReportContract.IndicatorView.CountType.LATEST_COUNT;
import static org.smartregister.reporting.contract.ReportContract.IndicatorView.CountType.TOTAL_COUNT;
import static org.smartregister.reporting.util.ReportingUtil.addPieChartSlices;
import static org.smartregister.reporting.util.ReportingUtil.getIndicatorDisplayModel;
import static org.smartregister.reporting.util.ReportingUtil.getPieChartDisplayModel;
import static org.smartregister.reporting.util.ReportingUtil.getPieChartSlice;

public class DashboardFragment extends Fragment implements ReportContract.View, LoaderManager.LoaderCallbacks<List<Map<String, IndicatorTally>>> {

    private static ReportContract.Presenter presenter;
    private ViewGroup visualizationsViewGroup;
    private List<Map<String, IndicatorTally>> indicatorTallies;
    Random random = new Random();

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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // fetch Indicator data
        presenter = new SamplePresenter(this);
        presenter.scheduleRecurringTallyJob();
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
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void refreshUI() {
        buildVisualization(visualizationsViewGroup);
    }

    @Override
    public void buildVisualization(ViewGroup mainLayout) {
        mainLayout.removeAllViews();
        createSampleReportViews(mainLayout);
    }

    @Override
    public List<Map<String, IndicatorTally>> getIndicatorTallies() {
        return indicatorTallies;
    }

    @Override
    public void setIndicatorTallies(List<Map<String, IndicatorTally>> indicatorTallies) {
        this.indicatorTallies = indicatorTallies;
    }

    private void createSampleReportViews(ViewGroup mainLayout) {
        NumericDisplayModel indicator1 = getIndicatorDisplayModel(TOTAL_COUNT, ChartUtil.numericIndicatorKey, R.string.total_under_5_count, indicatorTallies);
        mainLayout.addView(new NumericIndicatorView(getContext(), indicator1).createView());

        PieChartSlice indicator2_1 = getPieChartSlice(LATEST_COUNT, ChartUtil.pieChartYesIndicatorKey, getResources().getString(R.string.yes_slice_label), getResources().getColor(R.color.colorPieChartGreen), indicatorTallies);
        PieChartSlice indicator2_2 = getPieChartSlice(LATEST_COUNT, ChartUtil.pieChartNoIndicatorKey, getResources().getString(R.string.no_button_label), getResources().getColor(R.color.colorPieChartRed), indicatorTallies);
        mainLayout.addView(new PieChartIndicatorView(getContext(), getPieChartDisplayModel(addPieChartSlices(indicator2_1, indicator2_2), R.string.num_of_lieterate_children_0_60_label, R.string.sample_note, null)).createView());

        NumericDisplayModel floatIndicatorCount = getIndicatorDisplayModel(TOTAL_COUNT, "S_IND_005", R.string.float_count, indicatorTallies);
        mainLayout.addView(new NumericIndicatorView(getContext(), floatIndicatorCount).createView());

        int tableTitleStringResource = R.string.table_data_title;
        List<String> tableHeaderList = Arrays.asList("Vaccine Name", "Gender", "Value");
        TabularVisualization tabularVisualization = new TabularVisualization(tableTitleStringResource, tableHeaderList, getDummyData(), true);
        mainLayout.addView(new ReportingTableView(getContext(), tabularVisualization).createView());
    }

    private List<String> getDummyData() {
        List<String> list = new ArrayList<>();
        for (Integer i = 0; i < 10; i++) {
            list.add((i < 3 ? "BCG " : i > 6 ? "OPV " : "PENTA ") + i);
            list.add(getRange(1, 2) == 2 ? "Female" : "Male");
            list.add(String.valueOf(getRange(500, 3000)));
        }
        return list;
    }

    private List<String> getDummyDataIds() {
        List<String> list = new ArrayList<>();
        for (Integer i = 0; i < 10; i++) {
            list.add(UUID.randomUUID().toString());
        }
        return list;
    }

    private int getRange(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    @NonNull
    @Override
    public Loader<List<Map<String, IndicatorTally>>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new ReportIndicatorsLoader(getContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Map<String, IndicatorTally>>> loader, List<Map<String, IndicatorTally>> indicatorTallies) {
        setIndicatorTallies(indicatorTallies);
        refreshUI();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Map<String, IndicatorTally>>> loader) {
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
