package com.uaroads.osrmrouting.callback;

import com.uaroads.osrmrouting.model.Route;

public class RouteParseEvent {

    public enum Status {
        BEGIN, SUCCESS, FAILURE
    }

    public Status status;
    public Route route;

    public RouteParseEvent(Route route, Status status) {
        this.route = route;
        this.status = status;
    }
}
