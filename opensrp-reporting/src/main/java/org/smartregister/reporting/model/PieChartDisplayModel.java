package org.smartregister.reporting.model;

import org.smartregister.reporting.domain.PieChartSlice;
import org.smartregister.reporting.listener.PieChartSelectListener;

import java.util.List;

public class PieChartDisplayModel {
    private List<PieChartSlice> pieChartSlices;
    private Integer indicatorLabel;
    private Integer indicatorNote;
    private PieChartSelectListener pieChartSelectListener;


    public PieChartDisplayModel(List<PieChartSlice> pieChartSlices, Integer indicatorLabel, Integer indicatorNote, PieChartSelectListener pieChartSelectListener) {
        this.pieChartSlices = pieChartSlices;
        this.indicatorLabel = indicatorLabel;
        this.indicatorNote = indicatorNote;
        this.pieChartSelectListener = pieChartSelectListener;
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

    public PieChartSelectListener getPieChartSelectListener() {
        return pieChartSelectListener;
    }

    public void setPieChartSelectListener(PieChartSelectListener pieChartSelectListener) {
        this.pieChartSelectListener = pieChartSelectListener;
    }
}
