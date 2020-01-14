package org.smartregister.reporting.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import org.smartregister.reporting.view.ReportingProcessingSnackbar;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2020-01-06
 */

public class ViewUtils {

    public static ReportingProcessingSnackbar showReportingProcessingInProgressSnackbar(@NonNull AppCompatActivity appCompatActivity
            , @Nullable ReportingProcessingSnackbar reportingProcessingSnackbar, int margin) {
        if (reportingProcessingSnackbar == null) {
            // Create the snackbar
            View parentView = ((ViewGroup) appCompatActivity.findViewById(android.R.id.content))
                    .getChildAt(0);

            ReportingProcessingSnackbar processingSnackbar = ReportingProcessingSnackbar.make(parentView);

            if (margin != 0) {
                processingSnackbar.addBottomBarMargin(margin);
            }
            processingSnackbar.setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE);
            processingSnackbar.show();

            return processingSnackbar;
        } else if (!reportingProcessingSnackbar.isShown()) {
            reportingProcessingSnackbar.show();
        }

        return reportingProcessingSnackbar;
    }

    public static ReportingProcessingSnackbar showReportingProcessingInProgressBottomSnackbar(final @NonNull AppCompatActivity appCompatActivity, @Nullable final ReportingProcessingSnackbar reportingProcessingSnackbar) {
        final View bottomNavigationBar = appCompatActivity.findViewById(org.smartregister.R.id.bottom_navigation);
        if (bottomNavigationBar != null) {
            bottomNavigationBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int height = bottomNavigationBar.getHeight();
                    showReportingProcessingInProgressSnackbar(appCompatActivity, reportingProcessingSnackbar, height);

                    bottomNavigationBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });

            return null;
        } else {
            return showReportingProcessingInProgressSnackbar(appCompatActivity, reportingProcessingSnackbar, 0);
        }
    }

    public static void removeReportingProcessingInProgressSnackbar(@Nullable ReportingProcessingSnackbar reportingProcessingSnackbar) {
        if (reportingProcessingSnackbar != null && reportingProcessingSnackbar.isShown()) {
            reportingProcessingSnackbar.dismiss();
        }
    }

}
