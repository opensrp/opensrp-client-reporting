package org.smartregister.reporting.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
    private int headerTextColor;
    private int headerBackgroundColor;
    private int rowTextColor;
    private int borderColor;
    private String headerTextStyle;

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
        setBackground(ContextCompat.getDrawable(getContext(), R.drawable.table_view_border));
        //setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    /**
     * @param attrs an attribute set styled attributes from theme
     */

    private void setupAttributes(AttributeSet attrs) {
        setAttrs(attrs);

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TableView, 0, 0);

        try {
            setResourceValues(typedArray);

            TextViewStyle style = new TextViewStyle();
            style.headerTextColor = headerTextColor;
            style.borderColor = borderColor;
            style.headerBackgroundColor = headerBackgroundColor;
            style.rowTextColor = rowTextColor;
            style.headerTextStyle = headerTextStyle;

            resetHeaderLayoutParams(style);
            resetLayoutParams(style);

        } finally {

            typedArray.recycle();// We must recycle TypedArray objects as they are shared
        }
    }

    private void resetHeaderLayoutParams(TextViewStyle style) {

        RecyclerView recycler = findViewById(R.id.tableViewHeaderRecyclerViewGridLayout);
        recycler.setHasFixedSize(true);
        if (style.headerBackgroundColor != 0) {
            recycler.setBackgroundColor(style.headerBackgroundColor);
        }

        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), getColumnCount(), GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);

        RecyclerView.Adapter adapter = new TableViewWidgetAdapter(tableHeaderData, getContext(), TableViewWidgetAdapter.TABLEVIEW_DATATYPE.HEADER, style);
        recycler.setAdapter(adapter);
    }

    private void resetLayoutParams(TextViewStyle style) {

        RecyclerView recycler = findViewById(R.id.tableViewRecyclerViewGridLayout);
        recycler.setHasFixedSize(true);

        setTableViewBorderColor(style.borderColor == 0 && style.headerBackgroundColor != 0 ? style.headerBackgroundColor : style.borderColor);

        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), getColumnCount(), GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);

        RecyclerView.Adapter adapter = new TableViewWidgetAdapter(tableData, getContext(), TableViewWidgetAdapter.TABLEVIEW_DATATYPE.BODY, style);
        recycler.setAdapter(adapter);

    }

    private void setTableViewBorderColor(int borderColor) {
        if (borderColor != 0) {

            GradientDrawable tableBackgroundDrawable = new GradientDrawable();
            tableBackgroundDrawable.setColor(ContextCompat.getColor(getContext(), R.color.white));
            tableBackgroundDrawable.setStroke(1, borderColor);
            setBackground(tableBackgroundDrawable);
        }
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

        setupAttributes(getAttrs());
    }

    private int getColumnCount() {

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
        headerTextStyle = !TextUtils.isEmpty(headerTextStyle) ? headerTextStyle : typedArray.getString(R.styleable.TableView_headerTextStyle);

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

    public String getHeaderTextStyle() {
        return headerTextStyle;
    }

    public void setHeaderTextStyle(String headerTextStyle) {
        this.headerTextStyle = headerTextStyle;
        refreshLayout();
    }

    public class TextViewStyle {
        public int headerTextColor;
        public int headerBackgroundColor;
        public int rowTextColor;
        public int borderColor;
        public String headerTextStyle;
    }
}
