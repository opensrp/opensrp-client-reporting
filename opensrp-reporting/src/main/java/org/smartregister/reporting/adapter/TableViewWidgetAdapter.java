package org.smartregister.reporting.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.smartregister.reporting.R;
import org.smartregister.reporting.view.TableView;

import java.util.List;

/**
 * Created by ndegwamartin on 2019-09-19.
 */
public class TableViewWidgetAdapter extends RecyclerView.Adapter<TableViewWidgetAdapter.ViewHolder> {

    private List<String> tableDataList;
    private List<String> tableDataIdList;
    private Context context;
    private TableviewDatatype dataType;
    private TableView.TextViewStyle style;
    private View.OnClickListener rowClickListener;
    private static Integer headerCount;

    public enum TableviewDatatype {
        HEADER, BODY, FOOTER

    }

    public TableViewWidgetAdapter(List<String> tableDataList, List<String> tableDataIdList, Context context, TableviewDatatype dataType, TableView.TextViewStyle style, View.OnClickListener rowClickListener) {
        this.tableDataList = tableDataList;
        this.tableDataIdList = tableDataIdList;
        this.context = context;
        this.dataType = dataType;
        this.style = style;
        this.rowClickListener = rowClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.table_view_cell, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(tableDataList.get(position));

        if (style.rowTextColor != 0) {
            holder.textView.setTextColor(style.rowTextColor);
        }

        if (dataType.equals(TableviewDatatype.HEADER)) {
            // For headers only

            holder.textView.setAllCaps(true);

            TextViewCompat.setTextAppearance(holder.textView, R.style.TextAppearance_AppCompat_Medium);
            holder.textView.setTypeface(holder.textView.getTypeface(), style.headerTextStyle);

            holder.textView.setTextColor(style.headerTextColor != 0 ? style.headerTextColor : context.getResources().getColor(R.color.white));
            holder.textView.setGravity(Gravity.CENTER_VERTICAL);

            if (style.headerBackgroundColor != 0) {
                holder.textView.setBackgroundColor(style.headerBackgroundColor);
            }

            if (headerCount == null && tableDataList.size() > 0) {
                headerCount = tableDataList.size();
            }

        } else {

            //For rows only
            if (style.isRowBorderHidden) {
                holder.textView.setBackground(null);
            }
            //Bind row click listener
            if (rowClickListener != null && tableDataIdList != null) {
                holder.textView.setOnClickListener(rowClickListener);
                holder.textView.setTag(R.id.table_row_id, getRowId(position));
            }

        }

        if (style.borderColor != 0) {
            setTableViewBorderColor(holder.textView, style.borderColor);
        }
    }

    private String getRowId(int position) {
        Double idPosition = Math.floor(position / headerCount);
        return tableDataIdList.get(idPosition.intValue());
    }

    @Override
    public int getItemCount() {
        return tableDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.cellTextView);
        }
    }

    private void setTableViewBorderColor(View view, int borderColor) {

        if (borderColor != 0 && view.findViewById(R.id.cellTextView).getBackground() instanceof LayerDrawable) {
            LayerDrawable parentDrawable = (LayerDrawable) view.findViewById(R.id.cellTextView).getBackground().mutate();
            GradientDrawable tableBackgroundDrawable = (GradientDrawable) parentDrawable.getDrawable(0).mutate();
            tableBackgroundDrawable.setColor(borderColor);
        }

    }
}