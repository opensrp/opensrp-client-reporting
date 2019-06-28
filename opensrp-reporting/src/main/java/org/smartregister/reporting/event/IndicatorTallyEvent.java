package org.smartregister.reporting.event;

import org.smartregister.reporting.domain.TallyStatus;

public class IndicatorTallyEvent extends BaseEvent {

    private TallyStatus status;

    public TallyStatus getStatus() {
        return status;
    }

    public void setStatus(TallyStatus status) {
        this.status = status;
    }
}
