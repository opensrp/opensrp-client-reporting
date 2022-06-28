package org.smartregister.reporting.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.reporting.BaseUnitTest;
import org.smartregister.reporting.R;
import org.smartregister.reporting.domain.TabularVisualization;
import org.smartregister.reporting.factory.TableDisplayFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@PrepareForTest(LayoutInflater.class)
public class TableDisplayFactoryTest extends BaseUnitTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private Context context;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        context = RuntimeEnvironment.application;
    }

    @Test
    public void getTableDisplayIndicatorViewReturnsCorrectView() {
        List<String> tableHeaderList = Arrays.asList("Vaccine Name", "Gender", "Value");
        TabularVisualization tabularVisualization = new TabularVisualization(R.string.table_data_title, tableHeaderList, getDummyData(), true);

        TableDisplayFactory displayFactory = new TableDisplayFactory();
        View view = displayFactory.getIndicatorView(tabularVisualization, context);

        Assert.assertNotNull(view);
        TextView tableTitle = view.findViewById(R.id.tableViewTitle);
        TableView tableView = view.findViewById(R.id.tableView);

        Assert.assertNotNull(tableTitle);
        Assert.assertNotNull(tableView);
        Assert.assertEquals(context.getString(R.string.table_data_title), tableTitle.getText());
    }

    private List<String> getDummyData() {
        List<String> list = new ArrayList<>();
        list.add("BCG");
        list.add("Female");
        list.add(String.valueOf(2500));
        return list;
    }
}
