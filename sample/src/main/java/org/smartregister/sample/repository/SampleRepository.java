package org.smartregister.sample.repository;

import android.content.Context;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.reporting.ReportingLibrary;
import org.smartregister.reporting.domain.IndicatorTally;
import org.smartregister.reporting.repository.DailyIndicatorCountRepository;
import org.smartregister.reporting.repository.IndicatorQueryRepository;
import org.smartregister.reporting.repository.IndicatorRepository;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.Repository;
import org.smartregister.sample.utils.ChartUtil;

public class SampleRepository extends Repository {

    private SQLiteDatabase readableDatabase;
    private SQLiteDatabase writableDatabase;
    private String databasePassword = "db_pass";
    private static final String TAG = SampleRepository.class.getCanonicalName();
    private Context context;

    public SampleRepository(Context context, org.smartregister.Context openSRPContext) {
        super(context, AllConstants.DATABASE_NAME, AllConstants.DATABASE_VERSION, openSRPContext.session(), null, openSRPContext.sharedRepositoriesArray());
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        EventClientRepository.createTable(database, EventClientRepository.Table.client, EventClientRepository.client_column.values());
        EventClientRepository.createTable(database, EventClientRepository.Table.event, EventClientRepository.event_column.values());
        IndicatorRepository.createTable(database);
        IndicatorQueryRepository.createTable(database);
        DailyIndicatorCountRepository.createTable(database);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return getReadableDatabase(databasePassword);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return getWritableDatabase(databasePassword);
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase(String password) {
        try {
            if (readableDatabase == null || !readableDatabase.isOpen()) {
                if (readableDatabase != null) {
                    readableDatabase.close();
                }
                readableDatabase = super.getReadableDatabase(password);
            }
            return readableDatabase;
        } catch (Exception e) {
            Log.e(TAG, "Database Error. " + e.getMessage());
            return null;
        }

    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase(String password) {
        if (writableDatabase == null || !writableDatabase.isOpen()) {
            if (writableDatabase != null) {
                writableDatabase.close();
            }
            writableDatabase = super.getWritableDatabase(password);
        }
        return writableDatabase;
    }

    @Override
    public synchronized void close() {
        if (readableDatabase != null) {
            readableDatabase.close();
        }

        if (writableDatabase != null) {
            writableDatabase.close();
        }
        super.close();
    }

    public static void addSampleData() {
        addSampleIndicatorDailyTally();
        addSampleEvent();
    }

    private static void addSampleIndicatorDailyTally() {
        DailyIndicatorCountRepository dailyIndicatorCountRepository = ReportingLibrary.getInstance().dailyIndicatorCountRepository();
        dailyIndicatorCountRepository.add(new IndicatorTally(null, 80, ChartUtil.numericIndicatorKey, null));
        dailyIndicatorCountRepository.add(new IndicatorTally(null, 60, ChartUtil.pieChartYesIndicatorKey, null));
        dailyIndicatorCountRepository.add(new IndicatorTally(null, 20, ChartUtil.pieChartNoIndicatorKey, null));
    }

    private static void addSampleEvent() {
        EventClientRepository eventClientRepository = ReportingLibrary.getInstance().eventClientRepository();
        String eventJSONString = "{\"baseEntityId\":\"3b6048d5-231d-4a11-a141-4c4358b8e401\",\"duration\":0,\"entityType\":\"ec_woman\",\"eventDate\":\"2019-04-15T00:00:00.000Z\",\n" +
                "\"eventType\":\"ANC Registration\",\"formSubmissionId\":\"f297be35-de9c-4749-9ff4-a963e17d0680\",\"locationId\":\"8d6c993e-c2cc-11de-8d13-0010c6dffd0f\",\n" +
                "\"obs\":[{\"fieldCode\":\"dob_calculated\",\"fieldDataType\":\"text\",\"fieldType\":\"formsubmissionField\",\"formSubmissionField\":\"dob_calculated\",\n" +
                "\"humanReadableValues\":[],\"parentCode\":\"\",\"values\":[\"0\"]},{\"fieldCode\":\"dob_entered\",\"fieldDataType\":\"text\",\"fieldType\":\"formsubmissionField\",\n" +
                "\"formSubmissionField\":\"dob_entered\",\"humanReadableValues\":[],\"parentCode\":\"\",\"values\":[\"15-04-2001\"]},{\"fieldCode\":\"age_calculated\",\n" +
                "\"fieldDataType\":\"text\",\"fieldType\":\"formsubmissionField\",\"formSubmissionField\":\"age_calculated\",\"humanReadableValues\":[],\"parentCode\":\"\",\"values\":[\"18.0\"]},\n" +
                "{\"fieldCode\":\"159635AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"fieldDataType\":\"text\",\"fieldType\":\"concept\",\"formSubmissionField\":\"phone_number\",\"humanReadableValues\":[],\n" +
                "\"parentCode\":\"\",\"values\":[\"0899100100\"]},{\"fieldCode\":\"163164AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"fieldDataType\":\"select one\",\"fieldType\":\"concept\",\n" +
                "\"formSubmissionField\":\"reminders\",\"humanReadableValues\":[\"No\"],\"parentCode\":\"\",\"values\":[\"1066AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"]},\n" +
                "{\"fieldCode\":\"last_interacted_with\",\"fieldDataType\":\"text\",\"fieldType\":\"formsubmissionField\",\"formSubmissionField\":\"last_interacted_with\",\n" +
                "\"humanReadableValues\":[],\"parentCode\":\"\",\"values\":[\"1555330141549\"]},{\"fieldCode\":\"163137AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"fieldDataType\":\"start\",\n" +
                "\"fieldType\":\"concept\",\"formSubmissionField\":\"start\",\"humanReadableValues\":[],\"parentCode\":\"\",\"values\":[\"2019-04-15 15:08:35\"]},\n" +
                "{\"fieldCode\":\"163138AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"fieldDataType\":\"end\",\"fieldType\":\"concept\",\"formSubmissionField\":\"end\",\"humanReadableValues\":[],\n" +
                "\"parentCode\":\"\",\"values\":[\"2019-04-15 15:09:01\"]},{\"fieldCode\":\"163149AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"fieldDataType\":\"deviceid\",\"fieldType\":\"concept\",\n" +
                "\"formSubmissionField\":\"deviceid\",\"humanReadableValues\":[],\"parentCode\":\"\",\"values\":[\"358240051111110\"]},{\"fieldCode\":\"163150AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\n" +
                "\"fieldDataType\":\"subscriberid\",\"fieldType\":\"concept\",\"formSubmissionField\":\"subscriberid\",\"humanReadableValues\":[],\"parentCode\":\"\",\"values\":[\"310260000000000\"]},\n" +
                "{\"fieldCode\":\"163151AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"fieldDataType\":\"simserial\",\"fieldType\":\"concept\",\"formSubmissionField\":\"simserial\",\"humanReadableValues\":[],\n" +
                "\"parentCode\":\"\",\"values\":[\"89014103211118510720\"]},{\"fieldCode\":\"163152AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"fieldDataType\":\"phonenumber\",\"fieldType\":\"concept\",\n" +
                "\"formSubmissionField\":\"phonenumber\",\"humanReadableValues\":[],\"parentCode\":\"\",\"values\":[\"+15555215554\"]}],\"providerId\":\"testy\",\"team\":\"OnaTestTeam\",\n" +
                "\"teamId\":\"a2dc9f7d-3465-4dd4-9681-a9106c633f53\",\"version\":1,\"clientApplicationVersion\":1,\"clientDatabaseVersion\":1,\"dateCreated\":\"2019-04-15T15:09:01.558Z\",\"type\":\"Event\"}";
        String baseEntityId = "3b6048d5-231d-4a11-a141-4c4358b8e401";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(eventJSONString);
        } catch (JSONException ex) {
            Log.e(TAG, "Error creating Event JSONObject");
        }
        eventClientRepository.addEvent(baseEntityId, jsonObject);
    }


}
