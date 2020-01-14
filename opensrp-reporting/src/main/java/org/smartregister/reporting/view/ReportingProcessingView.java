package org.smartregister.reporting.view;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.snackbar.ContentViewCallback;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import org.smartregister.reporting.R;


/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2019-06-27
 */

public class ReportingProcessingView extends LinearLayout implements ContentViewCallback {

    public ReportingProcessingView(Context context) {
        super(context);
    }

    public ReportingProcessingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReportingProcessingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ReportingProcessingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_report_processing_in_progress, this);
    }

    @Override
    public void animateContentIn(int i, int i1) {
        // No animation for adding the content in
    }

    @Override
    public void animateContentOut(int i, int i1) {
        // No animation for content out
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

}