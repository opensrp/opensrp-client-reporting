package org.smartregister.sample.presenter;

import org.smartregister.reporting.contract.ReportIndicatorGeneratorContract;
import org.smartregister.reporting.model.BaseReportIndicatorsGeneratorModel;
import org.smartregister.reporting.model.IndicatorQuery;
import org.smartregister.reporting.model.IndicatorTally;
import org.smartregister.reporting.model.ReportIndicator;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public class SamplePresenter implements ReportIndicatorGeneratorContract.Presenter {

    private WeakReference<ReportIndicatorGeneratorContract.View> viewWeakReference;
    private ReportIndicatorGeneratorContract.Model model;

    public SamplePresenter(ReportIndicatorGeneratorContract.View view) {
        this.viewWeakReference = new WeakReference<>(view);
        this.model = new BaseReportIndicatorsGeneratorModel();
    }

    @Override
    public void onResume() {
        getView().refreshUI();
    }

    @Override
    public List<Map<String, IndicatorTally>> fetchIndicatorsDailytallies() {
        return model.getIndicatorsDailyTallies();
    }

    @Override
    public void initialiseIndicator(ReportIndicator indicator) {
        model.addIndicator(indicator);
    }

    @Override
    public void initialiseIndicatorQuery(IndicatorQuery indicatorQuery) {
        model.addIndicatorQuery(indicatorQuery);
    }


    public ReportIndicatorGeneratorContract.View getView() {
        if (viewWeakReference != null) {
            return viewWeakReference.get();
        }
        return null;
    }
}
