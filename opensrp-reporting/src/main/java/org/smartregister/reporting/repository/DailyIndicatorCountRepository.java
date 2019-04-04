package org.smartregister.reporting.repository;


import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;

/**
 * This DailyIndicatorCountRepository class handles saving daily computed indicator values
 * These values will consist the datetime of saving, the key and value
 *
 * @author allan
 *
 */

public class DailyIndicatorCountRepository extends BaseRepository {

    public static int ID;
    public static String INDICATOR_CODE;
    public static int INDICATOR_VALUE;
    public static String CREATED_AT;

    public DailyIndicatorCountRepository(Repository repository) {
        super(repository);
    }

    // TODO :: Add save a computed indicator value for a specific indicator


    // TODO :: build daily count -> sum for each indicator
}
