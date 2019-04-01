package org.smartregister.reporting.model;


public class PieChartSlice {
    private float value;
    private int color;

    public PieChartSlice(float value, int color) {
        this.value = value;
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
}
