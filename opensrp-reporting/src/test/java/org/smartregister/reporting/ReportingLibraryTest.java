package org.smartregister.reporting;

import android.content.res.AssetManager;

import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.Repository;

public class ReportingLibraryTest {

    @Mock
    private Context context;

    @Mock
    private Repository repository;

    @Mock
    private CommonFtsObject commonFtsObject;

    @Mock
    private AllSharedPreferences allSharedPreferences;

    @Mock
    private SQLiteDatabase sqLiteDatabase;

    @Mock
    AssetManager assetManager;

    private int appVersion = 1;
    private int dbVersion = 1;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInit() {
        ReportingLibrary.init(context, repository, commonFtsObject, appVersion, dbVersion);
        Assert.assertNotNull(ReportingLibrary.getInstance());
    }

    @Test
    public void testThatAllRepositoriesAreInitialized() {
        ReportingLibrary.init(context, repository, commonFtsObject, appVersion, dbVersion);
        ReportingLibrary reportingLibrary = ReportingLibrary.getInstance();
        Assert.assertNotNull(reportingLibrary.dailyIndicatorCountRepository());
        Assert.assertNotNull(reportingLibrary.indicatorQueryRepository());
        Assert.assertNotNull(reportingLibrary.eventClientRepository());
        Assert.assertNotNull(reportingLibrary.dailyIndicatorCountRepository());

    }

}
