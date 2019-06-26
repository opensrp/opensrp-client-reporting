package org.smartregister.sample.utils;

import android.content.Context;
import android.graphics.Color;

import org.smartregister.reporting.domain.PieChartSlice;
import org.smartregister.sample.R;

/**
 * The ChartUtil provides util functions to work with Charts
 *
 * @author allan
 */
public class ChartUtil {

    // Sample indicator keys
    public static String numericIndicatorKey = "S_IND_001";
    public static String pieChartYesIndicatorKey = "S_IND_002";
    public static String pieChartNoIndicatorKey = "S_IND_003";

    // Color definitions for the chart slices. This could essentially be defined in colors.xml
    public static final int YES_GREEN_SLICE_COLOR = Color.parseColor("#99CC00");
    public static final int NO_RED_SLICE_COLOR = Color.parseColor("#FF4444");

    /**
     * Returns the String label for a slice.
     * This is primarily used during handling of a slice click event.
     * It would have been better to have this as part of the PieChartSlice data attributes but
     * there's no mapping for the same in the SliceValue class
     *
     * @param sliceValue the PieChartSlice selected
     * @param context    the context used to retrieve the String value from strings.xml
     * @return the text to be displayed
     */
    public static String getPieSelectionValue(PieChartSlice sliceValue, Context context) {
        if (sliceValue.getColor() == YES_GREEN_SLICE_COLOR) {
            return context.getString(R.string.yes_slice_label);
        } else {
            return context.getString(R.string.no_slice_label);
        }
    }
}
