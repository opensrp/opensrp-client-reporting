package org.smartregister.reporting.event;

import org.greenrobot.eventbus.EventBus;

public class EventBusHelper {

    public static void postEvent(BaseEvent event) {
        EventBus.getDefault().post(event);
    }

    public static void postStickyEvent(BaseEvent event) {
        EventBus.getDefault().postSticky(event);
    }

    /**
     * Manually clean sticky event
     *
     * @param event The event to be cleaned
     */
    public static void removeStickyEvent(BaseEvent event) {
        EventBus.getDefault().removeStickyEvent(event);

    }
}
