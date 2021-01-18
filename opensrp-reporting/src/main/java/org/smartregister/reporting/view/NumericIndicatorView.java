package org.smartregister.reporting.view;

import android.content.Context;
import android.view.View;

import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.domain.NumericIndicatorDisplayOptions;
import org.smartregister.reporting.factory.NumericDisplayFactory;
import org.smartregister.reporting.util.ReportingUtil;


public class NumericIndicatorView implements ReportContract.IndicatorView {

    private Context context;
    private NumericIndicatorDisplayOptions displayOptions;
    private NumericDisplayFactory numericDisplayFactory;

    public NumericIndicatorView(Context context, NumericIndicatorDisplayOptions displayOptions) {
        this.context = context;
        this.displayOptions = displayOptions;
        numericDisplayFactory = new NumericDisplayFactory();
    }

    @Override
    public View createView() {
        return ReportingUtil.getIndicatorView(displayOptions, numericDisplayFactory, context);
    }
}
