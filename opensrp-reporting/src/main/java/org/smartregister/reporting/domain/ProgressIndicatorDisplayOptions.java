package org.smartregister.reporting.domain;


public class ProgressIndicatorDisplayOptions  extends ReportingIndicatorDisplayOptions{

    private ProgressIndicatorConfig config;


    public ProgressIndicatorDisplayOptions(ProgressIndicatorConfig progressIndicatorConfig) {
        this.config = progressIndicatorConfig;
    }

    public ProgressIndicatorConfig getConfig() {
        return config;
    }

    public void setConfig(ProgressIndicatorConfig config) {
        this.config = config;
    }

    public static class ProgressIndicatorBuilder {
        private int progressVal;
        private String title;
        private String subtitle;
        private int foregroundColor;
        private int backgroundColor;

        public ProgressIndicatorBuilder withProgressValue(int value) {
            this.progressVal = value;
            return this;
        }

        public ProgressIndicatorBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public ProgressIndicatorBuilder withSubtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public ProgressIndicatorBuilder withForegroundColor(int foregroundColor) {
            this.foregroundColor = foregroundColor;
            return this;
        }

        public ProgressIndicatorBuilder withBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public ProgressIndicatorDisplayOptions build() {
            ProgressIndicatorConfig config = new ProgressIndicatorConfig();
            config.setProgressVal(this.progressVal);
            config.setTitle(this.title);
            config.setSubtitle(this.subtitle);
            config.setForegroundColor(this.foregroundColor);
            config.setBackgroundColor(this.backgroundColor);
            return new ProgressIndicatorDisplayOptions(config);
        }
    }
}
