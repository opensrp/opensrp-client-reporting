package org.smartregister.reporting;

import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;
import org.smartregister.repository.Repository;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class ReportingLibraryTest {

    @Mock
    private Context context;

    @Mock
    private Repository repository;

    @Mock
    private IndicatorRepository indicatorRepository;

    @Mock
    private IndicatorQueryRepository indicatorQueryRepository;


    @Mock
    private CommonFtsObject commonFtsObject;

    @Mock
    private SQLiteDatabase sqliteDatabase;

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

    @Test
    public void truncateIndicatorTablesWithDBInvokesRepositoryTruncate() {
        ReportingLibrary.init(context, repository, commonFtsObject, appVersion, dbVersion);
        ReportingLibrary reportingLibrary = ReportingLibrary.getInstance();
        ReportingLibrary reportingLibrarySpy = Mockito.spy(reportingLibrary);
        // Magic
        Whitebox.setInternalState(reportingLibrarySpy, "indicatorRepository", indicatorRepository);
        Whitebox.setInternalState(reportingLibrarySpy, "indicatorQueryRepository", indicatorQueryRepository);

        reportingLibrarySpy.truncateIndicatorDefinitionTables(sqliteDatabase);
        Mockito.verify(indicatorRepository, Mockito.times(1)).truncateTable(sqliteDatabase);
        Mockito.verify(indicatorQueryRepository, Mockito.times(1)).truncateTable(sqliteDatabase);
    }
}
