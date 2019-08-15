package org.smartregister.reporting.utils;

import org.junit.Assert;
import org.junit.Test;
import org.smartregister.reporting.util.AppProperties;

public class AppPropertiesTest {
    private static final String COUNT_INCREMENTAL = "reporting.incremental";
    private static final String TEST_VAL_TRUE = "true";

    @Test
    public void testAppPropertiesInstantiatesCorrectly() {

        AppProperties properties = new AppProperties();
        Assert.assertNotNull(properties);
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
}
