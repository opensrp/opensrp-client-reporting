package org.smartregister.reporting.view;

import android.content.Context;
import android.view.View;

import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.domain.NumericIndicatorVisualization;
import org.smartregister.reporting.factory.NumericDisplayFactory;
import org.smartregister.reporting.model.NumericDisplayModel;

import static org.smartregister.reporting.util.ReportingUtil.getIndicatorView;


public class NumericIndicatorView implements ReportContract.IndicatorView {

    private Context context;
    private NumericDisplayModel numericDisplayModel;
    private NumericDisplayFactory numericDisplayFactory;

    public NumericIndicatorView(Context context, NumericDisplayModel numericDisplayModel) {
        this.context = context;
        this.numericDisplayModel = numericDisplayModel;
        numericDisplayFactory = new NumericDisplayFactory();
    }

    @Override
    public View createView() {
        return getIndicatorView(getNumericVisualization(), numericDisplayFactory, context);
    }

    private NumericIndicatorVisualization getNumericVisualization() {
        return new NumericIndicatorVisualization(context.getResources().getString(
                numericDisplayModel.getLabelStringResource()),  numericDisplayModel.getCount());
    }
}
