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
### Reveal indicator widget
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

**NB:** For more flexibility and configurations, it is also possible to style the widget by overriding the widgets drawable. You can do this by cloning the file [**here**](https://github.com/OpenSRP/opensrp-client-reporting/blob/master/opensrp-reporting/src/main/res/layout/numeric_indicator_view.xml) and making the necessary changes. Then place the files in your app's drawables folder. This is especially needed to configure the progress bar colors and styling for **API < 23**

*Checkout the sample app for more examples..*
