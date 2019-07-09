package org.smartregister.reporting.impl.views;

import android.content.Context;
import android.view.View;

import org.smartregister.reporting.domain.NumericIndicatorVisualization;
import org.smartregister.reporting.impl.models.IndicatorModel;
import org.smartregister.reporting.view.NumericDisplayFactory;

import static org.smartregister.reporting.impl.ReportingUtil.getIndicatorView;


public class NumericIndicatorView implements IndicatorView {

    private Context context;
    private IndicatorModel indicatorModel;
    private NumericDisplayFactory numericDisplayFactory;

    public NumericIndicatorView(Context context, IndicatorModel indicatorModel) {
        this.context = context;
        this.indicatorModel = indicatorModel;
        numericDisplayFactory = new NumericDisplayFactory();
    }

    @Override
    public View createView() {
        return getIndicatorView(getNumericVisualization(), numericDisplayFactory, context);
    }

    private NumericIndicatorVisualization getNumericVisualization() {
        return new NumericIndicatorVisualization(context.getResources().getString(
                indicatorModel.getLabelStringResource()), (int) indicatorModel.getTotalCount());
    }
}
