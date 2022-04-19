package org.smartregister.reporting.domain;

import java.util.List;

public class TabularVisualization extends ReportingIndicatorVisualization {

    private int titleLabelStringResource;
    private List<String> tableHeaderList;
    private List<String> tableDataList;
    private boolean tableHeaderHidden;
    private int colorResource;

    public TabularVisualization(int titleLabelStringResource, List<String> tableHeaderList, List<String> tableDataList, boolean tableHeaderHidden) {
        this.titleLabelStringResource = titleLabelStringResource;
        this.tableHeaderList = tableHeaderList;
        this.tableDataList = tableDataList;
        this.tableHeaderHidden = tableHeaderHidden;
    }

    public int getTitleLabelStringResource() {
        return titleLabelStringResource;
    }

    public void setTitleLabelStringResource(int titleLabelStringResource) {
        this.titleLabelStringResource = titleLabelStringResource;
    }

    public List<String> getTableHeaderList() {
        return tableHeaderList;
    }

    public void setTableHeaderList(List<String> tableHeaderList) {
        this.tableHeaderList = tableHeaderList;
    }

    public List<String> getTableDataList() {
        return tableDataList;
    }

    public void setTableDataList(List<String> tableDataList) {
        this.tableDataList = tableDataList;
    }

    public boolean isTableHeaderHidden() {
        return tableHeaderHidden;
    }

    public void setTableHeaderHidden(boolean tableHeaderHidden) {
        this.tableHeaderHidden = tableHeaderHidden;
    }
}
