package org.smartregister.reporting.view;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;
import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.domain.PieChartIndicatorDisplayOptions;
import org.smartregister.reporting.domain.PieChartSlice;
import org.smartregister.reporting.factory.PieChartFactory;
import org.smartregister.reporting.listener.PieChartSelectListener;

import timber.log.Timber;

import static org.smartregister.reporting.util.ReportingUtil.getIndicatorView;

public class PieChartIndicatorView implements ReportContract.IndicatorView {

    private Context context;
    private PieChartFactory pieChartFactory;
    PieChartIndicatorDisplayOptions displayOptions;

    public PieChartIndicatorView(Context context, PieChartIndicatorDisplayOptions displayOptions) {
        pieChartFactory = new PieChartFactory();
        this.displayOptions = displayOptions;
        this.context = context;
    }

    /**
     * Generating a pie chart is memory intensive in lower end devices.
     * Allow @java.lang.OutOfMemoryError is recorded in some devices
     *
     * @return view
     */
    @Override
    @Nullable
    public View createView() {
        try {
            if (displayOptions.getPieChartConfig().getListener() == null) {
                displayOptions.getPieChartConfig().setListener(new ChartListener());
            }
            return getIndicatorView(displayOptions, pieChartFactory, context);
        } catch (OutOfMemoryError e) {
            Timber.e(e);
        }

        return null;
    }

    public class ChartListener implements PieChartSelectListener {
        @Override
        public void handleOnSelectEvent(PieChartSlice slice) {
            Toast.makeText(context, slice.getLabel(), Toast.LENGTH_SHORT).show();
        }
    }
}
