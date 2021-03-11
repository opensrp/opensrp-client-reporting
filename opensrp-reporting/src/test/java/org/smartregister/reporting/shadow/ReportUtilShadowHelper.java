package org.smartregister.reporting.shadow;

import android.content.Context;
import android.view.View;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.smartregister.reporting.domain.ReportingIndicatorDisplayOptions;
import org.smartregister.reporting.factory.IndicatorVisualisationFactory;
import org.smartregister.reporting.util.ReportingUtil;

@Implements(ReportingUtil.class)
public class ReportUtilShadowHelper {



    @Implementation
    public static View getIndicatorView(ReportingIndicatorDisplayOptions displayOptions,
                                        IndicatorVisualisationFactory visualisationFactory, Context context) {
        return visualisationFactory.getIndicatorView(displayOptions, context);
    }

}
