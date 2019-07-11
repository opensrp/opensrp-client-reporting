package org.smartregister.reporting.model;

public class PieChartDisplayModel {
    private IndicatorDisplayModel yesSlice;
    private IndicatorDisplayModel noSlice;
    private String indicatorLabel;
    private String indicatorNote;

    public PieChartDisplayModel(IndicatorDisplayModel yesSlice, IndicatorDisplayModel noSlice, String indicatorLabel, String indicatorNote) {
        this.yesSlice = yesSlice;
        this.noSlice = noSlice;
        this.indicatorLabel = indicatorLabel;
        this.indicatorNote = indicatorNote;
    }

    public IndicatorDisplayModel getYesSlice() {
        return yesSlice;
    }

    public IndicatorDisplayModel getNoSlice() {
        return noSlice;
    }

    public String getIndicatorNote() {
        return indicatorNote;
    }

    public String getIndicatorLabel() {
        return indicatorLabel;
    }
}
