package org.smartregister.reporting.view;

import android.content.Context;
import android.view.View;

import org.jetbrains.annotations.Nullable;
import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.factory.ProgressIndicatorFactory;
import org.smartregister.reporting.domain.ProgressIndicatorDisplayOptions;
import org.smartregister.reporting.util.ReportingUtil;

public class ProgressIndicatorView implements ReportContract.IndicatorView {

    private Context context;
    private ProgressIndicatorDisplayOptions displayOptions;
    private ProgressIndicatorFactory progressIndicatorFactory;


    public ProgressIndicatorView(Context context, ProgressIndicatorDisplayOptions displayOptions) {
        this.progressIndicatorFactory = new ProgressIndicatorFactory();
        this.context = context;
        this.displayOptions = displayOptions;
    }

    @Nullable
    @Override
    public View createView() {
        return ReportingUtil.getIndicatorView(displayOptions, progressIndicatorFactory, context);
    }

}
