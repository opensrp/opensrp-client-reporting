package org.smartregister.reporting.exception;

import android.support.annotation.NonNull;

import org.smartregister.reporting.domain.CompositeIndicatorTally;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2019-07-10
 */

public class MultiResultProcessorException extends Exception {

    public MultiResultProcessorException() {
        super("CompositeIndicatorTally could not be processed");
    }

    public MultiResultProcessorException(String message) {
        super(message);
    }

    public MultiResultProcessorException(@NonNull Object value, @NonNull CompositeIndicatorTally compositeIndicatorTally) {
        super("CompositeIndicatorTally " + compositeIndicatorTally.getValueSet() + " could not be processed at value " + value);
    }
}
