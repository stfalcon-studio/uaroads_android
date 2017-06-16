package com.uaroads.osrmrouting.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.uaroads.osrmrouting.Config;
import com.uaroads.osrmrouting.view.overlay.UserLocationOverlay;

import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;

import java.util.ArrayList;
import java.util.List;

public class MapsUtils {

    public static final int MIN_ZOOM = 0; //Min zoom level
    public static final int MAX_ZOOM = 18; // Max zoom level

    public static final int ZOOM_LEVEL = 17;

    public static final int POLY_WIDTH = 20;

    public static final int DISTANCE_TO_POINT = 20;

    /**
     * Add tiles overlay to map
     */
    public static void addTilesOverlayToMap(Context context, MapView mapView) {
        final String[] tilesSources = new String[]{Config.TILES_API, Config.UA_ROADS_TILES_API};

        final MapTileProviderBasic mapTileProvider = new MapTileProviderBasic(context);
        final ITileSource tileSource = new XYTileSource(Config.TILE_SOURCE_NAME, null, MIN_ZOOM, MAX_ZOOM, Config.TILES_SIZE, Config.TILE_FORMAT, tilesSources);

        mapTileProvider.setTileSource(tileSource);

        final TilesOverlay tilesOverlay = new TilesOverlay(mapTileProvider, context);
        tilesOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);

        mapView.getOverlays().add(tilesOverlay);
    }

    /**
     * Add user location overlay to map
     */
    public static UserLocationOverlay getUserLocationOverlay(Context context, MapView mapView) {
        final List<Overlay> overlays = mapView.getOverlays();

        // Delete user location overlay (if already exist)
        for (Overlay overlay : overlays) {
            if (overlay instanceof UserLocationOverlay)
                mapView.getOverlays().remove(overlay);
        }

        final GpsMyLocationProvider userLocationProvider = new GpsMyLocationProvider(context);
        userLocationProvider.setLocationUpdateMinTime(1000);

        final UserLocationOverlay userLocationOverlay = new UserLocationOverlay(context, userLocationProvider, mapView);
        userLocationOverlay.enableMyLocation();
        userLocationOverlay.setDrawAccuracyEnabled(false);

        mapView.getOverlays().add(userLocationOverlay);

        return userLocationOverlay;
    }

    public static Polyline createPolyline(Context context, ArrayList<GeoPoint> geoPointArrayList,
                                          int color, int width) {
        Polyline polyline = new Polyline(context);
        polyline.setPoints(geoPointArrayList);
        polyline.setColor(ContextCompat.getColor(context, color));
        polyline.setWidth(width);
        return polyline;
    }
}
