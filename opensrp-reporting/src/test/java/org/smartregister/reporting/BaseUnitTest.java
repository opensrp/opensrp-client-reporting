package org.smartregister.reporting;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Calendar;
import java.util.Date;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 22)
public abstract class BaseUnitTest {

    public static Date getDate(int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); //Months index begin from 0 - 11 i.e Jan: 0 ; Dec: 11
        calendar.set(Calendar.DATE, date);
        return calendar.getTime();
    }

    public static Date getDateTime(int year, int month, int date, int hour, int min, int sec) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); //Months index begin from 0 - 11 i.e Jan: 0 ; Dec: 11
        calendar.set(Calendar.DATE, date);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);
        return calendar.getTime();
    }
}
