package org.smartregister.reporting.util;

import java.util.Properties;

/**
 * Created by randati on 2019-09-02.
 */
public class AppProperties extends Properties {
    public Boolean getPropertyBoolean(String key) {
        return Boolean.valueOf(getProperty(key));
    }

    public Boolean hasProperty(String key) {
        return getProperty(key) != null;
    }

    public static class KEY {
        public static final String COUNT_INCREMENTAL = "reporting.incremental";
    }
}
