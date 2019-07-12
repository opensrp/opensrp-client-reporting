package org.smartregister.reporting.util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;

import org.smartregister.reporting.R;
import org.smartregister.reporting.contract.ReportContract;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.domain.PieChartSlice;
import org.smartregister.reporting.domain.ReportingIndicatorVisualization;
import org.smartregister.reporting.factory.IndicatorVisualisationFactory;
import org.smartregister.reporting.model.IndicatorDisplayModel;
import org.smartregister.reporting.model.PieChartDisplayModel;

import java.util.List;
import java.util.Map;

import static org.smartregister.reporting.util.AggregationUtil.getLatestIndicatorCount;
import static org.smartregister.reporting.util.AggregationUtil.getStaticIndicatorCount;

public class ReportingUtil {


    // Color definitions for the chart slices. This could essentially be defined in colors.xml
    public static final int YES_GREEN_SLICE_COLOR = Color.parseColor("#99CC00");
    public static final int NO_RED_SLICE_COLOR = Color.parseColor("#FF4444");

    public static long getTotalStaticCount(List<Map<String, IndicatorTally>> indicatorTallies, String indicatorKey) {
        return getStaticIndicatorCount(indicatorTallies, indicatorKey);
    }

    public static long getLatestCountBasedOnDate(List<Map<String, IndicatorTally>> indicatorTallies, String indicatorKey) {
        return getLatestIndicatorCount(indicatorTallies, indicatorKey);
    }

    public static View getIndicatorView(ReportingIndicatorVisualization reportingIndicatorVisualization,
                                        IndicatorVisualisationFactory visualisationFactory, Context context) {
        return visualisationFactory.getIndicatorView(reportingIndicatorVisualization, context);
    }

    public static IndicatorDisplayModel getIndicatorModel(ReportContract.IndicatorView.CountType countType, String indicatorCode,
                                                          int labelResource, List<Map<String, IndicatorTally>> indicatorTallies) {
        long count = 0;
        if (countType == ReportContract.IndicatorView.CountType.STATIC_COUNT) {
            count = getTotalStaticCount(indicatorTallies, indicatorCode);
        } else if ((countType == ReportContract.IndicatorView.CountType.LATEST_COUNT)) {
            count = getLatestCountBasedOnDate(indicatorTallies, indicatorCode);
        }
        return new IndicatorDisplayModel(countType, indicatorCode, labelResource, count);
    }

    public static PieChartDisplayModel getPieChartViewModel(IndicatorDisplayModel yesPart, IndicatorDisplayModel noPart,
                                                            @Nullable String indicatorLabel, @Nullable String indicatorNote) {
        return new PieChartDisplayModel(yesPart, noPart, indicatorLabel, indicatorNote);
    }

    /**
     * Returns the String label for a slice.
     * This is primarily used during handling of a slice click event.
     * It would have been better to have this as part of the PieChartSlice data attributes but
     * there's no mapping for the same in the SliceValue class
     *
     * @param sliceValue the PieChartSlice selected
     * @param context    the context used to retrieve the String value from strings.xml
     * @return text to be displayed
     */
    public static String getPieSelectionValue(PieChartSlice sliceValue, Context context) {
        if (sliceValue.getColor() == YES_GREEN_SLICE_COLOR) {
            return context.getString(R.string.yes_slice_label);
        } else {
            return context.getString(R.string.no_slice_label);
        }
    }
}
