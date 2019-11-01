package org.smartregister.reporting.view;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.util.AttributeSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.reporting.BaseUnitTest;
import org.smartregister.reporting.R;

/**
 * Created by ndegwamartin on 2019-10-30.
 */
public class TableViewTest extends BaseUnitTest {

    @Mock
    private AttributeSet attributeSet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructorsInstantiateSuccesfully() {

        TableView view = new TableView(RuntimeEnvironment.application);
        Assert.assertNotNull(view);


        view = new TableView(RuntimeEnvironment.application, attributeSet);
        Assert.assertNotNull(view);


        view = new TableView(RuntimeEnvironment.application, attributeSet, R.styleable.ProgressIndicatorView_progress);
        Assert.assertNotNull(view);


        view = new TableView(RuntimeEnvironment.application, attributeSet, R.styleable.ProgressIndicatorView_progress, R.style.tableViewTestStyle);
        Assert.assertNotNull(view);
    }

    @Test
    public void testSetupAttributesInitializesStyleAttributesWithCorrectValues() {

        Resources.Theme theme = RuntimeEnvironment.application.getResources().newTheme();
        theme.applyStyle(R.style.tableViewTestStyle, true);

        int[] viewAttr = R.styleable.TableView;
        TypedArray styledAttributes = theme.obtainStyledAttributes(viewAttr);

        TableView actualObject = new TableView(RuntimeEnvironment.application);
        TableView view = Mockito.spy(actualObject);
        Assert.assertNotNull(view);

        Mockito.when(view.getStyledAttributes()).thenReturn(styledAttributes);

        view.setupAttributes(attributeSet);

        Assert.assertEquals(Typeface.NORMAL, view.getHeaderTextStyle());
        Assert.assertEquals(RuntimeEnvironment.application.getResources().getColor(R.color.dark_grey_text), view.getHeaderTextColor());
        Assert.assertEquals(RuntimeEnvironment.application.getResources().getColor(R.color.colorPastelGreen), view.getHeaderBackgroundColor());
        Assert.assertEquals(RuntimeEnvironment.application.getResources().getColor(R.color.text_blue), view.getRowTextColor());
        Assert.assertEquals(RuntimeEnvironment.application.getResources().getColor(R.color.dark_grey), view.getBorderColor());
        Assert.assertTrue(view.isRowBorderHidden());

    }

    @Test
    public void testObjectSettersOverrideXMLStyledAttributes() {

        Resources.Theme theme = RuntimeEnvironment.application.getResources().newTheme();
        theme.applyStyle(R.style.tableViewTestStyle, true);

        TableView actualObject = new TableView(RuntimeEnvironment.application);
        TableView view = Mockito.spy(actualObject);

        view.setupAttributes(attributeSet);

        view.setHeaderTextStyle(Typeface.ITALIC);
        view.setHeaderTextColor(RuntimeEnvironment.application.getResources().getColor(R.color.colorAccent));
        view.setHeaderBackgroundColor(RuntimeEnvironment.application.getResources().getColor(R.color.colorPrimaryDark));
        view.setBorderColor(RuntimeEnvironment.application.getResources().getColor(R.color.pnc_circle_yellow));
        view.setRowTextColor(RuntimeEnvironment.application.getResources().getColor(R.color.alert_complete_green));
        view.setRowBorderHidden(false);

        Assert.assertEquals(Typeface.ITALIC, view.getHeaderTextStyle());
        Assert.assertEquals(RuntimeEnvironment.application.getResources().getColor(R.color.colorAccent), view.getHeaderTextColor());
        Assert.assertEquals(RuntimeEnvironment.application.getResources().getColor(R.color.colorPrimaryDark), view.getHeaderBackgroundColor());
        Assert.assertEquals(RuntimeEnvironment.application.getResources().getColor(R.color.pnc_circle_yellow), view.getBorderColor());
        Assert.assertEquals(RuntimeEnvironment.application.getResources().getColor(R.color.alert_complete_green), view.getRowTextColor());
        Assert.assertFalse(view.isRowBorderHidden());

    }


    @Test
    public void testSaveInstanceStateSavesAndRestoresStateCorrectly() {

        Resources.Theme theme = RuntimeEnvironment.application.getResources().newTheme();
        theme.applyStyle(R.style.tableViewTestStyle, true);

        TableView actualObject = new TableView(RuntimeEnvironment.application, attributeSet);
        TableView view = Mockito.spy(actualObject);

        view.setHeaderTextStyle(Typeface.BOLD_ITALIC);
        view.setHeaderTextColor(RuntimeEnvironment.application.getResources().getColor(R.color.dark_grey_text));
        view.setHeaderBackgroundColor(RuntimeEnvironment.application.getResources().getColor(R.color.colorPastelGreen));
        view.setBorderColor(RuntimeEnvironment.application.getResources().getColor(R.color.text_blue));
        view.setRowTextColor(RuntimeEnvironment.application.getResources().getColor(R.color.dark_grey));
        view.setRowBorderHidden(true);

        Parcelable saveInstanceState = view.onSaveInstanceState();

        //clear values
        view.setHeaderTextStyle(-1);
        view.setHeaderTextColor(1);
        view.setHeaderBackgroundColor(1);
        view.setBorderColor(1);
        view.setRowTextColor(1);
        view.setRowBorderHidden(false);

        //restore values
        view.onRestoreInstanceState(saveInstanceState);

        Assert.assertEquals(Typeface.BOLD_ITALIC, view.getHeaderTextStyle());
        Assert.assertEquals(RuntimeEnvironment.application.getResources().getColor(R.color.dark_grey_text), view.getHeaderTextColor());
        Assert.assertEquals(RuntimeEnvironment.application.getResources().getColor(R.color.colorPastelGreen), view.getHeaderBackgroundColor());
        Assert.assertEquals(RuntimeEnvironment.application.getResources().getColor(R.color.text_blue), view.getBorderColor());
        Assert.assertEquals(RuntimeEnvironment.application.getResources().getColor(R.color.dark_grey), view.getRowTextColor());
        Assert.assertTrue(view.isRowBorderHidden());

    }
}
