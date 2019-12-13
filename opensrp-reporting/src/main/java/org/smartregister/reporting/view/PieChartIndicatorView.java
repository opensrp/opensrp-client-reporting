package org.smartregister.reporting.view;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;
import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.domain.PieChartIndicatorVisualization;
import org.smartregister.reporting.domain.PieChartSlice;
import org.smartregister.reporting.factory.PieChartFactory;
import org.smartregister.reporting.listener.PieChartSelectListener;
import org.smartregister.reporting.model.PieChartDisplayModel;

import timber.log.Timber;

import static org.smartregister.reporting.util.ReportingUtil.getIndicatorView;

public class PieChartIndicatorView implements ReportContract.IndicatorView {

    private Context context;
    private PieChartFactory pieChartFactory;
    private PieChartDisplayModel pieChartDisplayModel;

    public PieChartIndicatorView(Context context, PieChartDisplayModel pieChartDisplayModel) {
        pieChartFactory = new PieChartFactory();
        this.pieChartDisplayModel = pieChartDisplayModel;
        this.context = context;
    }

    /**
     * Generating a pie chart is memory intensive in lower end devices.
     * Allow @java.lang.OutOfMemoryError is recorded in some devices
     * @return view
     */
    @Override
    @Nullable
    public View createView() {
        try {
            PieChartIndicatorVisualization pieChartIndicatorVisualization = getPieChartVisualization();

            if (pieChartDisplayModel.getIndicatorLabel() != null) {
                pieChartIndicatorVisualization.setIndicatorLabel(context.getResources().getString(pieChartDisplayModel.getIndicatorLabel()));
            }

            if (pieChartDisplayModel.getIndicatorNote() != null) {
                pieChartIndicatorVisualization.setIndicatorNote(context.getResources().getString(pieChartDisplayModel.getIndicatorNote()));
            }
            return getIndicatorView(pieChartIndicatorVisualization, pieChartFactory, context);
        }catch (OutOfMemoryError e){
            Timber.e(e);
        }

        return null;
    }

    private PieChartIndicatorVisualization getPieChartVisualization() {
        // Build the chart
        String pieChartLabel = "No label provided"; //to avoid crash when string resource not provide
        if (pieChartDisplayModel.getIndicatorLabel() != null) {
            pieChartLabel = context.getResources().getString(pieChartDisplayModel.getIndicatorLabel());
        }
        return new PieChartIndicatorVisualization.PieChartIndicatorVisualizationBuilder()
                .indicatorLabel(pieChartLabel)
                .chartHasLabels(true)
                .chartHasLabelsOutside(true)
                .chartHasCenterCircle(false)
                .chartSlices(pieChartDisplayModel.getPieChartSlices())
                .chartListener(new ChartListener()).build();
    }

    public class ChartListener implements PieChartSelectListener {
        @Override
        public void handleOnSelectEvent(PieChartSlice slice) {
            Toast.makeText(context, slice.getLabel(), Toast.LENGTH_SHORT).show();
        }
    }
}
