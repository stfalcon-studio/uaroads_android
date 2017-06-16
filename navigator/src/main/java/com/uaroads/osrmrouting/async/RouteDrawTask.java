package com.uaroads.osrmrouting.async;

import android.content.Context;
import android.os.AsyncTask;

import com.uaroads.osrmrouting.BusProvider;
import com.uaroads.osrmrouting.R;
import com.uaroads.osrmrouting.callback.RouteDrawEvent;
import com.uaroads.osrmrouting.model.Route;
import com.uaroads.osrmrouting.utils.MapsUtils;

import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class RouteDrawTask extends AsyncTask<Void, Void, Polyline> {

    private Context context;
    private Route route;

    public RouteDrawTask(Context context, Route route) {
        this.context = context;
        this.route = route;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        BusProvider.getBus().post(new RouteDrawEvent(null, RouteDrawEvent.Status.BEGIN));
    }

    @Override
    protected Polyline doInBackground(Void... voids) {
        if (route == null) return null;

        final ArrayList<GeoPoint> points = route.decodePolylineToPoints();
        if (points == null) return null;

        return MapsUtils.createPolyline(context, points, R.color.color_semitransparent_green, MapsUtils.POLY_WIDTH);
    }

    @Override
    protected void onPostExecute(Polyline polyline) {
        super.onPostExecute(polyline);

        if (polyline != null) {
            BusProvider.getBus().post(new RouteDrawEvent(polyline, RouteDrawEvent.Status.SUCCESS));
            return;
        }

        BusProvider.getBus().post(new RouteDrawEvent(null, RouteDrawEvent.Status.FAILURE));
    }
}
