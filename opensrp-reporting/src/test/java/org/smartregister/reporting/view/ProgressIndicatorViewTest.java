package org.smartregister.reporting.view;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.annotation.Config;
import org.smartregister.reporting.domain.ProgressIndicatorDisplayOptions;
import org.smartregister.reporting.factory.ProgressIndicatorFactory;
import org.smartregister.reporting.util.ReportingUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ReportingUtil.class)
@Config(sdk = 22)
public class ProgressIndicatorViewTest {

    @Mock
    Context context;

    @Mock
    ProgressIndicatorDisplayOptions displayOptions;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(ReportingUtil.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createViewGetsIndicatorView() {
        ProgressIndicatorView view = new ProgressIndicatorView(context, displayOptions);
        ProgressIndicatorView viewSpy = Mockito.spy(view);
        viewSpy.createView();
        PowerMockito.verifyStatic(ReportingUtil.class, VerificationModeFactory.times(1));
        ReportingUtil.getIndicatorView(ArgumentMatchers.any(ProgressIndicatorDisplayOptions.class),
                ArgumentMatchers.any(ProgressIndicatorFactory.class),
                ArgumentMatchers.any(Context.class));

    }
}
