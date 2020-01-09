package org.smartregister.reporting;

import org.smartregister.view.activity.DrishtiApplication;

public class TestApplication extends DrishtiApplication {

    public static void setInstance(DrishtiApplication application) {
        mInstance = application;
    }

    @Override
    public void logoutCurrentUser() {

    }
}
