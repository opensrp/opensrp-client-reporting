package org.smartregister.reporting.contract;

public interface IndicatorGeneratorContract {

    interface View {
        // init Presenter
        void onResume(); // Refresh data
    }

    interface Presenter {
        // List getIndicatorValues(List interestedIndicators)
    }

    interface Model {
        // Repository.fetchIndicatorData(Map<> indicators)
    }
}
