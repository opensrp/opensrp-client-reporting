package org.smartregister.reporting.factory;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import org.smartregister.reporting.R;
import org.smartregister.reporting.domain.ReportingIndicatorVisualization;
import org.smartregister.reporting.domain.TabularVisualization;
import org.smartregister.reporting.view.TableView;

import java.util.ArrayList;

public class TableDisplayFactory implements IndicatorVisualisationFactory {

    @Override
    public View getIndicatorView(ReportingIndicatorVisualization visualization, Context context) {
        LinearLayout rootLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.table_view_layout, null);
        TextView tableTitle = rootLayout.findViewById(R.id.tableViewTitle);
        TableView tableView = rootLayout.findViewById(R.id.tableView);

        TabularVisualization tabularVisualization = (TabularVisualization) visualization;

        tableTitle.setText(tabularVisualization.getTitleLabelStringResource());
        tableView.setTableData(tabularVisualization.getTableHeaderList(), tabularVisualization.getTableDataList(), new ArrayList<>(), null);
        tableView.setHeaderTextStyle(Typeface.BOLD);
        tableView.setRowBorderHidden(false);
        tableView.setBorderColor(ContextCompat.getColor(context, R.color.light_grey)); // Default
        tableView.setTableHeaderHidden(tabularVisualization.isTableHeaderHidden());
        return rootLayout;
    }
}
