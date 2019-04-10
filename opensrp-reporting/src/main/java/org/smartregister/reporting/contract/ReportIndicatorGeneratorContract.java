package org.smartregister.reporting.contract;

import org.smartregister.reporting.model.IndicatorQuery;
import org.smartregister.reporting.model.IndicatorTally;
import org.smartregister.reporting.model.ReportIndicator;

import java.util.Map;

/**
 * Contract defining common methods that should be implemented when working with this
 * Reporting Library
 *
 * @author allan
 */
public interface ReportIndicatorGeneratorContract {

    interface View {
        // Refresh data for specific indicators
        void refreshUI();
    }

    interface Presenter {

        void onResume(); // Update UI when this happens

        Map<String, IndicatorTally> fetchIndicatorsDailytallies();

        void initialiseIndicator(ReportIndicator indicator);

        void initialiseIndicatorQuery(IndicatorQuery indicatorQuery);
    }

    interface Model {

        void addIndicator(ReportIndicator indicator);

        void addIndicatorQuery(IndicatorQuery indicatorQuery);

        Map<String, IndicatorTally> getIndicatorsDailyTallies();
    }
}
