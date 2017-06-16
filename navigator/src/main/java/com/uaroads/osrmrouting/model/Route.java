package com.uaroads.osrmrouting.model;

import android.text.TextUtils;

import org.osmdroid.bonuspack.utils.PolylineEncoder;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class Route {

    public String name;
    public String polyline;
    public long duration;
    public long distance;
    public ArrayList<Instruction> instructions;
    public ArrayList<GeoPoint> geoPointArrayList;

    public ArrayList<GeoPoint> decodePolylineToPoints() {
        if (TextUtils.isEmpty(polyline)) return new ArrayList<>();
        return PolylineEncoder.decode(polyline, 1, false);
    }
}
