package org.smartregister.reporting.adapter;

import android.content.Context;
import android.graphics.Typeface;
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

    private List<String> list;
    private Context context;
    private TABLEVIEW_DATATYPE dataType;
    private TableView.TextViewStyle style;

    public enum TABLEVIEW_DATATYPE {
        HEADER, BODY, FOOTER

    }

    public TableViewWidgetAdapter(List<String> list, Context context, TABLEVIEW_DATATYPE dataType, TableView.TextViewStyle style) {
        this.list = list;
        this.context = context;
        this.dataType = dataType;
        this.style = style;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tableview_cell, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(list.get(position));

        if (style.rowTextColor != 0) {
            holder.textView.setTextColor(style.rowTextColor);
        }

        if (dataType.equals(TABLEVIEW_DATATYPE.HEADER)) {

            holder.textView.setAllCaps(true);

            TextViewCompat.setTextAppearance(holder.textView, R.style.TextAppearance_AppCompat_Medium);
            holder.textView.setTypeface(holder.textView.getTypeface(), style.headerTextStyle);

            holder.textView.setTextColor(style.headerTextColor != 0 ? style.headerTextColor : context.getResources().getColor(R.color.white));
            holder.textView.setGravity(Gravity.CENTER_VERTICAL);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.cellTextView);
        }
    }
}