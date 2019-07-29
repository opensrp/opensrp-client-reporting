package org.smartregister.reporting.domain;


public class PieChartSlice {
    private String label;
    private float value;
    private int color;

    public PieChartSlice(float value, String label, int color) {
        this.value = value;
        this.label = label;
        this.color = color;
    }

    public PieChartSlice() {

    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
