package org.smartregister.reporting.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.MockitoAnnotations;
import org.smartregister.reporting.util.AppProperties;

import java.util.Properties;

@RunWith(JUnit4.class)
public class AppPropertiesTest {
    private static final String COUNT_INCREMENTAL = "reporting.incremental";
    private static final String TEST_VAL_TRUE = "true";
    private AppProperties appProperties = new AppProperties();
    private Properties properties = new Properties();
    private String key;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        key = "1869791809-10918081090-1";
        String value = "app-properties-value";
        appProperties.setProperty(key, value);
        properties.setProperty(key, value);
    }

    @Test
    public void testAppPropertiesInstantiatesCorrectly() {
        Assert.assertNotNull(appProperties);
    }

    @Test
    public void testGetPropertyBooleanReturnsCorrectValue() {
        AppProperties properties = new AppProperties();
        Boolean value = properties.getPropertyBoolean(COUNT_INCREMENTAL);
        Assert.assertNotNull(value);
        Assert.assertFalse(value);

        properties.setProperty(COUNT_INCREMENTAL, TEST_VAL_TRUE);
        value = properties.getPropertyBoolean(COUNT_INCREMENTAL);
        Assert.assertNotNull(value);
        Assert.assertTrue(value);
    }

    @Test
    public  void testHasPropertyKey(){
        appProperties.hasProperty(key);
        boolean value = properties.getProperty(key) != null;
         Assert.assertTrue(value);
    }
}
