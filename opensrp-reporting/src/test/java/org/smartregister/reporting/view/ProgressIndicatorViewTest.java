package org.smartregister.reporting.view;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.reporting.BaseUnitTest;
import org.smartregister.reporting.R;

/**
 * Created by ndegwamartin on 2019-10-11.
 */
public class ProgressIndicatorViewTest extends BaseUnitTest {

    private AttributeSet attributeSet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        attributeSet = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.subtitle, "subtitle")
                .build();
    }

    @Test
    public void testConstructorsInstantiateSuccessfully() {

        ProgressIndicatorView view = new ProgressIndicatorView(RuntimeEnvironment.application);
        Assert.assertNotNull(view);


        view = new ProgressIndicatorView(RuntimeEnvironment.application, attributeSet);
        Assert.assertNotNull(view);


        view = new ProgressIndicatorView(RuntimeEnvironment.application, attributeSet, R.styleable.ProgressIndicatorView_progress);
        Assert.assertNotNull(view);


        view = new ProgressIndicatorView(RuntimeEnvironment.application, attributeSet, R.styleable.ProgressIndicatorView_progress, R.style.progressIndicatorViewTestStyle);
        Assert.assertNotNull(view);
    }

    @Test
    public void testSetupAttributesInitializesStyleAttributesWithCorrectValues() {

        Resources.Theme theme = RuntimeEnvironment.application.getResources().newTheme();
        theme.applyStyle(R.style.progressIndicatorViewTestStyle, true);

        int[] viewAttr = R.styleable.ProgressIndicatorView;
        TypedArray styledAttributes = theme.obtainStyledAttributes(viewAttr);

        ProgressIndicatorView actualObject = new ProgressIndicatorView(RuntimeEnvironment.application);
        ProgressIndicatorView view = Mockito.spy(actualObject);
        Assert.assertNotNull(view);

        Mockito.when(view.getStyledAttributes()).thenReturn(styledAttributes);

        view.setupAttributes(attributeSet);

        Assert.assertEquals("Test Title", view.getTitle());
        Assert.assertEquals("Sub Title", view.getSubTitle());
        Assert.assertEquals(25, view.getProgress());
        Assert.assertEquals(R.drawable.progress_indicator_bg, view.getProgressDrawable());
        Assert.assertEquals(RuntimeEnvironment.application.getResources().getColor(R.color.colorSecondaryGreen), view.getProgressBarBackgroundColor());
        Assert.assertEquals(RuntimeEnvironment.application.getResources().getColor(R.color.colorPastelGreen), view.getProgressBarForegroundColor());

    }

    @Test
    public void testObjectSettersOverrideXMLStyledAttributes() {

        Resources.Theme theme = RuntimeEnvironment.application.getResources().newTheme();
        theme.applyStyle(R.style.progressIndicatorViewTestStyle, true);

        ProgressIndicatorView actualObject = new ProgressIndicatorView(RuntimeEnvironment.application);
        ProgressIndicatorView view = Mockito.spy(actualObject);
        view.setupAttributes(attributeSet);

        view.setTitle("New Test Title");
        view.setSubTitle("New Sub Title");
        view.setProgress(35);
        view.setProgressDrawable(R.drawable.login_background);
        view.setProgressBarForegroundColor(RuntimeEnvironment.application.getResources().getColor(R.color.colorAccent));
        view.setProgressBarBackgroundColor(RuntimeEnvironment.application.getResources().getColor(R.color.colorPrimaryDark));

        Assert.assertEquals("New Test Title", view.getTitle());
        Assert.assertEquals("New Sub Title", view.getSubTitle());
        Assert.assertEquals(35, view.getProgress());
        Assert.assertEquals(R.drawable.login_background, view.getProgressDrawable());
        Assert.assertEquals(RuntimeEnvironment.application.getResources().getColor(R.color.colorPrimaryDark), view.getProgressBarBackgroundColor());
        Assert.assertEquals(RuntimeEnvironment.application.getResources().getColor(R.color.colorAccent), view.getProgressBarForegroundColor());

    }


    @Test
    public void testSaveInstanceStateSavesAndRestoresStateCorrectly() {

        Resources.Theme theme = RuntimeEnvironment.application.getResources().newTheme();
        theme.applyStyle(R.style.progressIndicatorViewTestStyle, true);

        ProgressIndicatorView actualObject = new ProgressIndicatorView(RuntimeEnvironment.application, attributeSet);
        ProgressIndicatorView view = Mockito.spy(actualObject);

        view.setTitle("Test Title");
        view.setSubTitle("Sub Title");
        view.setProgress(25);
        view.setProgressDrawable(R.drawable.progress_indicator_bg);
        view.setProgressBarForegroundColor(RuntimeEnvironment.application.getResources().getColor(R.color.colorSecondaryGreen));
        view.setProgressBarBackgroundColor(RuntimeEnvironment.application.getResources().getColor(R.color.colorPrimaryDark));

        Parcelable saveInstanceState = view.onSaveInstanceState();

        //clear values
        view.setTitle(null);
        view.setSubTitle(null);
        view.setProgress(0);
        view.setProgressDrawable(R.drawable.bottom_bar_initials_background);//set any random bg
        view.setProgressBarForegroundColor(1);
        view.setProgressBarBackgroundColor(1);

        view.onRestoreInstanceState(saveInstanceState);

        Assert.assertEquals("Test Title", view.getTitle());
        Assert.assertEquals("Sub Title", view.getSubTitle());
        Assert.assertEquals(25, view.getProgress());
        Assert.assertEquals(R.drawable.progress_indicator_bg, view.getProgressDrawable());
        Assert.assertEquals(RuntimeEnvironment.application.getResources().getColor(R.color.colorPrimaryDark), view.getProgressBarBackgroundColor());
        Assert.assertEquals(RuntimeEnvironment.application.getResources().getColor(R.color.colorSecondaryGreen), view.getProgressBarForegroundColor());

    }

    @Test
    public void testSettingTitleVisiblityConfigurationsUpdateViewTitlesCorrectly() {

        Resources.Theme theme = RuntimeEnvironment.application.getResources().newTheme();
        theme.applyStyle(R.style.progressIndicatorViewTestStyle, true);

        ProgressIndicatorView actualObject = new ProgressIndicatorView(RuntimeEnvironment.application);
        ProgressIndicatorView view = Mockito.spy(actualObject);
        view.onDraw(Mockito.mock(Canvas.class));
        Assert.assertNotNull(view);

        TextView titleTextView = view.findViewById(R.id.title_textview);
        TextView subTitleTextView = view.findViewById(R.id.sub_title_textview);

        Assert.assertEquals(View.VISIBLE, titleTextView.getVisibility());
        Assert.assertEquals(View.VISIBLE, subTitleTextView.getVisibility());

        view.hideTitle(true);
        view.hideSubTitle(true);

        view.setupAttributes(attributeSet);

        Assert.assertEquals(View.GONE, titleTextView.getVisibility());
        Assert.assertEquals(View.GONE, subTitleTextView.getVisibility());

    }
}
