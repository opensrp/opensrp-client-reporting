package org.smartregister.reporting.domain;

public class ProgressIndicatorConfig {

    private int progressVal;
    private String title;
    private String subtitle;
    private int foregroundColor;
    private int backgroundColor;

    public ProgressIndicatorConfig() {
    }

    public int getProgressVal() {
        return progressVal;
    }

    public void setProgressVal(int progressVal) {
        this.progressVal = progressVal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
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
