package org.smartregister.reporting.repository;


import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;

/**
 * This DailyIndicatorCountRepository class handles saving daily computed indicator values
 *
 * @author allan
 */

public class DailyIndicatorCountRepository extends BaseRepository {

    public DailyIndicatorCountRepository(Repository repository) {
        super(repository);
    }

    // TODO :: Add save a computed indicator value for a specific indicator


    // TODO :: build daily count -> sum for each indicator
}
