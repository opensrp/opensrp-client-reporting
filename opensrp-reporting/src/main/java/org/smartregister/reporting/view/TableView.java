package org.smartregister.reporting.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private int headerTextColor;
    private int headerBackgroundColor;
    private int rowTextColor;
    private int borderColor;
    private int headerTextStyle;
    private int tableHeight = 0;
    private Boolean isRowBorderHidden;
    private Boolean isTableHeaderHidden;
    private OnClickListener onRowClickListener;
    private List<String> tableDataRowIds = new ArrayList<>();

    private AttributeSet attrs;

    private static final String TABLEVIEW_HEADERTEXT_COLOR = "tableview_headertext_color";
    private static final String TABLEVIEW_HEADER_BACKGROUND_COLOR = "tableview_background_color";
    private static final String TABLEVIEW_HEADER_TEXTSTYLE = "tableview_header_textstyle";
    private static final String TABLEVIEW_BORDER_COLOR = "tableview_border_color";
    private static final String TABLEVIEW_INSTANCE_STATE = "tableview_instance_state";
    private static final String TABLEVIEW_ROWTEXT_COLOR = "tableview_rowtext_color";
    private static final String TABLEVIEW_ROWTBORDER_HIDDEN = "tableview_rowborder_color";
    private static final String TABLEVIEW_HEADER_HIDDEN = "tableview_header_hidden";
    private static final String TABLEVIEW_HEIGHT = "tableview_height";

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
    }

    /**
     * @param attrs an attribute set styled attributes from theme
     */

    protected void setupAttributes(AttributeSet attrs) {
        setAttrs(attrs);

        TypedArray typedArray = getStyledAttributes();

        try {

            setResourceValues(typedArray);

            TextViewStyle style = new TextViewStyle();
            style.headerTextColor = headerTextColor;
            style.borderColor = borderColor != 0 ? borderColor : headerBackgroundColor;
            style.headerBackgroundColor = headerBackgroundColor;
            style.rowTextColor = rowTextColor;
            style.headerTextStyle = headerTextStyle;
            style.isRowBorderHidden = isRowBorderHidden;
            style.isTableHeaderHidden = isTableHeaderHidden;
            style.tableHeight = tableHeight;

            resetHeaderLayoutParams(style);
            resetLayoutParams(style);

        } finally {

            typedArray.recycle();// We must recycle TypedArray objects as they are shared
        }
    }

    protected TypedArray getStyledAttributes() {
        return getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TableView, 0, 0);
    }

    private void resetHeaderLayoutParams(TextViewStyle style) {

        RecyclerView recycler = findViewById(R.id.tableViewHeaderRecyclerViewGridLayout);
        if (style.isTableHeaderHidden) {
            recycler.setVisibility(View.GONE);
            return;
        }
        recycler.setHasFixedSize(true);
        if (style.headerBackgroundColor != 0) {
            recycler.setBackgroundColor(style.headerBackgroundColor);
        }

        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), getColumnCount(), GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);

        RecyclerView.Adapter adapter = new TableViewWidgetAdapter(tableHeaderData, null, getContext(), TableViewWidgetAdapter.TableviewDatatype.HEADER, style, null);
        recycler.setAdapter(adapter);
    }

    private void resetLayoutParams(TextViewStyle style) {

        RecyclerView recycler = findViewById(R.id.tableViewRecyclerViewGridLayout);
        recycler.setHasFixedSize(true);

        if (tableHeight > 0) {
            ViewGroup.LayoutParams layoutParams = recycler.getLayoutParams();
            layoutParams.height = tableHeight;
            recycler.setLayoutParams(layoutParams);
        }

        setTableViewBorderColor(style.borderColor == 0 && style.headerBackgroundColor != 0 ? style.headerBackgroundColor : style.borderColor);

        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), getColumnCount(), GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);

        RecyclerView.Adapter adapter = new TableViewWidgetAdapter(tableData, tableDataRowIds, getContext(), TableViewWidgetAdapter.TableviewDatatype.BODY, style, onRowClickListener);
        recycler.setAdapter(adapter);

    }

    private void setTableViewBorderColor(int borderColor) {

        if (borderColor != 0) {

            GradientDrawable tableBackgroundDrawable = (GradientDrawable) getChildAt(0).getBackground().mutate();
            tableBackgroundDrawable.setColor(ContextCompat.getColor(getContext(), R.color.white));
            tableBackgroundDrawable.setStroke(1, borderColor);
            getChildAt(0).setBackground(tableBackgroundDrawable);
        }

    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TABLEVIEW_INSTANCE_STATE, super.onSaveInstanceState());// Store base view state

        bundle.putInt(TABLEVIEW_HEADERTEXT_COLOR, this.headerTextColor);
        bundle.putInt(TABLEVIEW_HEADER_BACKGROUND_COLOR, this.headerBackgroundColor);
        bundle.putInt(TABLEVIEW_HEADER_TEXTSTYLE, this.headerTextStyle);
        bundle.putInt(TABLEVIEW_BORDER_COLOR, this.borderColor);
        bundle.putInt(TABLEVIEW_ROWTEXT_COLOR, this.rowTextColor);
        bundle.putBoolean(TABLEVIEW_ROWTBORDER_HIDDEN, this.isRowBorderHidden);
        bundle.putBoolean(TABLEVIEW_HEADER_HIDDEN, this.isTableHeaderHidden);
        bundle.putInt(TABLEVIEW_HEIGHT, this.tableHeight);

        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        Parcelable updatedState = null;

        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            // Load back our custom view state

            this.headerTextColor = bundle.getInt(TABLEVIEW_HEADERTEXT_COLOR);
            this.headerBackgroundColor = bundle.getInt(TABLEVIEW_HEADER_BACKGROUND_COLOR);
            this.borderColor = bundle.getInt(TABLEVIEW_BORDER_COLOR);
            this.rowTextColor = bundle.getInt(TABLEVIEW_ROWTEXT_COLOR);
            this.headerTextStyle = bundle.getInt(TABLEVIEW_HEADER_TEXTSTYLE);
            this.isRowBorderHidden = bundle.getBoolean(TABLEVIEW_ROWTBORDER_HIDDEN);
            this.isTableHeaderHidden = bundle.getBoolean(TABLEVIEW_HEADER_HIDDEN);
            this.tableHeight = bundle.getInt(TABLEVIEW_HEIGHT);

            updatedState = bundle.getParcelable(TABLEVIEW_INSTANCE_STATE);// Load base view state back
        }

        super.onRestoreInstanceState(updatedState != null ? updatedState : state);// Pass base view state to super
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

        setTableData(tableHeaderData, tableData, null, null);
    }

    public void setTableData(List<String> tableHeaderData, List<String> tableData, List<String> tableDataRowIds, OnClickListener onRowClickListener) {

        this.tableHeaderData = tableHeaderData;
        this.tableData = tableData;
        this.tableDataRowIds = tableDataRowIds;
        this.onRowClickListener = onRowClickListener;

        refreshLayout();
    }

    protected void refreshLayout() {

        setupAttributes(getAttrs());
    }

    protected int getColumnCount() {

        int count = 1;

        if (this.tableHeaderData != null) {
            count = tableHeaderData.size();
        }

        return count > 0 ? count : 1;
    }

    private void setResourceValues(TypedArray typedArray) {

        headerTextColor = headerTextColor != 0 ? headerTextColor : typedArray.getColor(R.styleable.TableView_headerTextColor, 0);
        headerBackgroundColor = headerBackgroundColor != 0 ? headerBackgroundColor : typedArray.getColor(R.styleable.TableView_headerBackgroundColor, 0);
        rowTextColor = rowTextColor != 0 ? rowTextColor : typedArray.getColor(R.styleable.TableView_rowTextColor, 0);
        borderColor = borderColor != 0 ? borderColor : typedArray.getColor(R.styleable.TableView_borderColor, 0);
        headerTextStyle = headerTextStyle != 0 ? headerTextStyle : typedArray.getInteger(R.styleable.TableView_headerTextStyle, 0);
        isRowBorderHidden = isRowBorderHidden != null ? isRowBorderHidden : typedArray.getBoolean(R.styleable.TableView_rowBorderHidden, false);
        isTableHeaderHidden = isTableHeaderHidden != null ? isTableHeaderHidden : typedArray.getBoolean(R.styleable.TableView_headerHidden, false);
        tableHeight = tableHeight != 0 ? tableHeight : typedArray.getInt(R.styleable.TableView_tableHeight, 0);
    }

    public int getHeaderTextColor() {
        return headerTextColor;
    }

    public void setHeaderTextColor(int headerTextColor) {
        this.headerTextColor = headerTextColor;
        refreshLayout();
    }

    public int getHeaderBackgroundColor() {
        return headerBackgroundColor;
    }

    public void setHeaderBackgroundColor(int headerBackgroundColor) {
        this.headerBackgroundColor = headerBackgroundColor;
        refreshLayout();
    }

    public int getRowTextColor() {
        return rowTextColor;
    }

    public void setRowTextColor(int rowTextColor) {
        this.rowTextColor = rowTextColor;
        refreshLayout();
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        refreshLayout();
    }

    public int getHeaderTextStyle() {
        return headerTextStyle;
    }

    public void setHeaderTextStyle(int headerTextStyle) {
        this.headerTextStyle = headerTextStyle;
        refreshLayout();
    }

    public boolean isRowBorderHidden() {
        return isRowBorderHidden;
    }
    public boolean isTableHeaderHidden() {
        return isTableHeaderHidden;
    }

    public void setRowBorderHidden(boolean rowBorderHidden) {
        isRowBorderHidden = rowBorderHidden;
        refreshLayout();
    }

    public void setTableHeaderHidden(boolean tableHeaderHidden) {
        isTableHeaderHidden = tableHeaderHidden;
        refreshLayout();
    }

    public int getTableHeight() {
        return tableHeight;
    }

    public void setTableHeight(int tableHeight) {
        this.tableHeight = tableHeight;
        refreshLayout();
    }

    public class TextViewStyle {
        public int headerTextColor;
        public int headerBackgroundColor;
        public int rowTextColor;
        public int borderColor;
        public int headerTextStyle;
        public boolean isRowBorderHidden;
        public boolean isTableHeaderHidden;
        public int tableHeight;
    }
}
