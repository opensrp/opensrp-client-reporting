package org.smartregister.reporting.domain;

/**
 * The ReportingIndicatorVisualization base class models reporting indicator visualizations with
 * common properties.
 * For instance, the different visualizations will always have a label (description)
 * of the specific chart.
 * The different custom indicator visualizations extend this class to define their custom properties.
 *
 * @author allan
 */

public class ReportingIndicatorVisualization {
    private String indicatorLabel;
    private String indicatorNote;

    public ReportingIndicatorVisualization() {
    }

    public ReportingIndicatorVisualization(String indicatorLabel) {
        this.indicatorLabel = indicatorLabel;
    }

    public String getIndicatorLabel() {
        return indicatorLabel;
    }

    public void setIndicatorLabel(String indicatorLabel) {
        this.indicatorLabel = indicatorLabel;
    }

    public String getIndicatorNote() {
        return indicatorNote;
    }

    public void setIndicatorNote(String indicatorNote) {
        this.indicatorNote = indicatorNote;
    }
}
