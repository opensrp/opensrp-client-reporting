package org.smartregister.sample.utils;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.reporting.model.IndicatorQuery;
import org.smartregister.reporting.model.IndicatorTally;
import org.smartregister.reporting.model.ReportIndicator;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;
import org.smartregister.sample.BuildConfig;

public class SampleDataDBUtil {

    public static String numericIndicatorKey = "IND-001";
    public static String pieChartYesIndicatorKey = "IND-002";
    public static String pieChartNoIndicatorKey = "IND-003";

    public static void addSampleIndicators(IndicatorRepository indicatorRepository, SQLiteDatabase database) {
        indicatorRepository.add(new ReportIndicator(null, numericIndicatorKey, "Total Children", null), database);
        indicatorRepository.add(new ReportIndicator(null, pieChartYesIndicatorKey, "Immunized children", null), database);
        indicatorRepository.add(new ReportIndicator(null, pieChartNoIndicatorKey, "Non immunized children", null), database);
    }

    public static void addSampleIndicatorQueries(IndicatorQueryRepository indicatorQueryRepository, SQLiteDatabase database) {
        indicatorQueryRepository.add(new IndicatorQuery(null, numericIndicatorKey, "", BuildConfig.DATABASE_VERSION), database);
        indicatorQueryRepository.add(new IndicatorQuery(null, pieChartYesIndicatorKey, "", BuildConfig.DATABASE_VERSION), database);
        indicatorQueryRepository.add(new IndicatorQuery(null, pieChartNoIndicatorKey, "", BuildConfig.DATABASE_VERSION), database);
    }

    public static void addSampleIndicatorDailyTally(DailyIndicatorCountRepository dailyIndicatorCountRepository, SQLiteDatabase database) {
        dailyIndicatorCountRepository.add(new IndicatorTally(null, 80, numericIndicatorKey, null), database);
        dailyIndicatorCountRepository.add(new IndicatorTally(null, 60, pieChartYesIndicatorKey, null), database);
        dailyIndicatorCountRepository.add(new IndicatorTally(null, 20, pieChartNoIndicatorKey, null), database);
    }
}
