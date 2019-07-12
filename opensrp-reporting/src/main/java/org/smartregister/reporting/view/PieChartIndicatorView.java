package org.smartregister.reporting.view;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.domain.PieChartIndicatorVisualization;
import org.smartregister.reporting.domain.PieChartSlice;
import org.smartregister.reporting.factory.PieChartFactory;
import org.smartregister.reporting.listener.PieChartSelectListener;
import org.smartregister.reporting.model.PieChartDisplayModel;
import org.smartregister.reporting.util.ReportingUtil;

import java.util.ArrayList;
import java.util.List;

import static org.smartregister.reporting.util.ReportingUtil.getIndicatorView;
import static org.smartregister.reporting.util.ReportingUtil.getPieSelectionValue;


public class PieChartIndicatorView implements ReportContract.IndicatorView {

    private Context context;
    private PieChartFactory pieChartFactory;
    private PieChartDisplayModel pieChartDisplayModel;

    public PieChartIndicatorView(Context context, PieChartDisplayModel pieChartDisplayModel) {
        pieChartFactory = new PieChartFactory();
        this.pieChartDisplayModel = pieChartDisplayModel;
        this.context = context;
    }

    @Override
    public View createView() {
        PieChartIndicatorVisualization pieChartIndicatorVisualization = getPieChartVisualization();

        if (pieChartDisplayModel.getIndicatorLabel() != null) {
            pieChartIndicatorVisualization.setIndicatorLabel(pieChartDisplayModel.getIndicatorLabel());
        }

        if (pieChartDisplayModel.getIndicatorNote() != null) {
            pieChartIndicatorVisualization.setIndicatorNote(pieChartDisplayModel.getIndicatorNote());
        }

        return getIndicatorView(pieChartIndicatorVisualization, pieChartFactory, context);
    }

    private PieChartIndicatorVisualization getPieChartVisualization() {
        // Define pie chart chartSlices
        List<PieChartSlice> chartSlices = new ArrayList<>();
        int yesCount = (int) pieChartDisplayModel.getYesSlice().getTotalCount();
        int noCount = (int) pieChartDisplayModel.getNoSlice().getTotalCount();
        chartSlices.add(new PieChartSlice(yesCount, ReportingUtil.YES_GREEN_SLICE_COLOR));
        chartSlices.add(new PieChartSlice(noCount, ReportingUtil.NO_RED_SLICE_COLOR));
        // Build the chart
        return new PieChartIndicatorVisualization.PieChartIndicatorVisualizationBuilder()
                .indicatorLabel(context.getResources().getString(pieChartDisplayModel.getYesSlice().getLabelStringResource()))
                .chartHasLabels(true)
                .chartHasLabelsOutside(true)
                .chartHasCenterCircle(false)
                .chartSlices(chartSlices)
                .chartListener(new ChartListener()).build();
    }

    public class ChartListener implements PieChartSelectListener {
        @Override
        public void handleOnSelectEvent(PieChartSlice sliceValue) {
            Toast.makeText(context, getPieSelectionValue(sliceValue, context),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
