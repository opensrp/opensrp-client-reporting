package org.smartregister.reporting.contract;

public interface IndicatorGeneratorContract {

    interface View { // AbstractView?
        // init Presenter
        // Refresh data for specific indicators
        void onResume();
    }

    interface Presenter {
        // Return a list of request indicator data
        // List getIndicatorValues(List interestedIndicators)
    }

    interface Model {
        // lastProcessedDate -> From preferences
        // List<Map<String, Integer>> getIndicatorsDailyTallies(String lastProcessedDate) // this will in turn call the dao
    }
}
