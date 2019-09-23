package org.smartregister.reporting.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import org.smartregister.reporting.R;
import org.smartregister.reporting.adapter.TableViewWidgetAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ndegwamartin on 2019-09-20.
 */
public class TableView extends LinearLayout {

    private List<String> tableData = new ArrayList<>();
    private List<String> tableHeaderData = new ArrayList<>();

    private AttributeSet attrs;

    public TableView(Context context) {
        super(context);
        initView();

    }

    public TableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        setupAttributes(attrs);
    }

    public TableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        setupAttributes(attrs);
    }

    @TargetApi(android.os.Build.VERSION_CODES.LOLLIPOP)
    public TableView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
        setupAttributes(attrs);
    }

    /**
     * Set init configs for parent layout view
     */
    private void initView() {
        inflate(getContext(), R.layout.table_view, this);
        setGravity(Gravity.LEFT);
        setOrientation(VERTICAL);
        setBackground(ContextCompat.getDrawable(getContext(), (R.drawable.table_view_border)));
        //setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    /**
     * @param attrs an attribute set styled attributes from theme
     */

    private void setupAttributes(AttributeSet attrs) {
        this.attrs = attrs;

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TableView, 0, 0);

        try {

            resetHeaderLayoutParams(typedArray);
            resetLayoutParams(typedArray);

        } finally {

            typedArray.recycle();// We must recycle TypedArray objects as they are shared
        }
    }

    private void resetHeaderLayoutParams(TypedArray typedArray) {

        RecyclerView recycler = findViewById(R.id.tableViewHeaderRecyclerViewGridLayout);
        recycler.setHasFixedSize(true);

        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), getColumnCount(), GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);

        RecyclerView.Adapter adapter = new TableViewWidgetAdapter(tableHeaderData, getContext(), TableViewWidgetAdapter.TABLEVIEW_DATATYPE.HEADER);
        recycler.setAdapter(adapter);
    }

    private void resetLayoutParams(TypedArray typedArray) {

        RecyclerView recycler = findViewById(R.id.tableViewRecyclerViewGridLayout);
        recycler.setHasFixedSize(true);

        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), getColumnCount(), GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);

        RecyclerView.Adapter adapter = new TableViewWidgetAdapter(tableData, getContext(), TableViewWidgetAdapter.TABLEVIEW_DATATYPE.BODY);
        recycler.setAdapter(adapter);

    }

    public AttributeSet getAttrs() {
        return attrs;
    }

    public void setAttrs(AttributeSet attrs) {
        this.attrs = attrs;
    }

    public List<String> getTableData() {
        return tableData;
    }


    public void setTableData(List<String> tableHeaderData, List<String> tableData) {

        this.tableHeaderData = tableHeaderData;
        this.tableData = tableData;

        refreshLayout();
    }

    private void refreshLayout() {

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TableView, 0, 0);

        try {
            resetHeaderLayoutParams(typedArray);
            resetLayoutParams(typedArray);
            invalidate();
            requestLayout();
        } finally {
            typedArray.recycle();
        }
    }

    private int getColumnCount() {

        int count = 1;

        if (this.tableHeaderData != null) {
            count = tableHeaderData.size();
        }

        return count > 0 ? count : 1;
    }
}
