package org.smartregister.reporting;

import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;
import org.smartregister.repository.Repository;

public class ReportingLibrary {

    private Repository repository;
    private DailyIndicatorCountRepository dailyIndicatorCountRepository;
    private IndicatorQueryRepository indicatorQueryRepository;
    private IndicatorRepository indicatorRepository;
    private Context context;
    private static ReportingLibrary instance;
    private CommonFtsObject commonFtsObject;
    private int applicationVersion;
    private int databaseVersion;

    public static void init(Context context, Repository repository, CommonFtsObject commonFtsObject, int applicationVersion, int databaseVersion) {
        if (instance == null) {
            instance = new ReportingLibrary(context, repository, commonFtsObject, applicationVersion, databaseVersion);
        }
    }

    public static ReportingLibrary getInstance() {
        if (instance == null) {
            throw new IllegalStateException(" Instance does not exist!!! Call " + ReportingLibrary.class.getName() + ".init() in the onCreate() method of your Application class");
        }
        return instance;
    }

    private ReportingLibrary(Context context, Repository repository, CommonFtsObject commonFtsObject, int applicationVersion, int databaseVersion) {
        this.repository = repository;
        this.context = context;
        this.commonFtsObject = commonFtsObject;
        this.applicationVersion = applicationVersion;
        this.databaseVersion = databaseVersion;
    }

    public Repository getRepository() {
        return repository;
    }


    public DailyIndicatorCountRepository dailyIndicatorCountRepository() {
        if (dailyIndicatorCountRepository == null) {
            dailyIndicatorCountRepository = new DailyIndicatorCountRepository(getRepository());
        }

        return dailyIndicatorCountRepository;
    }

    public IndicatorQueryRepository indicatorQueryRepository() {
        if (indicatorQueryRepository == null) {
            indicatorQueryRepository = new IndicatorQueryRepository(getRepository());
        }

        return indicatorQueryRepository;
    }

    public IndicatorRepository indicatorRepository() {
        if (indicatorRepository == null) {
            indicatorRepository = new IndicatorRepository(getRepository());
        }

        return indicatorRepository;
    }

    public Context getContext() {
        return context;
    }

    public CommonFtsObject getCommonFtsObject() {
        return commonFtsObject;
    }

    public int getApplicationVersion() {
        return applicationVersion;
    }

    public int getDatabaseVersion() {
        return databaseVersion;
    }
}
