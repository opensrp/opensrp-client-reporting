package org.smartregister.sample.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.model.IndicatorDisplayModel;
import org.smartregister.reporting.view.NumericIndicatorView;
import org.smartregister.reporting.view.PieChartIndicatorView;
import org.smartregister.sample.R;
import org.smartregister.sample.presenter.SamplePresenter;
import org.smartregister.sample.utils.ChartUtil;

import java.util.List;
import java.util.Map;

import static org.smartregister.reporting.contract.ReportContract.IndicatorView.CountType.LATEST_COUNT;
import static org.smartregister.reporting.contract.ReportContract.IndicatorView.CountType.STATIC_COUNT;
import static org.smartregister.reporting.util.ReportingUtil.getIndicatorModel;
import static org.smartregister.reporting.util.ReportingUtil.getPieChartViewModel;

public class DashboardFragment extends Fragment implements ReportContract.View, LoaderManager.LoaderCallbacks<List<Map<String, IndicatorTally>>> {

    private static ReportContract.Presenter presenter;
    private ViewGroup visualizationsViewGroup;
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

    private void createSampleReportViews(ViewGroup mainLayout) {
        IndicatorDisplayModel indicator1 = getIndicatorModel(STATIC_COUNT, ChartUtil.numericIndicatorKey, R.string.total_under_5_count, indicatorTallies);
        mainLayout.addView(new NumericIndicatorView(mainLayout.getContext(), indicator1).createView());

        IndicatorDisplayModel indicator2_1 = getIndicatorModel(LATEST_COUNT, ChartUtil.pieChartYesIndicatorKey, R.string.num_of_lieterate_children_0_60_label, indicatorTallies);
        IndicatorDisplayModel indicator2_2 = getIndicatorModel(LATEST_COUNT, ChartUtil.pieChartNoIndicatorKey, R.string.num_of_lieterate_children_0_60_label, indicatorTallies);
        mainLayout.addView(new PieChartIndicatorView(mainLayout.getContext(), getPieChartViewModel(indicator2_1, indicator2_2, null, mainLayout.getContext().getResources().getString(R.string.sample_note))).createView());
    }

    @Override
    public List<Map<String, IndicatorTally>> getIndicatorTallies() {
        return indicatorTallies;
    }

    @Override
    public void setIndicatorTallies(List<Map<String, IndicatorTally>> indicatorTallies) {
        this.indicatorTallies = indicatorTallies;
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
