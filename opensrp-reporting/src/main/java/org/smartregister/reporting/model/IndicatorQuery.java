package org.smartregister.reporting.model;

public class IndicatorQuery {
    private Long id;
    private String indicatoryCode;
    private String query;
    private int dbVersion;

    public IndicatorQuery(Long id, String indicatoryCode, String query, int dbVersion) {
        this.id = id;
        this.indicatoryCode = indicatoryCode;
        this.query = query;
        this.dbVersion = dbVersion;
    }

    public IndicatorQuery() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndicatorCode() {
        return indicatoryCode;
    }

    public void setIndicatorCode(String indicatorCode) {
        this.indicatoryCode = indicatoryCode;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(int dbVersion) {
        this.dbVersion = dbVersion;
    }
}
