package org.smartregister.reporting.domain;

import java.util.List;

public class IndicatorsYamlConfig {
    private List<IndicatorYamlConfigItem> indicators;

    public List<IndicatorYamlConfigItem> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<IndicatorYamlConfigItem> indicators) {
        this.indicators = indicators;
    }
}
