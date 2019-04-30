# opensrp-client-reporting

OpenSRP Client Reporting Library

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/27dcbf45e12941acae3daa3bd310de95)](https://app.codacy.com/app/allan-allay/opensrp-client-reporting?utm_source=github.com&utm_medium=referral&utm_content=OpenSRP/opensrp-client-reporting&utm_campaign=Badge_Grade_Dashboard)
[![Build Status](https://travis-ci.org/OpenSRP/opensrp-client-reporting.svg?branch=master)](https://travis-ci.org/OpenSRP/opensrp-client-reporting) [![Coverage Status](https://coveralls.io/repos/github/OpenSRP/opensrp-client-reporting/badge.svg?branch=master)](https://coveralls.io/github/OpenSRP/opensrp-client-reporting?branch=master)

## Introduction

The Reporting library allows creating of visualizations for display in an application for instance on a dashboard.  
The library provides a custom numeric value display to show a lable and a numeric value and extends [HelloCharts Android library](https://github.com/lecho/hellocharts-android)
to show Charts e.g. PieCharts

## Supported visualizations

The current supported visualizations are  

1. Numeric display

    * This is used to display a lable and a value (count)

2. PieChart

    * This is used to display ratios with different colors for the different ratios being displayed

## Displaying visualizations

### Numeric indicator display  

To display a numeric value, create an instance of NumericIndicatorVisualization and set the lable and the value.  
This visualization data object is what is used by the NumericDisplayFactory to generate the view to be displayed.
Create an instance of NumericDisplayFactory and use its getIndicatorView(numericIndicatorData, context) to get the numeric display view.  

#### Sample Numeric indicator display

```Java
// Generate numeric indicator visualization
NumericIndicatorVisualization numericIndicatorData = new NumericIndicatorVisualization(getResources().getString(R.string.total_under_5_count), numericIndicatorValue.get(SampleDataDBUtil.numericIndicatorKey).getCount());

NumericDisplayFactory numericIndicatorFactory = new NumericDisplayFactory();
numericIndicatorView = numericIndicatorFactory.getIndicatorView(numericIndicatorData, context);
```

### PieChart

To display a PieChart, create an instance of PieChartIndicatorVisualization and initialize the chart label and chart data
The chart data is a PieChartIndicatorData object and allows setting whether the PieChart labels, Slice values and colors.  
Create an instance of PieChartIndicatorData and use its getIndicatorView(pieChartIndicatorVisualization, context) to get the chart view for display.

#### Sample PieChart

```Java
// Generate pie chart

// Define pie chart chartSlices
List<PieChartSlice> chartSlices = new ArrayList<>();

PieChartSlice yesSlice = new PieChartSlice(pieChartYesValue.get(ChartUtil.pieChartYesIndicatorKey).getCount(), ChartUtil.YES_GREEN_SLICE_COLOR);
PieChartSlice noSlice = new PieChartSlice(pieChartNoValue.get(ChartUtil.pieChartNoIndicatorKey).getCount(), ChartUtil.NO_RED_SLICE_COLOR);
chartSlices.add(yesSlice);
chartSlices.add(noSlice);

// Build the chart
PieChartIndicatorVisualization pieChartIndicatorVisualization = new PieChartIndicatorVisualization.PieChartIndicatorVisualizationBuilder()
    .indicatorLabel(getResources().getString(R.string.num_of_lieterate_children_0_60_label))
    .chartHasLabels(true)
    .chartHasLabelsOutside(true)
    .chartHasCenterCircle(false)
    .chartSlices(chartSlices)
    .chartListener(new ChartListener()).build();

PieChartFactory pieChartFactory = new PieChartFactory();
pieChartView = pieChartFactory.getIndicatorView(pieChartIndicatorVisualization, getContext());
```