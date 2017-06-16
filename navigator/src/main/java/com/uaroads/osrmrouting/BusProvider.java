package com.uaroads.osrmrouting;

import de.greenrobot.event.EventBus;

public class BusProvider {

    private static final EventBus bus = new EventBus();

    public static EventBus getBus() {
        return bus;
    }

    private BusProvider() {
        //no implementation
    }

}
