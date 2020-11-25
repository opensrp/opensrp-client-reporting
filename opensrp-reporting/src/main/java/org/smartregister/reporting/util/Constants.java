package org.smartregister.reporting.util;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2019-07-25
 */

public interface Constants {

    interface PrefKey {

        String APP_VERSION_CODE = "APP_VERSION_CODE";
        String INDICATOR_DATA_INITIALISED = "INDICATOR_DATA_INITIALISED";

    }

    interface DailyIndicatorCountRepository {

        String ID = "_id";
        String INDICATOR_CODE = "indicator_code";
        String INDICATOR_VALUE = "indicator_value";
        String DAY = "day";
        String INDICATOR_VALUE_SET = "indicator_value_set";
        String INDICATOR_VALUE_SET_FLAG = "indicator_is_value_set";
        String INDICATOR_DAILY_TALLY_TABLE = "indicator_daily_tally";
        String INDICATOR_GROUPING = "indicator_grouping";
    }

    interface IndicatorQueryRepository {

        String ID = "_id";
        String QUERY = "indicator_query";
        String INDICATOR_CODE = "indicator_code";
        String DB_VERSION = "db_version";
        String INDICATOR_QUERY_TABLE = "indicator_queries";
        String INDICATOR_QUERY_IS_MULTI_RESULT = "indicator_is_multi_result";
        String INDICATOR_QUERY_EXPECTED_INDICATORS = "expected_indicators";
        String INDICATOR_GROUPING = "grouping";
    }

    interface ReportingConfig {
        String SHOULD_ALLOW_ZERO_TALLIES = "SHOULD_ALLOW_ZERO_TALLIES";
        String MIN_REPORT_DATE = "MIN_REPORT_DATE";
    }
}
