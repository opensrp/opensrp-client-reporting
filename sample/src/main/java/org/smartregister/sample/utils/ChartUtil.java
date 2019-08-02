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
}
