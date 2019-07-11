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
import org.smartregister.reporting.domain.PieChartIndicatorData;
import org.smartregister.reporting.domain.PieChartIndicatorVisualization;
import org.smartregister.reporting.factory.PieChartFactory;

import lecho.lib.hellocharts.view.PieChartView;

@PrepareForTest(LayoutInflater.class)
public class PieChartFactoryTest extends BaseUnitTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private PieChartIndicatorVisualization visualization;

    @Mock
    private Context context;

    @Mock
    private TextView chartLabelTextView;

    @Mock
    private TextView chartNoteTextView;

    @Mock
    private TextView numericValueTextView;

    @Mock
    private PieChartView pieChartView;

    @Mock
    private LayoutInflater layoutInflater;

    @Mock
    private LinearLayout rootLayout;

    @Mock
    private PieChartIndicatorData chartConfiguration;

    @InjectMocks
    private PieChartFactory pieChartFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getPieChartIndicatorViewReturnsCorrectView() {
        PieChartFactory pieChartFactorySpy = Mockito.spy(pieChartFactory);
        PowerMockito.mockStatic(LayoutInflater.class);
        PowerMockito.when(LayoutInflater.from(context)).thenReturn(layoutInflater);
        Mockito.doReturn(rootLayout).when(layoutInflater).inflate(R.layout.pie_chart_view, null);
        Mockito.doReturn(chartLabelTextView).when(rootLayout).findViewById(R.id.pie_indicator_label);
        Mockito.doReturn(chartNoteTextView).when(rootLayout).findViewById(R.id.pie_note_label);
        Mockito.doReturn(numericValueTextView).when(rootLayout).findViewById(R.id.numeric_indicator_value);
        Mockito.doReturn(pieChartView).when(rootLayout).findViewById(R.id.pie_chart);
        Mockito.doReturn(chartConfiguration).when(visualization).getChartData();
        View view = pieChartFactorySpy.getIndicatorView(visualization, context);
        Assert.assertNotNull(view);
        Assert.assertTrue(view instanceof LinearLayout);
    }
}
