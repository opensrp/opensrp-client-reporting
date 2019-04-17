package org.smartregister.reporting.job;

import com.evernote.android.job.Job;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IndicatorGeneratorJobCreatorTest {

    private IndicatorGeneratorJobCreator jobCreator;

    @Before
    public void setUp() {
        jobCreator = new IndicatorGeneratorJobCreator();
    }

    @Test
    public void indicatorGeneratorJobcreator_returnsRecurringIndicatorGeneratingJob() {
        Job jobCreated = jobCreator.create(RecurringIndicatorGeneratingJob.TAG);
        Assert.assertNotNull(jobCreated);
        Assert.assertTrue(jobCreated instanceof RecurringIndicatorGeneratingJob);
    }
}
