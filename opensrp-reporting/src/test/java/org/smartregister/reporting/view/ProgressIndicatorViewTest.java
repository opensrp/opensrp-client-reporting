package org.smartregister.reporting.view;

import android.view.View;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.reporting.BaseUnitTest;
import org.smartregister.reporting.domain.ProgressIndicatorDisplayOptions;


public class ProgressIndicatorViewTest extends BaseUnitTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createViewGetsIndicatorView() {
        ProgressIndicatorDisplayOptions displayOptions = new ProgressIndicatorDisplayOptions.ProgressIndicatorBuilder()
                .withIndicatorLabel("Number of tasks")
                .withProgressValue(80)
                .withProgressIndicatorTitle("50%")
                .withProgressIndicatorTitleColor(0)
                .withForegroundColor(1)
                .withBackgroundColor(2)
                .build();
        ProgressIndicatorView progressIndicatorView = new ProgressIndicatorView(RuntimeEnvironment.application, displayOptions);
        View returnedView = progressIndicatorView.createView();
        Assert.assertNotNull(returnedView);
    }
}
