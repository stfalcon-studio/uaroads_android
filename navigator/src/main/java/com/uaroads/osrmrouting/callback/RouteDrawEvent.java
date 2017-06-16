package com.uaroads.osrmrouting.callback;

import org.osmdroid.bonuspack.overlays.Polyline;

public class RouteDrawEvent {

    public enum Status {
        BEGIN, SUCCESS, FAILURE
    }

    public Status status;
    public Polyline polyline;

    public RouteDrawEvent(Polyline polyline, Status status) {
        this.polyline = polyline;
        this.status = status;
    }
}
