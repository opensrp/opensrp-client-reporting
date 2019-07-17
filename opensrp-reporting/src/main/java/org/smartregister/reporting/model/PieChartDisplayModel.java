package org.smartregister.reporting.model;

import org.smartregister.reporting.domain.PieChartSlice;

import java.util.List;

public class PieChartDisplayModel {
    private List<PieChartSlice> pieChartSlices;
    private Integer indicatorLabel;
    private Integer indicatorNote;

    public PieChartDisplayModel(List<PieChartSlice> pieChartSlices, Integer indicatorLabel, Integer indicatorNote) {
        this.pieChartSlices = pieChartSlices;
        this.indicatorLabel = indicatorLabel;
        this.indicatorNote = indicatorNote;
    }

    public Integer getIndicatorNote() {
        return indicatorNote;
    }

    public Integer getIndicatorLabel() {
        return indicatorLabel;
    }

    public List<PieChartSlice> getPieChartSlices() {
        return pieChartSlices;
    }

    public void setPieChartSlices(List<PieChartSlice> pieChartSlices) {
        this.pieChartSlices = pieChartSlices;
    }
}
