package org.smartregister.reporting.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.smartregister.reporting.BaseUnitTest;
import org.smartregister.reporting.R;
import org.smartregister.reporting.domain.ProgressIndicatorConfig;
import org.smartregister.reporting.domain.ProgressIndicatorDisplayOptions;
import org.smartregister.reporting.factory.ProgressIndicatorFactory;

@PrepareForTest(LayoutInflater.class)
public class ProgressIndicatorFactoryTest extends BaseUnitTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    ProgressIndicatorDisplayOptions displayOptions;

    @Mock
    private Context context;

    @Mock
    private TextView indicatorLabel;

    @Mock
    private LinearLayout rootLayout;

    @Mock
    private LayoutInflater layoutInflater;

    @Mock
    private ProgressIndicator progressWidget;

    @Mock
    private ProgressIndicatorConfig config;

    @InjectMocks
    private ProgressIndicatorFactory progressIndicatorFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void getProgressIndicatorViewReturnsCorrectView() {
        ProgressIndicatorFactory factorySpy = Mockito.spy(progressIndicatorFactory);
        PowerMockito.mockStatic(LayoutInflater.class);
        PowerMockito.when(LayoutInflater.from(context)).thenReturn(layoutInflater);
        Mockito.doReturn(rootLayout).when(layoutInflater).inflate(R.layout.progress_indicator_factory_layout, null);
        Mockito.doReturn(indicatorLabel).when(rootLayout).findViewById(R.id.tv_indicator_label);
        Mockito.doReturn(config).when(displayOptions).getConfig();
        Mockito.doReturn(progressWidget).when(rootLayout).findViewById(R.id.progressIndicatorView);

        View view = factorySpy.getIndicatorView(displayOptions, context);
        Assert.assertNotNull(view);
        Assert.assertTrue(view instanceof LinearLayout);
    }

}
