package org.smartregister.reporting.model;

public class ReportIndicator {
    private Long id;
    private String key;
    private String description;
    private String type;

    public ReportIndicator(Long id, String key, String description, String type) {
        this.id = id;
        this.key = key;
        this.description = description;
        this.type = type;
    }

    public ReportIndicator() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
