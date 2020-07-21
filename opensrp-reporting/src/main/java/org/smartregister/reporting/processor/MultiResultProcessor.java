package org.smartregister.reporting.processor;

import androidx.annotation.NonNull;

import org.smartregister.reporting.domain.CompositeIndicatorTally;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.exception.MultiResultProcessorException;

import java.util.List;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2019-07-10
 */

public interface MultiResultProcessor {

    boolean canProcess(int cols, @NonNull String[] colNames);

    @NonNull
    List<IndicatorTally> processMultiResultTally(@NonNull CompositeIndicatorTally compositeIndicatorTally) throws MultiResultProcessorException;
}
