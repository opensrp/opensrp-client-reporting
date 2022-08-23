package org.smartregister.reporting.view;

import android.content.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.smartregister.reporting.BaseUnitTest;
import org.smartregister.reporting.domain.TabularVisualization;

public class ReportingTableViewTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private Context context;

    @Before
    public void setUp() {
        context = Mockito.mock(Context.class);
    }

    @Test
    public void reportingTableViewInstanceInitializesCorrectly() {
        ReportingTableView tableView = new ReportingTableView(context, Mockito.mock(TabularVisualization.class));
        Assert.assertNotNull(tableView);
    }
}
