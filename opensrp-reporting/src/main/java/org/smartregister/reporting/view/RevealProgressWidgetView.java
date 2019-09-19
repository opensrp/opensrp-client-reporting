package org.smartregister.reporting.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.smartregister.reporting.R;

/**
 * Created by ndegwamartin on 2019-09-16.
 */
public class RevealProgressWidgetView extends LinearLayout {

    private int progressBarBackgroundColor;
    private int progressBarForegroundColor;
    private String title;
    private String subTitle;
    private int progress;
    private boolean isTitleHidden;
    private boolean isSubTitleHidden;

    private static final String PROGRESSBAR_FOREGROUND_COLOR = "progressbar_foreground_color";
    private static final String PROGRESSBAR_BACKGROUND_COLOR = "progressbar_background_color";
    private static final String PROGRESSBAR_TITLE = "progressbar_title";
    private static final String PROGRESSBAR_SUB_TITLE = "progressbar_sub_title";
    private static final String PROGRESSBAR_PROGRESS = "progressbar_progress";
    private static final String PROGRESSBAR_INSTANCE_STATE = "progressbar_instance_state";

    private AttributeSet attrs;

    public RevealProgressWidgetView(Context context) {
        super(context);
        initView();

    }

    public RevealProgressWidgetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        setupAttributes(attrs);
    }

    public RevealProgressWidgetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        setupAttributes(attrs);
    }

    @TargetApi(android.os.Build.VERSION_CODES.LOLLIPOP)
    public RevealProgressWidgetView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
        setupAttributes(attrs);
    }

    /**
     * Set init configs for parent layout view
     */
    private void initView() {
        inflate(getContext(), R.layout.reveal_progress_widget, this);
        setGravity(Gravity.LEFT);
        setOrientation(VERTICAL);
    }

    /**
     * @param attrs an attribute set styled attributes from theme
     */

    private void setupAttributes(AttributeSet attrs) {
        this.attrs = attrs;

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.RevealProgressWidgetView, 0, 0);

        try {

            resetLayoutParams(typedArray);

        } finally {

            typedArray.recycle();// We must recycle TypedArray objects as they are shared
        }
    }

    private void resetLayoutParams(TypedArray typedArray) {
        progress = progress > 0 ? progress : typedArray.getInteger(R.styleable.RevealProgressWidgetView_progress, 0);

        progressBarForegroundColor = progressBarForegroundColor != 0 ? progressBarForegroundColor : typedArray.getColor(R.styleable.RevealProgressWidgetView_progressBarForegroundColor, 0);
        progressBarBackgroundColor = progressBarBackgroundColor != 0 ? progressBarBackgroundColor : typedArray.getColor(R.styleable.RevealProgressWidgetView_progressBarBackgroundColor, 0);

        title = TextUtils.isEmpty(title) ? typedArray.getString(R.styleable.RevealProgressWidgetView_title) : title;
        title = TextUtils.isEmpty(title) ? progress + "%" : title;

        subTitle = TextUtils.isEmpty(subTitle) ? typedArray.getString(R.styleable.RevealProgressWidgetView_subTitle) : subTitle;

        TextView titleTextView = findViewById(R.id.title_textview);
        titleTextView.setText(title);
        titleTextView.setVisibility(isTitleHidden ? View.GONE : View.VISIBLE);

        TextView subTitleTextView = findViewById(R.id.sub_title_textview);
        subTitleTextView.setText(subTitle);
        subTitleTextView.setVisibility(isSubTitleHidden ? View.GONE : View.VISIBLE);

        ProgressBar progressBarView = findViewById(R.id.progressbar_view);
        progressBarView.setProgress(progress);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            LayerDrawable progressBarDrawable = (LayerDrawable) progressBarView.getProgressDrawable().mutate();
            Drawable backgroundDrawable = progressBarDrawable.getDrawable(0).mutate();
            ClipDrawable clipDrawable = (ClipDrawable) progressBarDrawable.getDrawable(1).mutate();

            if (progressBarBackgroundColor != 0)
                backgroundDrawable.setColorFilter(progressBarBackgroundColor, PorterDuff.Mode.SRC_IN);

            if (progressBarForegroundColor != 0) {

                GradientDrawable gdDefault = new GradientDrawable();
                gdDefault.setColor(progressBarForegroundColor);
                gdDefault.setCornerRadius(10);
                gdDefault.setStroke(2, progressBarBackgroundColor);

                clipDrawable.setDrawable(gdDefault);


            }
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PROGRESSBAR_INSTANCE_STATE, super.onSaveInstanceState());// Store base view state

        bundle.putInt(PROGRESSBAR_FOREGROUND_COLOR, this.progressBarForegroundColor);
        bundle.putInt(PROGRESSBAR_BACKGROUND_COLOR, this.progressBarBackgroundColor);
        bundle.putString(PROGRESSBAR_TITLE, this.title);
        bundle.putString(PROGRESSBAR_SUB_TITLE, this.subTitle);
        bundle.putInt(PROGRESSBAR_PROGRESS, this.progress);


        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            // Load back our custom view state

            this.progressBarForegroundColor = bundle.getInt(PROGRESSBAR_FOREGROUND_COLOR);
            this.progressBarBackgroundColor = bundle.getInt(PROGRESSBAR_BACKGROUND_COLOR);
            this.title = bundle.getString(PROGRESSBAR_TITLE);
            this.subTitle = bundle.getString(PROGRESSBAR_SUB_TITLE);
            this.progress = bundle.getInt(PROGRESSBAR_PROGRESS);

            state = bundle.getParcelable(PROGRESSBAR_INSTANCE_STATE);// Load base view state back
        }

        super.onRestoreInstanceState(state);// Pass base view state to super
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    private void refreshLayout() {

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.RevealProgressWidgetView, 0, 0);

        try {
            resetLayoutParams(typedArray);
            invalidate();
            requestLayout();
        } finally {
            typedArray.recycle();
        }
    }

    public int getProgressBarBackgroundColor() {
        return progressBarBackgroundColor;
    }

    /**
     * Only works for API 23 and onwards other use a custom drawable xml to customize style
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void setProgressBarBackgroundColor(int progressBarBackgroundColor) {
        this.progressBarBackgroundColor = progressBarBackgroundColor;
        refreshLayout();
    }

    public int getProgressBarForegroundColor() {
        return progressBarForegroundColor;
    }

    /**
     * Only works for API 23 and onwards other use a custom drawable xml to customize style
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void setProgressBarForegroundColor(int progressBarForegroundColor) {
        this.progressBarForegroundColor = progressBarForegroundColor;
        refreshLayout();
    }

    public String getTitle() {
        return title;
    }

    /**
     * Sets title of indicator (top text)
     */
    public void setTitle(String title) {
        this.title = title;
        refreshLayout();
    }

    public String getSubTitle() {
        return subTitle;
    }

    /**
     * Sets subtitle of indicator (top text)
     */
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        refreshLayout();
    }

    public int getProgress() {
        return progress;
    }

    /**
     * Sets progress of indicator bar
     */
    public void setProgress(int progress) {
        this.progress = progress;
        refreshLayout();
    }

    /**
     * Sets hides title of indicator
     */
    public void hideTitle(boolean isHidden) {
        this.isTitleHidden = isHidden;
        refreshLayout();
    }

    public void hideSubTitle(boolean isHidden) {
        this.isSubTitleHidden = isHidden;
        refreshLayout();
    }
}