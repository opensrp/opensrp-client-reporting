package org.smartregister.reporting.domain;

public class ProgressIndicatorConfig {

    private int progressVal;
    private String indicatorLabel;
    private String progressIndicatorTitle;
    private String progressIndicatorSubtitle;
    private int progressIndicatorTitleColor;
    private int foregroundColor;
    private int backgroundColor;

    public int getProgressVal() {
        return progressVal;
    }

    public void setProgressVal(int progressVal) {
        this.progressVal = progressVal;
    }

    public String getIndicatorLabel() {
        return indicatorLabel;
    }

    public void setIndicatorLabel(String indicatorLabel) {
        this.indicatorLabel = indicatorLabel;
    }

    public String getProgressIndicatorTitle() {
        return progressIndicatorTitle;
    }

    public void setProgressIndicatorTitle(String progressIndicatorTitle) {
        this.progressIndicatorTitle = progressIndicatorTitle;
    }

    public String getProgressIndicatorSubtitle() {
        return progressIndicatorSubtitle;
    }

    public void setProgressIndicatorSubtitle(String progressIndicatorSubtitle) {
        this.progressIndicatorSubtitle = progressIndicatorSubtitle;
    }

    public int getProgressIndicatorTitleColor() {
        return progressIndicatorTitleColor;
    }

    public void setProgressIndicatorTitleColor(int progressIndicatorTitleColor) {
        this.progressIndicatorTitleColor = progressIndicatorTitleColor;
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
