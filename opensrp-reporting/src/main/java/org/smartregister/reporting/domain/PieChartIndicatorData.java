package org.smartregister.reporting.domain;

import org.smartregister.reporting.listener.PieChartSelectListener;

import java.util.List;

public class PieChartIndicatorData {
    private boolean hasLabels;
    private boolean hasLabelsOutside;
    private boolean hasCenterCircle;
    private List<PieChartSlice> slices;
    private PieChartSelectListener listener;

    public PieChartIndicatorData(boolean hasLabels, boolean hasLabelsOutside, boolean hasCenterCircle, List<PieChartSlice> slices) {
        this.hasLabels = hasLabels;
        this.hasLabelsOutside = hasLabelsOutside;
        this.hasCenterCircle = hasCenterCircle;
        this.slices = slices;
    }

    public PieChartIndicatorData() {}

    public boolean hasLabels() {
        return hasLabels;
    }

    public void setHasLabels(boolean hasLabels) {
        this.hasLabels = hasLabels;
    }

    public boolean hasLabelsOutside() {
        return hasLabelsOutside;
    }

    public void setHasLabelsOutside(boolean hasLabelsOutside) {
        this.hasLabelsOutside = hasLabelsOutside;
    }

    public boolean hasCenterCircle() {
        return hasCenterCircle;
    }

    public void setHasCenterCircle(boolean hasCenterCircle) {
        this.hasCenterCircle = hasCenterCircle;
    }

    public List<PieChartSlice> getSlices() {
        return slices;
    }

    public void setSlices(List<PieChartSlice> slices) {
        this.slices = slices;
    }

    public PieChartSelectListener getListener() {
        return listener;
    }

    public void setListener(PieChartSelectListener listener) {
        this.listener = listener;
    }
}
