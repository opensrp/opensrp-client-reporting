package org.smartregister.reporting;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.repository.Repository;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
public class ReportingLibraryTest {

    @Mock
    private Context context;
    @Mock
    private Repository repository;
    @Mock
    private CommonFtsObject commonFtsObject;

    private int appVersion = 1;
    private int dbVersion = 1;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInit() {
        ReportingLibrary.init(context, repository, commonFtsObject, appVersion, dbVersion);
        assertNotNull(ReportingLibrary.getInstance());
    }

    @Test
    public void testThatAllRepositoriesAreInitialized() {
        ReportingLibrary.init(context, repository, commonFtsObject, appVersion, dbVersion);
        ReportingLibrary reportingLibrary = ReportingLibrary.getInstance();
        assertNotNull(reportingLibrary.dailyIndicatorCountRepository());
        assertNotNull(reportingLibrary.indicatorQueryRepository());
        assertNotNull(reportingLibrary.eventClientRepository());
        assertNotNull(reportingLibrary.dailyIndicatorCountRepository());

    }
}
