package org.smartregister.reporting.contract;

import org.smartregister.reporting.model.IndicatorQuery;
import org.smartregister.reporting.model.IndicatorTally;
import org.smartregister.reporting.model.ReportIndicator;

import java.util.List;
import java.util.Map;

/**
 * Contract defining common methods that should be implemented when working with this
 * Reporting Library
 *
 * @author allan
 */
public interface ReportContract {

    interface View {
        // Refresh UI to display latest indicator data
        void refreshUI();
    }

    interface Presenter {

        void onResume(); // Update UI when this happens

        List<Map<String, IndicatorTally>> fetchIndicatorsDailytallies();

        void addIndicators(List<ReportIndicator> indicators);

        void addIndicatorQueries(List<IndicatorQuery> indicatorQueries);
    }

    interface Model {

        void addIndicator(ReportIndicator indicator);

        void addIndicatorQuery(IndicatorQuery indicatorQuery);

        List<Map<String, IndicatorTally>> getIndicatorsDailyTallies();
    }
}
