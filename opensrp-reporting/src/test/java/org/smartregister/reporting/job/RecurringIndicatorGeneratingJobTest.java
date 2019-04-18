package org.smartregister.reporting.job;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 22)
public class RecurringIndicatorGeneratingJobTest {

    private RecurringIndicatorGeneratingJob recurringIndicatorGeneratingJob;
    @Mock
    private Context context;
    @Mock
    private ComponentName componentName;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        recurringIndicatorGeneratingJob = new RecurringIndicatorGeneratingJob();
    }

    @Test
    public void recurringIndicatorGeneratingJobStartsIndicatorGeneratorIntentService() {
        RecurringIndicatorGeneratingJob indicatorGeneratingJobSpy = Mockito.spy(recurringIndicatorGeneratingJob);
        ArgumentCaptor<Intent> intent = ArgumentCaptor.forClass(Intent.class);
        Mockito.doReturn(context).when(indicatorGeneratingJobSpy).getApplicationContext();
        Mockito.doReturn(componentName).when(context).startService(ArgumentMatchers.any(Intent.class));

        indicatorGeneratingJobSpy.onRunJob(null);

        Mockito.verify(context).startService(intent.capture());
        Assert.assertEquals("org.smartregister.reporting.service.IndicatorGeneratorIntentService", intent.getValue().getComponent().getClassName());
    }
}
