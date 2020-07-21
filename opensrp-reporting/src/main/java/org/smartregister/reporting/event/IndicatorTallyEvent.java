package org.smartregister.reporting.event;

import androidx.annotation.NonNull;

import org.smartregister.reporting.domain.TallyStatus;

public class IndicatorTallyEvent extends BaseEvent {

    private TallyStatus status;

    public IndicatorTallyEvent(@NonNull TallyStatus tallyStatus) {
        this.status = tallyStatus;
    }

    public TallyStatus getStatus() {
        return status;
    }

    public void setStatus(TallyStatus status) {
        this.status = status;
    }
}
