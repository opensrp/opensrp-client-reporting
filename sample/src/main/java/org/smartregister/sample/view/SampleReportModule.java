package org.smartregister.sample.view;

import android.view.ViewGroup;

import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.impl.ReportingModule;
import org.smartregister.reporting.impl.models.IndicatorModel;
import org.smartregister.reporting.impl.views.NumericIndicatorView;
import org.smartregister.reporting.impl.views.PieChartIndicatorView;
import org.smartregister.sample.R;
import org.smartregister.sample.utils.ChartUtil;

import java.util.List;
import java.util.Map;

import static org.smartregister.reporting.impl.ReportingUtil.getIndicatorModel;
import static org.smartregister.reporting.impl.ReportingUtil.getPieChartViewModel;
import static org.smartregister.reporting.impl.views.IndicatorView.CountType.LATEST_COUNT;
import static org.smartregister.reporting.impl.views.IndicatorView.CountType.STATIC_COUNT;
import static org.smartregister.reporting.impl.views.IndicatorViewFactory.createView;

public class SampleReportModule implements ReportingModule {

    private List<Map<String, IndicatorTally>> indicatorTallies;

    @Override
    public void generateReport(ViewGroup mainLayout) {
        IndicatorModel indicator1 = getIndicatorModel(STATIC_COUNT, ChartUtil.numericIndicatorKey, R.string.total_under_5_count, indicatorTallies);
        mainLayout.addView(createView(new NumericIndicatorView(mainLayout.getContext(), indicator1)));

        IndicatorModel indicator2_1 = getIndicatorModel(LATEST_COUNT, ChartUtil.pieChartYesIndicatorKey, R.string.num_of_lieterate_children_0_60_label, indicatorTallies);
        IndicatorModel indicator2_2 = getIndicatorModel(LATEST_COUNT, ChartUtil.pieChartNoIndicatorKey, R.string.num_of_lieterate_children_0_60_label, indicatorTallies);
        mainLayout.addView(createView(new PieChartIndicatorView(mainLayout.getContext(), getPieChartViewModel(indicator2_1, indicator2_2, null, mainLayout.getContext().getResources().getString(R.string.sample_note)))));

    }

    @Override
    public List<Map<String, IndicatorTally>> getIndicatorTallies() {
        return indicatorTallies;
    }

    @Override
    public void setIndicatorTallies(List<Map<String, IndicatorTally>> indicatorTallies) {
        this.indicatorTallies = indicatorTallies;
    }

}
