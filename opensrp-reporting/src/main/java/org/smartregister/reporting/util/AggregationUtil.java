package org.smartregister.reporting.util;

import org.smartregister.reporting.domain.IndicatorTally;

import java.util.Date;
import java.util.List;
import java.util.Map;

/***
 * @author Elly Nerdstone
 */
public class AggregationUtil {
    public static float getTotalIndicatorCount(List<Map<String, IndicatorTally>> indicatorTallies,
                                               String indicatorKey) {
        float count = 0;
        if (indicatorTallies != null && !indicatorTallies.isEmpty()) {
            for (Map<String, IndicatorTally> indicatorTallyMap : indicatorTallies) {
                for (String key : indicatorTallyMap.keySet()) {
                    if (indicatorKey.equals(key)) {
                        count += indicatorTallyMap.get(key).getFloatCount();
                    }
                }
            }
        }

        return count;
    }

    public static float getLatestIndicatorCount(List<Map<String, IndicatorTally>> indicatorTallies,
                                                String indicatorKey) {
        float count = 0;
        //Back date
        Date currentDate = new Date(Long.MIN_VALUE);

        if (indicatorTallies != null && !indicatorTallies.isEmpty()) {
            for (Map<String, IndicatorTally> indicatorTallyMap : indicatorTallies) {
                for (String key : indicatorTallyMap.keySet()) {
                    if (indicatorKey.equals(key) && indicatorTallyMap.get(key).getCreatedAt() != null &&
                            indicatorTallyMap.get(key).getCreatedAt().after(currentDate)) {
                        currentDate = indicatorTallyMap.get(key).getCreatedAt();
                        count = indicatorTallyMap.get(key).getFloatCount();
                    }
                }
            }
        }
        return count;
    }
}
