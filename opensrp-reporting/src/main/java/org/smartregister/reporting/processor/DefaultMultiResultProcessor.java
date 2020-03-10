package org.smartregister.reporting.processor;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.smartregister.reporting.domain.CompositeIndicatorTally;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.exception.MultiResultProcessorException;

import java.util.ArrayList;
import java.util.List;


/**
 * This processor is able to processor queries where the first column returned is an indicator grouping
 * eg. gender while the second column contains the count. The indicator group should be a string or have default affinity
 * to {@link android.database.Cursor}.FIELD_TYPE_STRING as described https://www.sqlite.org/datatype3.html.
 * <p>
 * The second column should have affinity to either Cursor.FIELD_TYPE_INTEGER or Cursor.FIELD_TYPE_FLOAT in SQLite
 * <p>
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2019-07-10
 */

public class DefaultMultiResultProcessor implements MultiResultProcessor {

    public static final String GROUPING_SEPARATOR = "_";

    @Override
    public boolean canProcess(int cols, @NonNull String[] colNames) {
        return cols == 2 && colNames.length == 2;
    }

    @NonNull
    @Override
    public List<IndicatorTally> processMultiResultTally(@NonNull CompositeIndicatorTally compositeIndicatorTally) throws MultiResultProcessorException {
        ArrayList<Object[]> compositeTallies = new Gson().fromJson(compositeIndicatorTally.getValueSet(), new TypeToken<List<Object[]>>(){}.getType());

        // Remove the column names from processing
        compositeTallies.remove(0);

        List<IndicatorTally> tallies = new ArrayList<>();

        for (Object[] compositeTally: compositeTallies) {
            IndicatorTally indicatorTally = new IndicatorTally();
            indicatorTally.setCreatedAt(compositeIndicatorTally.getCreatedAt());
            indicatorTally.setGrouping(compositeIndicatorTally.getGrouping());

            String indicatorGrouping = String.valueOf(compositeTally[0]);
            indicatorTally.setIndicatorCode(compositeIndicatorTally.getIndicatorCode() + GROUPING_SEPARATOR + indicatorGrouping);

            Object indicatorValue = compositeTally[1];
            if (indicatorValue instanceof Integer) {
                indicatorTally.setCount((int) indicatorValue);
            } else if (indicatorValue instanceof Double) {
                indicatorTally.setCount(((Double) indicatorValue).floatValue());
            } else {
                throw new MultiResultProcessorException(indicatorValue, compositeIndicatorTally);
            }

            tallies.add(indicatorTally);
        }

        return tallies;
    }
}
