package org.smartregister.reporting.view;

import android.content.Context;
import android.view.View;

import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.domain.NumericIndicatorVisualization;
import org.smartregister.reporting.model.IndicatorDisplayModel;
import org.smartregister.reporting.factory.NumericDisplayFactory;

import static org.smartregister.reporting.util.ReportingUtil.getIndicatorView;


public class NumericIndicatorView implements ReportContract.IndicatorView {

    private Context context;
    private IndicatorDisplayModel indicatorDisplayModel;
    private NumericDisplayFactory numericDisplayFactory;

    public NumericIndicatorView(Context context, IndicatorDisplayModel indicatorDisplayModel) {
        this.context = context;
        this.indicatorDisplayModel = indicatorDisplayModel;
        numericDisplayFactory = new NumericDisplayFactory();
    }

    @Override
    public View createView() {
        return getIndicatorView(getNumericVisualization(), numericDisplayFactory, context);
    }

    private NumericIndicatorVisualization getNumericVisualization() {
        return new NumericIndicatorVisualization(context.getResources().getString(
                indicatorDisplayModel.getLabelStringResource()), (int) indicatorDisplayModel.getTotalCount());
    }
}
