package org.smartregister.reporting.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
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
import org.smartregister.reporting.domain.NumericIndicatorVisualization;

@PrepareForTest(LayoutInflater.class)
public class NumericDisplayFactoryTest extends BaseUnitTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private NumericIndicatorVisualization visualization;

    @Mock
    private Context context;

    @Mock
    private ConstraintLayout rootLayout;

    @Mock
    private LayoutInflater layoutInflater;

    @Mock
    private TextView chartLabelTextView;

    @Mock
    private TextView chartValueTextView;

    @InjectMocks
    private NumericDisplayFactory numericDisplayFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getNumericDisplayIndicatorViewReturnsCorrectView() {
        NumericDisplayFactory numericDisplayFactorySpy = Mockito.spy(numericDisplayFactory);
        PowerMockito.mockStatic(LayoutInflater.class);
        PowerMockito.when(LayoutInflater.from(context)).thenReturn(layoutInflater);
        Mockito.doReturn(rootLayout).when(layoutInflater).inflate(R.layout.numeric_indicator_view, null);
        Mockito.doReturn(chartLabelTextView).when(rootLayout).findViewById(R.id.numeric_indicator_label);
        Mockito.doReturn(chartValueTextView).when(rootLayout).findViewById(R.id.numeric_indicator_value);
        View view = numericDisplayFactorySpy.getIndicatorView(visualization, context);
        Assert.assertNotNull(view);
        Assert.assertTrue(view instanceof ConstraintLayout);
    }

}
