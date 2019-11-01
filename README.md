# opensrp-client-reporting

OpenSRP Client Reporting Library

[![Build Status](https://travis-ci.org/OpenSRP/opensrp-client-reporting.svg?branch=master)](https://travis-ci.org/OpenSRP/opensrp-client-reporting) [![Coverage Status](https://coveralls.io/repos/github/OpenSRP/opensrp-client-reporting/badge.svg?branch=master)](https://coveralls.io/github/OpenSRP/opensrp-client-reporting?branch=master) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/642391cacc03450b894b662eac7f30a3)](https://www.codacy.com/app/OpenSRP/opensrp-client-reporting?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=OpenSRP/opensrp-client-reporting&amp;utm_campaign=Badge_Grade)

## Introduction

The Reporting library allows creating of visualizations for display in an application for instance on a dashboard.  
The library provides a custom numeric value display to show a lable and a numeric value and extends [HelloCharts Android library](https://github.com/lecho/hellocharts-android)
to show Charts e.g. PieCharts

## Supported visualizations

The current supported visualizations are  

  1. Numeric display

      * This is used to display a label and a value (count)

  2. PieChart

      * This is used to display ratios with different colors for the different ratios being displayed

## Displaying visualizations
To display the currently supported visualization, you will need to implement the ``` Reporting.View ``` contract
Then create your views in the overridden method called ``` void buildVisualization(ViewGroup mainLayout)```
The **mainLayout** is the root view you want the visualizations to be shown.
### Numeric indicator display  

To display a numeric value use the code snippet below. 
```Java

  NumericDisplayModel indicator1 = getIndicatorDisplayModel(TOTAL_COUNT, ChartUtil.numericIndicatorKey, R.string.total_under_5_count, indicatorTallies);
  mainLayout.addView(new NumericIndicatorView(getContext(), indicator1).createView());

```
```TOTAL_COUNT ``` is an enumeration type used to denote the type of aggregation you want to use on the passed 
```indicatorTallies ```. You can also use ```LATEST_COUNT``` if you need to get the latest count based on date and time.

### PieChart
For pie charts display. You can use the following code snippet.
```Java
   PieChartSlice indicator2_1 = getPieChartSlice(LATEST_COUNT, ChartUtil.pieChartYesIndicatorKey, getResources().getString(R.string.yes_slice_label), getResources().getColor(R.color.colorPieChartGreen), indicatorTallies);
   PieChartSlice indicator2_2 = getPieChartSlice(LATEST_COUNT, ChartUtil.pieChartNoIndicatorKey, getResources().getString(R.string.no_button_label), getResources().getColor(R.color.colorPieChartRed), indicatorTallies);
   mainLayout.addView(new PieChartIndicatorView(getContext(), getPieChartDisplayModel(addPieChartSlices(indicator2_1, indicator2_2), R.string.num_of_lieterate_children_0_60_label, R.string.sample_note)).createView());
```
### Progress indicator
This indicator widget basically has a progressbar, main title(Label) and a sub title. 

The following are the configurable properties

| **Property**   | **Type** | **Usage** |
| ------------- | ------------- |-------------
| progressBarForegroundColor  | int  | This is a color Resource ID that sets the progress bar foreground color **(API 23 and above)**|
| progressBarBackgroundColor  | int  |This is a color Resource ID that sets the progress bar background color **(API 23 and above)**|
| title  | String  | This is a label for the indicator (*appears at top*)|
| subTitle  | String  |This is a sub title (*label appears at bottom*)|
| progress  | int  | This the progress of the indicator out of 100 (*Percentage %*)|
| isTitleHidden  | int  | This hides or shows the title (*default false*)|
| isSubTitleHidden  | int  | This hides or shows the sub title (*default false*)|
|progressDrawable | int | This is the Resource ID of your custom progressbar drawable [_Template Here_](https://github.com/OpenSRP/opensrp-client-reporting/blob/master/opensrp-reporting/src/main/res/layout/numeric_indicator_view.xml) |

**NB:** For more flexibility and configurations, it is also possible to style the widget by overriding the widgets drawable. You can do this by cloning the file [**here**](https://github.com/OpenSRP/opensrp-client-reporting/blob/master/opensrp-reporting/src/main/res/layout/numeric_indicator_view.xml) and making the necessary changes. Then place the files in your app's drawables folder. This is especially needed to configure the progress bar colors and styling for **API < 23**

using the property **progressDrawable** listed above, you can set the progressDrawable for individual instances of the ProgressIndicatorViews both programmatically or via xml styling.

**Programmatically:** 
```
        ProgressIndicatorView progressWidget = getActivity().findViewById(R.id.progressIndicatorView);
        progressWidget.setProgress(42);
        progressWidget.setTitle("Users registered - 42%");
        progressWidget.setProgressDrawable(R.drawable.progress_indicator_bg);
        progressWidget.setProgressBarForegroundColor(ContextCompat.getColor(getContext(), R.color.pnc_circle_red));
        progressWidget.setProgressBarBackgroundColor(ContextCompat.getColor(getContext(), R.color.pnc_circle_yellow));
```

**Via XML:**

```
 <org.smartregister.reporting.view.ProgressIndicatorView
            android:id="@+id/progressIndicatorView2"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            app:progressBarBackgroundColor="@color/text_black"
            app:progressBarForegroundColor="@color/colorPastelGreen"
            app:subTitle="Coverage"
            app:progressDrawable="@drawable/custom_progress_indicator_bg"
``` 

### Table View
This table widget basically has a header with column head values and rows to display data in a tabular format.

The following are the configurable properties

| **Property**   | **Type** | **Usage** |
| ------------- | ------------- |-------------
| headerTextColor  | int  | This is a color Resource ID that sets the header text color|
| headerTextStyleColor  | Enum  | This is a string enum which can have any of the values *normal*, *italic* or *bold* for xml , Programmatically one should use the *Typeface* class enum |
| headerBackgroundColor  | int  |This is a color Resource ID that sets the header background color|
| rowTextColor  | String  | This is a color Resource ID that sets the data rows text color|
| borderColor  | int  | This is a color Resource ID that sets the table border color. By default, it inherits from the header background color| 
| isRowBorderHidden  | boolean  | This is a boolean value that sets the visibility of the individual table rows border| 

**Programmatically:** 

```
   TableView tableView = getActi4vity().findViewById(R.id.tableView);
   tableView.setTableData(Arrays.asList(new String[]{"Vaccine Name", "Gender", "Value"}), getDummyData());
   tableView.setHeaderTextColor(ContextCompat.getColor(getContext(), R.color.colorPieChartRed));
   tableView.setHeaderBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_blue));
   tableView.setRowTextColor(ContextCompat.getColor(getContext(), R.color.alert_complete_green));
   tableView.setBorderColor(ContextCompat.getColor(getContext(), R.color.pnc_circle_yellow));    
   tableView.setHeaderTextStyle(Typeface.ITALIC);  
   tableView.setRowBorderHidden(true);
        
```

The setTableData 

**Via XML:**

```    
 <org.smartregister.reporting.view.TableView
        android:id="@+id/tableView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        app:borderColor="@color/colorPieChartRed"
        app:headerBackgroundColor="@color/colorSecondaryGreen"
        app:headerTextColor="@color/white"
        app:headerTextStyle="italic"
        app:rowTextColor="@color/text_black"
        app:rowBorderHidden="true" />
```
**How to use:** 

The **setTableData** method is used to populate the header columns and row values. The first parameter is a list of header columns whereas the second column is a list of values (rows data).

The number of columns for the table view widget is derived from the count of values in the header parameter list.

The rows data is populated by creating a list of all the data to be rendered in order. Thus if the header column list has 3 values and the data row list has 6 values, the table view will render the first 3 data items on the list in the first row (thereby matching them with the corresponding column header values) and the last 3 items as the 2nd row on the table

```
        TableView tableView = getActivity().findViewById(R.id.tableView);
        tableView.setTableData(Arrays.asList(new String[]{"Vaccine Name", "Gender", "Value"}), getDummyData());
        
``` 

*Checkout the sample app for more examples..*
