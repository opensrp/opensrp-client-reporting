package org.smartregister.reporting.util

import java.util.*

/**
 * Created by randati on 2019-09-02.
 */
class AppProperties : Properties() {
    fun getPropertyBoolean(key: String?): Boolean {
        return java.lang.Boolean.valueOf(getProperty(key))
    }

    fun hasProperty(key: String?): Boolean {
        return getProperty(key) != null
    }

    object KEY {
        const val COUNT_INCREMENTAL = "reporting.incremental"
    }
}