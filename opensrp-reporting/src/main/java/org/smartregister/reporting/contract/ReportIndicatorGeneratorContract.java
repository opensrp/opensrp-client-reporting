package org.smartregister.reporting.contract;

import org.smartregister.reporting.model.IndicatorTally;

import java.util.List;

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
    }

    interface Model {
        // Should use the Dao
        List<IndicatorTally> getIndicatorsDailyTallies();
    }
}
