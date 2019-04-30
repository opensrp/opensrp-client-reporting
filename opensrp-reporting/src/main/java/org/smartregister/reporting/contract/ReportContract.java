package org.smartregister.reporting.contract;

import org.smartregister.reporting.domain.IndicatorQuery;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.domain.ReportIndicator;

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

        void scheduleRecurringTallyJob();
    }

    interface Interactor {
        void scheduleDailyTallyJob();
    }

    interface Model {

        void addIndicator(ReportIndicator indicator);

        void addIndicatorQuery(IndicatorQuery indicatorQuery);

        List<Map<String, IndicatorTally>> getIndicatorsDailyTallies();

    }
}
