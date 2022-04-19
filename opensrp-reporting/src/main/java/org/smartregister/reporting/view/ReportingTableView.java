package org.smartregister.reporting.view;

import static org.smartregister.reporting.util.ReportingUtil.getIndicatorView;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;

import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.domain.TabularVisualization;
import org.smartregister.reporting.factory.TableDisplayFactory;

public class ReportingTableView implements ReportContract.IndicatorView{

    private Context context;
    private TableDisplayFactory tableDisplayFactory;
    private TabularVisualization tabularVisualization;

    public ReportingTableView(Context context, TabularVisualization visualization) {
        this.context = context;
        this.tabularVisualization = visualization;
        tableDisplayFactory = new TableDisplayFactory();
    }

    @Nullable
    @Override
    public View createView() {
        return getIndicatorView(this.tabularVisualization, tableDisplayFactory, context);

    }
}
