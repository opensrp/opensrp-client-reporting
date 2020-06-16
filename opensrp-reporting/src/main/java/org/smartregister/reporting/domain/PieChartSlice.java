package org.smartregister.reporting.domain;


public class PieChartSlice {
    private String label;
    private float value;
    private int color;
    private String key;

    public PieChartSlice(float value, String label, int color, String key) {
        this.value = value;
        this.label = label;
        this.color = color;
        this.key = key;
    }

    public PieChartSlice() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
