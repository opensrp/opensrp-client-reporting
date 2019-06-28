# In-App Reporting Library

The In-app reporting library allows creating of data visualizations for display in an application for instance on a dashboard. The library provides a custom numeric value display to show a lable and a numeric value and extends HelloCharts Android library to show Charts e.g. PieCharts.  

## Supported Visualizations

The current supported visualizations are

    1. Numeric display

        - This is used to display a lable and a value (count)

    2. PieChart

        - This is used to display ratios with different colors for the different ratios being displayed

## How the library works  

When the library starts the Indicator defintions and respective queries are read from a YAML config file and persisted on the device.  
The library schedules a job to run periodically (essentially every 2 minutes) to generate the indicators.  

When the job runs the persisted queries are picked and executed each at a time and the resulting values saved against each indicator against the time of execution

The job maintains a `last processed date` so that when computing indicator values it picks up new event entries starting from the last time it completed processing. This is to avoid doing computations on the entire data set every time.  

### Tallying

There are two approaches to generating the indicator tallies: 

    1. Summation of all values stored

        - This is useful when aggregation is required. For instance if generating a tally for _'total number of people registered'_, the queries would store a count of people registered per day then when tallying the total would be a summation of all the values.  

    2. Getting the latest count

        - This applies when interested only in the count as at a particular point in time. For instance, _'Count of persons who have renewed their passports'_.  

## Integrating/using the In-App reporting library  

### Initializing the Reporting library

The library should be initialized in your module's Application class.

```java
// Init Reporting library
ReportingLibrary.init(context, getRepository(), null, BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);
```

Since the library will schedule a job to run periodically to compute the indicator values, you should ensure the JobManager is initialized as well. 

```java
//init Job Manager
JobManager.create(this).addJobCreator(new ChwJobCreator());
```

The job that peridiocally generates the indicator tallies should be scheduled as below:

```java
ChwIndicatorGeneratingJob.scheduleJob(ChwIndicatorGeneratingJob.TAG,
      TimeUnit.MINUTES.toMillis(org.smartregister.reporting.BuildConfig.REPORT_INDICATOR_GENERATION_MINUTES), TimeUnit.MINUTES.toMillis(1));
```

The _report indicator generating minutes_ default value is 2

Tables to persist the indicator defintions, queries and tallies should be created and the indicator data saved for use. 
This should be done in a Repository class.

```java
// Create necessary tables
IndicatorRepository.createTable(database);
IndicatorQueryRepository.createTable(database);
DailyIndicatorCountRepository.createTable(database);

// Initialize indicator data
ReportingLibrary reportingLibraryInstance = ReportingLibrary.getInstance();
// Check if indicator data initialized
boolean indicatorDataInitialised = Boolean.parseBoolean(reportingLibraryInstance.getContext()
        .allSharedPreferences().getPreference(indicatorDataInitialisedPref));
boolean isUpdated = checkIfAppUpdated();
if (!indicatorDataInitialised || isUpdated) {
    reportingLibraryInstance.initIndicatorData(indicatorsConfigFile, database); // This will persist the data in the DB
    reportingLibraryInstance.getContext().allSharedPreferences().savePreference(indicatorDataInitialisedPref, "true");
    reportingLibraryInstance.getContext().allSharedPreferences().savePreference(appVersionCodePref, String.valueOf(BuildConfig.VERSION_CODE));
}
```

### Displaying indicator visualizations

First, fetch the indicator tallies. This would be ideally done in a background thread.

```java
@Nullable
@Override
public List<Map<String, IndicatorTally>> loadInBackground() {
    return presenter.fetchIndicatorsDailytallies();
}
```

The above returns a list of tallies for all the indicators defined as they were stored. 

#### Numeric indicator display

To display a numeric value, create an instance of NumericIndicatorVisualization and set the lable and the value.

This visualization data object is what is used by the NumericDisplayFactory to generate the view to be displayed.

Create an instance of NumericDisplayFactory and use its `getIndicatorView(numericIndicatorData, context)` to get the numeric display view.

```java
// Generate numeric indicator visualization
NumericIndicatorVisualization numericIndicatorData = new NumericIndicatorVisualization(getResources().getString(R.string.total_under_5_count), numericIndicatorValue.get(SampleDataDBUtil.numericIndicatorKey).getCount());

NumericDisplayFactory numericIndicatorFactory = new NumericDisplayFactory();
numericIndicatorView = numericIndicatorFactory.getIndicatorView(numericIndicatorData, context);
```

#### PieChart

To display a PieChart, create an instance of PieChartIndicatorVisualization and initialize the chart label and chart data The chart data is a PieChartIndicatorData object and allows setting whether the PieChart labels, Slice values and colors.

Create an instance of PieChartIndicatorData and use its getIndicatorView(pieChartIndicatorVisualization, context) to get the chart view for display.

```java
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
