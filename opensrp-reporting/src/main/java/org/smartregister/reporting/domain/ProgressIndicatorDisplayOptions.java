package org.smartregister.reporting.domain;


public class ProgressIndicatorDisplayOptions extends ReportingIndicatorDisplayOptions {

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
        private String indicatorLabel;
        private String progressIndicatorTitle;
        private String progressIndicatorSubtitle;
        private int progressIndicatorTitleColor;
        private int foregroundColor;
        private int backgroundColor;

        public ProgressIndicatorBuilder withProgressValue(int value) {
            this.progressVal = value;
            return this;
        }

        public ProgressIndicatorBuilder withIndicatorLabel(String indicatorLabel) {
            this.indicatorLabel = indicatorLabel;
            return this;
        }

        public ProgressIndicatorBuilder withProgressIndicatorTitle(String title) {
            this.progressIndicatorTitle = title;
            return this;
        }

        public ProgressIndicatorBuilder withProgressIndicatorSubtitle(String subtitle) {
            this.progressIndicatorSubtitle = subtitle;
            return this;
        }

        public ProgressIndicatorBuilder withProgressIndicatorTitleColor(int titleColor) {
            this.progressIndicatorTitleColor = titleColor;
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
            config.setIndicatorLabel(this.indicatorLabel);
            config.setProgressIndicatorTitle(this.progressIndicatorTitle);
            config.setProgressIndicatorTitleColor(this.progressIndicatorTitleColor);
            config.setProgressIndicatorSubtitle(this.progressIndicatorSubtitle);
            config.setForegroundColor(this.foregroundColor);
            config.setBackgroundColor(this.backgroundColor);
            return new ProgressIndicatorDisplayOptions(config);
        }
    }
}
