package org.smartregister.reporting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.reporting.BaseUnitTest;
import org.smartregister.reporting.R;
import org.smartregister.reporting.view.TableView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ndegwamartin on 2020-02-19.
 */
public class TableViewWidgetAdapterTest extends BaseUnitTest {

    @Mock
    private List<String> tableData;

    @Mock
    private List<String> tableDataIds;

    @Mock
    private Context context;

    @Mock
    private TableView.TextViewStyle style;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTableViewWidgetAdapterInitsCorrectly() {

        TableViewWidgetAdapter tableViewWidgetAdapter = new TableViewWidgetAdapter(tableData, tableDataIds, context, TableViewWidgetAdapter.TableviewDatatype.BODY, style, null);
        Assert.assertNotNull(tableViewWidgetAdapter);

    }

    @Test
    public void testOnBindViewHolderProcessesSetHeaderDataCorrectly() {

        List<String> headerData = new ArrayList<>();
        headerData.add("ALPHA");
        headerData.add("BETA");


        TableViewWidgetAdapter tableViewWidgetAdapter = new TableViewWidgetAdapter(headerData, null, RuntimeEnvironment.application, TableViewWidgetAdapter.TableviewDatatype.HEADER, style, null);
        Assert.assertNotNull(tableViewWidgetAdapter);

        View mView = LayoutInflater.from(RuntimeEnvironment.application).inflate(R.layout.table_view_cell, new TableView(RuntimeEnvironment.application), true);


        TableViewWidgetAdapter.ViewHolder viewHolder = new TableViewWidgetAdapter.ViewHolder(mView);

        TextView textView = Whitebox.getInternalState(viewHolder, "textView");
        Assert.assertNotNull(textView);

        Assert.assertEquals("", textView.getText());

        tableViewWidgetAdapter.onBindViewHolder(viewHolder, 0);
        Assert.assertEquals("ALPHA", textView.getText());

        tableViewWidgetAdapter.onBindViewHolder(viewHolder, 1);
        Assert.assertEquals("BETA", textView.getText());

        Assert.assertEquals(headerData.size(), tableViewWidgetAdapter.getItemCount());

    }

    @Test
    public void testOnBindViewHolderProcessesSetBodyDataCorrectly() {

        List<String> tableData = new ArrayList<>();
        tableData.add("alpha");
        tableData.add("beta");
        tableData.add("kappa");
        tableData.add("omega");


        TableViewWidgetAdapter tableViewWidgetAdapter = new TableViewWidgetAdapter(tableData, null, RuntimeEnvironment.application, TableViewWidgetAdapter.TableviewDatatype.BODY, style, null);
        Assert.assertNotNull(tableViewWidgetAdapter);

        View mView = LayoutInflater.from(RuntimeEnvironment.application).inflate(R.layout.table_view_cell, new TableView(RuntimeEnvironment.application), true);


        TableViewWidgetAdapter.ViewHolder viewHolder = new TableViewWidgetAdapter.ViewHolder(mView);

        TextView textView = Whitebox.getInternalState(viewHolder, "textView");
        Assert.assertNotNull(textView);

        Assert.assertEquals("", textView.getText());

        tableViewWidgetAdapter.onBindViewHolder(viewHolder, 0);
        Assert.assertEquals("alpha", textView.getText());

        tableViewWidgetAdapter.onBindViewHolder(viewHolder, 1);
        Assert.assertEquals("beta", textView.getText());

        tableViewWidgetAdapter.onBindViewHolder(viewHolder, 2);
        Assert.assertEquals("kappa", textView.getText());

        tableViewWidgetAdapter.onBindViewHolder(viewHolder, 3);
        Assert.assertEquals("omega", textView.getText());

        Assert.assertEquals(4, tableViewWidgetAdapter.getItemCount());

    }
}
