package org.smartregister.reporting.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;

import org.smartregister.reporting.contract.ReportContract.IndicatorView.CountType;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.domain.PieChartSlice;
import org.smartregister.reporting.domain.ReportingIndicatorVisualization;
import org.smartregister.reporting.factory.IndicatorVisualisationFactory;
import org.smartregister.reporting.model.NumericDisplayModel;
import org.smartregister.reporting.model.PieChartDisplayModel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static org.smartregister.reporting.util.AggregationUtil.getLatestIndicatorCount;

public class ReportingUtil {
    public static final String APP_PROPERTIES_FILE = "app.properties";

    public static long getTotalCount(List<Map<String, IndicatorTally>> indicatorTallies, String indicatorKey) {
        return AggregationUtil.getTotalIndicatorCount(indicatorTallies, indicatorKey);
    }

    public static long getLatestCountBasedOnDate(List<Map<String, IndicatorTally>> indicatorTallies, String indicatorKey) {
        return getLatestIndicatorCount(indicatorTallies, indicatorKey);
    }

    public static View getIndicatorView(ReportingIndicatorVisualization reportingIndicatorVisualization,
                                        IndicatorVisualisationFactory visualisationFactory, Context context) {
        return visualisationFactory.getIndicatorView(reportingIndicatorVisualization, context);
    }

    public static NumericDisplayModel getIndicatorDisplayModel(CountType countType, String indicatorCode,
                                                               int labelResource, List<Map<String, IndicatorTally>> indicatorTallies) {
        return new NumericDisplayModel(countType, indicatorCode, labelResource, getCount(countType, indicatorCode, indicatorTallies));
    }

    private static long getCount(CountType countType, String indicatorCode, List<Map<String, IndicatorTally>> indicatorTallies) {
        long count = 0;
        if (countType == CountType.TOTAL_COUNT) {
            count = getTotalCount(indicatorTallies, indicatorCode);
        } else if ((countType == CountType.LATEST_COUNT)) {
            count = getLatestCountBasedOnDate(indicatorTallies, indicatorCode);
        }
        return count;
    }

    public static PieChartDisplayModel getPieChartDisplayModel(List<PieChartSlice> pieChartSlices,
                                                               Integer indicatorLabel, Integer indicatorNote) {
        return new PieChartDisplayModel(pieChartSlices, indicatorLabel, indicatorNote);
    }

    public static PieChartSlice getPieChartSlice(CountType countType, String indicatorCode, String label, int color,
                                                 List<Map<String, IndicatorTally>> indicatorTallies) {
        return new PieChartSlice((float) getCount(countType, indicatorCode, indicatorTallies), label, color);
    }

    public static List<PieChartSlice> addPieChartSlices(PieChartSlice... chartSlices) {
        List<PieChartSlice> pieChartSlices = new ArrayList<>();
        Collections.addAll(pieChartSlices, chartSlices);
        return pieChartSlices;
    }

    public static AppProperties getProperties(android.content.Context context) {
        AppProperties properties = new AppProperties();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(APP_PROPERTIES_FILE);
            properties.load(inputStream);
        } catch (Exception e) {
            Timber.e(e, "GrowthMonitoringUtils --> getProperties");
        }
        return properties;

    }
}
