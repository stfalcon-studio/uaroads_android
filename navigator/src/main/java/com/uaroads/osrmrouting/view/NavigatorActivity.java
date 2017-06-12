package com.uaroads.osrmrouting.view;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.uaroads.osrmrouting.BusProvider;
import com.uaroads.osrmrouting.Config;
import com.uaroads.osrmrouting.R;
import com.uaroads.osrmrouting.async.RouteDrawTask;
import com.uaroads.osrmrouting.async.RouteParseTask;
import com.uaroads.osrmrouting.callback.RouteDrawEvent;
import com.uaroads.osrmrouting.callback.RouteParseEvent;
import com.uaroads.osrmrouting.model.Instruction;
import com.uaroads.osrmrouting.model.Route;
import com.uaroads.osrmrouting.utils.MapsUtils;
import com.uaroads.osrmrouting.utils.Utils;
import com.uaroads.osrmrouting.view.overlay.UserLocationOverlay;

import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.compass.IOrientationConsumer;
import org.osmdroid.views.overlay.compass.IOrientationProvider;

import java.util.ArrayList;

public class NavigatorActivity extends BaseNavigatorActivity implements GPSDialog.OnDialogDismissCallback {

    private final int GPS_REQUEST_CODE = 101;
    private static final String BETA_DIALOG_SHOWING_KEY = "beta_dialog_showing";

    private MapView mapView;
    private TextView tvNextStreet;
    private TextView tvCurrentStreet;
    private TextView tvDistanceToInstruction;
    private ImageView ivInstruction;
    private TextView tvElapsed;

    private UserLocationOverlay userLocationOverlay;

    private Route route;
    private ArrayList<GeoPoint> instructionPoints;

    private Handler handler;
    private Handler mPolyRedrawHandler;

    private static int instructionPosition = 0;
    private static long elapsedTime;
    private static long elapsedDistance;

    private static long resolvedTimeForInstructions;
    private static long resolvedDistanceForInstructions;

    private Button mZoomInButton;
    private Button mZoomOutButton;
    private Button mLocationButton;

    private LocationTracking mLocationTracking;

    public Polyline polyline;
    public ArrayList<GeoPoint> geoPointArrayList;

    private long deltaDistance;
    private long lastDistance;


    private int deletePointCount = 0;
    private String UAROADS_STATE = STOP_RECORD;


    private LocationTracking.LocationFindCallback mLocationCallback = new LocationTracking.LocationFindCallback() {
        @Override
        public void onLocationFound(Location location) {
            setViewInCurrentLocation(location, MapsUtils.ZOOM_LEVEL);
            setZoomButtonsEnable();
            mLocationButton.setEnabled(true);
        }

        @Override
        public void onLocationNotFound() {
            final GPSDialog dialog = new GPSDialog();
            dialog.show(getSupportFragmentManager(), "GPSDialog");
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigator_activity);

        clearValues();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.routing_toolbar);
        tvElapsed = (TextView) findViewById(R.id.routing_tv_elapsed);
        setSupportActionBar(toolbar);

        this.tvNextStreet = (TextView) findViewById(R.id.routing_tv_next_street);
        this.tvCurrentStreet = (TextView) findViewById(R.id.routing_tv_current_street);
        this.tvDistanceToInstruction = (TextView) findViewById(R.id.routing_tv_distance);
        this.ivInstruction = (ImageView) findViewById(R.id.routing_iv_direction);
        mZoomInButton = (Button) findViewById(R.id.zoom_in_button);
        mZoomOutButton = (Button) findViewById(R.id.zoom_out_button);
        mLocationButton = (Button) findViewById(R.id.exact_location_button);

        configureMapWithTiles();
        userLocationOverlay = MapsUtils.getUserLocationOverlay(this, mapView);
        userLocationOverlay.enableFollowLocation();

        this.handler = new Handler();
        handler.postDelayed(userLocationRunnable, 1000);

        mPolyRedrawHandler = new Handler();
        mPolyRedrawHandler.postDelayed(polylineRedrawRunnable, 1000);

        mLocationTracking = new LocationTracking(this, mLocationCallback);

        mZoomInButton.setOnClickListener(mZoomInListener);
        mZoomOutButton.setOnClickListener(mZoomOutListener);
        mLocationButton.setOnClickListener(mCurrentLocationListener);

        parseRouteJSON();

        notifyUserAboutBetaVersionIfNeed();
        initFeedbackButton();
    }

    private void initFeedbackButton() {
        findViewById(R.id.feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "uaroads@stfalcon.com", null));
                try {
                    startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(NavigatorActivity.this, "No email app installed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void notifyUserAboutBetaVersionIfNeed() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isUserKnown = preferences.getBoolean(BETA_DIALOG_SHOWING_KEY, false);
        if (!isUserKnown) {
            AlertDialog.Builder betaDialog = new AlertDialog.Builder(this);
            betaDialog
                    .setTitle(R.string.beta)
                    .setMessage(getString(R.string.beta_text))
                    .setPositiveButton(getString(R.string.ok), null)
                    .setNegativeButton(R.string.dont_show, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            preferences.edit().putBoolean(BETA_DIALOG_SHOWING_KEY, true).commit();
                        }
                    });
            betaDialog.create().show();
        }
    }


    private View.OnClickListener mZoomInListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mapView.getController().zoomIn();
            setZoomButtonsEnable();

            getTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("Navigator Screen")
                    .setAction("zoomIn")
                    .build());
        }
    };

    private View.OnClickListener mZoomOutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mapView.getController().zoomOut();
            setZoomButtonsEnable();

            getTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("Navigator Screen")
                    .setAction("zoomOut")
                    .build());
        }
    };

    private View.OnClickListener mCurrentLocationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Location location = mLocationTracking.getCurrentLocation();
            setViewInCurrentLocation(location, MapsUtils.ZOOM_LEVEL);

            userLocationOverlay.enableFollowLocation();

            setZoomButtonsEnable();

            getTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("Navigator Screen")
                    .setAction("toСenter")
                    .build());
        }
    };

    private void setViewInCurrentLocation(Location location, int zoomLevel) {
        final double lat = location.getLatitude();
        final double lon = location.getLongitude();
        final GeoPoint geoPoint = new GeoPoint(lat, lon);
        try {
            mapView.getController().setZoom(zoomLevel);
            mapView.getController().animateTo(geoPoint);
        } catch (NullPointerException ignored) {
        }
    }

    private void setZoomButtonsEnable() {
        mZoomInButton.setEnabled((mapView.getZoomLevel() != MapsUtils.MAX_ZOOM));
        mZoomOutButton.setEnabled((mapView.getZoomLevel() != MapsUtils.MIN_ZOOM));
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Navigator Screen")
                .setAction("backPressedButton")
                .build());

        if (!UAROADS_STATE.equals(STOP_RECORD)) {
            actionToUAROADSapp(STOP_RECORD);
        }
        releaseMemory();
        finish();
    }

    private void releaseMemory() {
        route = null;
        instructionPoints = null;
        polyline = null;
        geoPointArrayList = null;
        userLocationOverlay = null;
        mapView.getTileProvider().clearTileCache();
        System.gc();
    }

    @Override
    public void onStart() {
        super.onStart();
        mLocationTracking.connectClient();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationTracking.disconnectClient();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getBus().register(this);

        if (!Utils.isGPSEnabled(this)) {
            final GPSDialog dialog = new GPSDialog();
            dialog.show(getSupportFragmentManager(), "gpsDialog");
            return;
        }

        if (userLocationOverlay == null) {
            userLocationOverlay = MapsUtils.getUserLocationOverlay(this, mapView);
            userLocationOverlay.enableFollowLocation();
        }

        handler.postDelayed(userLocationRunnable, 1000);
        mPolyRedrawHandler.postDelayed(polylineRedrawRunnable, 1000);
        startTrackingOrientation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getBus().unregister(this);
        handler.removeCallbacks(userLocationRunnable);
        mPolyRedrawHandler.removeCallbacks(polylineRedrawRunnable);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearValues();

        if (!UAROADS_STATE.equals(STOP_RECORD)) {
            actionToUAROADSapp(STOP_RECORD);
        }
    }

    private void clearValues() {
        elapsedDistance = 0;
        elapsedTime = 0;
        instructionPosition = 0;
        resolvedDistanceForInstructions = 0;
        resolvedTimeForInstructions = 0;
        deletePointCount = 0;
    }

    /**
     * Parse route json from UARoads app
     */
    private void parseRouteJSON() {
        final Intent intent = getIntent();

        if (intent == null || !intent.hasExtra(Config.EXTRA_DATA)) return;

        final String json = intent.getExtras().getString(Config.EXTRA_DATA);
        handler.post(new RouteParseTask(json));
    }

    /**
     * Success Callback from @GPSDialog
     */
    @Override
    public void onAcceptEnableGPS() {
        final Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, GPS_REQUEST_CODE);
    }

    /**
     * Configure map with base tiles (OSMTiles, UARoads tiles)
     */
    private void configureMapWithTiles() {
        mapView = (MapView) findViewById(R.id.routing_mapview);
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(false);
        mapView.setMinZoomLevel(MapsUtils.MIN_ZOOM);
        mapView.setMaxZoomLevel(MapsUtils.MAX_ZOOM);
        mapView.setScrollableAreaLimit(mapView.getScrollableAreaLimit());

        MapsUtils.addTilesOverlayToMap(this, mapView);
    }

    /**
     * Start tracking device orientation
     */
    private void startTrackingOrientation() {
        final IOrientationProvider orientationProvider = new CustomCompassOrientationProvider(this);
        orientationProvider.startOrientationProvider(orientationConsumer);
    }

    /**
     * Add exist polyline to map
     *
     * @param polyline - route (path)
     */
    private void drawRouteOnMap(Polyline polyline) {
        if (mapView == null) return;
        mapView.getOverlays().add(polyline);
        mapView.invalidate();
    }

    /**
     * Map orientation callback (Based on android sensors)
     */
    private IOrientationConsumer orientationConsumer = new IOrientationConsumer() {

        @Override
        public void onOrientationChanged(float orientation, IOrientationProvider source) {
            //don`t need rotate map
            /*if (mapView != null) {
                final float inversionOrientation = -orientation;
                final float prevOrientation = mapView.getMapOrientation();
                if (Math.abs(prevOrientation - inversionOrientation) < 5f) return;

                mapView.setMapOrientation(inversionOrientation);
            }*/
        }
    };

    /**
     * Get instruction points from polyline (for instruction area tracking)
     *
     * @param route - current route
     */
    @SuppressWarnings("all")
    private void addRouteInstructionPointsToLocationManager(Route route) {
        final ArrayList<GeoPoint> points = route.decodePolylineToPoints();

        this.instructionPoints = new ArrayList<>();
        for (int i = 0; i < route.instructions.size(); i++) {
            final Instruction instruction = route.instructions.get(i);
            final GeoPoint instructionPoint = points.get(instruction.positionInPolyline);

            instructionPoints.add(instructionPoint);
        }
    }

    /**
     * @param position - position of instruction in Instructions list
     */
    private void showInstruction(int position) {
        if (route.instructions.size() - 1 < position) return;

        Log.d("Debug", "Show instruction with position: " + position);

        instructionPosition = position;
        final Instruction instruction = route.instructions.get(position);

        final int nextStreetNameVisibility = TextUtils.isEmpty(instruction.nextStreetName) ? View.GONE : View.VISIBLE;
        final int currentStreetNameVisibility = TextUtils.isEmpty(instruction.currentStreetName) ? View.GONE : View.VISIBLE;

        tvNextStreet.setText(instruction.nextStreetName);
        tvDistanceToInstruction.setText(Utils.getFormattedDistance(this, instruction.distanceToDirection));
        tvCurrentStreet.setText(instruction.currentStreetName);
        ivInstruction.setBackgroundResource(instruction.getInstructionImageId());

        tvNextStreet.setVisibility(nextStreetNameVisibility);
        tvCurrentStreet.setVisibility(currentStreetNameVisibility);
    }

    /**
     * Route parse callback
     */
    @SuppressWarnings("all")
    public void onEventMainThread(RouteParseEvent event) {

        switch (event.status) {
            case BEGIN:
                //showProgress();
                break;
            case SUCCESS:
                this.route = event.route;
                geoPointArrayList = event.route.geoPointArrayList;
                addRouteInstructionPointsToLocationManager(route);
                new RouteDrawTask(getApplicationContext(), route).execute();
                break;
            case FAILURE:
                //hideProgress();
                Log.d("Debug", "Route parse failure");
                break;
            default:
                break;
        }
    }

    /**
     * Route draw callback
     */
    @SuppressWarnings("all")
    public void onEventMainThread(RouteDrawEvent event) {

        switch (event.status) {
            case BEGIN:
                //showProgress();
                break;
            case SUCCESS:
                polyline = event.polyline;
                drawRouteOnMap(polyline);
                addMarkerToLastPoint();
                redrawArrow();
                showInstruction(0);
                break;
            case FAILURE:
                //hideProgress();
                break;
            default:
                break;
        }
    }

    /**
     * Redraw an arrow to lay above  polyline
     */
    private void redrawArrow() {
        mapView.getOverlays().remove(userLocationOverlay);
        mapView.getOverlays().add(userLocationOverlay);
    }

    private void addMarkerToLastPoint() {

        if (geoPointArrayList == null || mapView == null) return;

        final GeoPoint geoPoint = geoPointArrayList.get(geoPointArrayList.size() - 1);

        final ArrayList<OverlayItem> overlayItemList = new ArrayList<>();

        final OverlayItem overlayItem = new OverlayItem(getString(R.string.text_last_point_title), "", geoPoint);
        final Drawable marker = ContextCompat.getDrawable(this, R.drawable.flag_pin_150);

        overlayItem.setMarker(marker);
        overlayItemList.add(overlayItem);

        final ItemizedIconOverlay<OverlayItem> iconOverlay = new ItemizedIconOverlay<>(this, overlayItemList, null);
        mapView.getOverlays().add(iconOverlay);
    }


    private Runnable userLocationRunnable = new Runnable() {

        @Override
        public void run() {
            if (userLocationOverlay == null) {
                handler.postDelayed(this, 10);
                return;
            }

            final Location location = userLocationOverlay.getLastFix();
            if (location == null) {
                handler.postDelayed(this, 10);
                return;
            }

            processRouteResolvedTimeAndDistance(location);
            handler.postDelayed(this, 10);
        }
    };

    private Runnable polylineRedrawRunnable = new Runnable() {
        @Override
        public void run() {
            if (userLocationOverlay == null) {
                mPolyRedrawHandler.postDelayed(this, 10);
                return;
            }

            final Location location = userLocationOverlay.getLastFix();
            if (location == null) {
                mPolyRedrawHandler.postDelayed(this, 10);
                return;
            }

            redrawRoute(location);
            mPolyRedrawHandler.postDelayed(this, 10);
        }
    };

    private void redrawRoute(Location location) {
        if (route == null) return;

        final double lon = location.getLongitude();
        final double lat = location.getLatitude();
        final GeoPoint userPosition = new GeoPoint(lat, lon);

        if (geoPointArrayList.size() == 0) return;

        //my check
        checkForMissingPoints(userPosition);

        final GeoPoint positionToGo = geoPointArrayList.get(0);

        if (userPosition.distanceTo(positionToGo) < MapsUtils.DISTANCE_TO_POINT) {

            geoPointArrayList.remove(positionToGo);
            mapView.getOverlays().remove(polyline);

            polyline = MapsUtils.createPolyline(this, geoPointArrayList,
                    R.color.color_semitransparent_green, MapsUtils.POLY_WIDTH);

            drawRouteOnMap(polyline);

            redrawArrow();

            deletePointCount++;

            setNextNearestInstruction();

            actionToUAROADSapp(START_RECORD);
        }

    }

    private void setNextNearestInstruction() {
        for (int i = 0; i < route.instructions.size(); i++) {
            final int instructionPointIndex = route.instructions.get(i).positionInPolyline;

            if (deletePointCount <= instructionPointIndex) {
                showInstruction(i);
                calculateElapsedValuesWhenExitingFromInstruction(i);
                return;
            }
        }
    }

    private void checkForMissingPoints(GeoPoint userPosition) {
        long distanceToNextPoint = userPosition.distanceTo(geoPointArrayList.get(0));

        if (lastDistance == 0) {
            lastDistance = distanceToNextPoint;
        }

        if (distanceToNextPoint - lastDistance > 0) {
            deltaDistance = deltaDistance + distanceToNextPoint;
        }

        if (deltaDistance > MapsUtils.DISTANCE_TO_POINT) {
            findMostNearestPoint(userPosition);
            deltaDistance = 0;
        }

        lastDistance = distanceToNextPoint;
    }

    private void findMostNearestPoint(GeoPoint userPosition) {
        //todo optimize geo search
        long lastDistanceToPoint = userPosition.distanceTo(geoPointArrayList.get(geoPointArrayList.size() - 1));
        int pointIndex = 0;
        for (int i = 0; i < geoPointArrayList.size(); i++) {
            long distance = userPosition.distanceTo(geoPointArrayList.get(i));
            if (distance < lastDistanceToPoint) {
                pointIndex = i;
                lastDistanceToPoint = distance;
            }
        }
        //todo add marker to nearest point and warning instruction
        clearMissedPoints(pointIndex);
        setNextNearestInstruction();
    }

    private void clearMissedPoints(int pointIndex) {
        deletePointCount = deletePointCount + pointIndex;
        geoPointArrayList.subList(0, pointIndex).clear();
    }

    private void processRouteResolvedTimeAndDistance(Location userLocation) {
        final long resolvedDistanceInInstruction = getResolvedDistanceInInstruction(userLocation);
        final long resolvedTimeInInstruction = getResolvedTimeInInstruction(resolvedDistanceInInstruction);

        if (route != null) {
            elapsedDistance = route.distance - resolvedDistanceInInstruction - resolvedDistanceForInstructions;
            elapsedTime = route.duration - resolvedTimeInInstruction - resolvedTimeForInstructions;

            showElapsedTimeAndDistance();
        }
    }

    private long getResolvedDistanceInInstruction(Location userLocation) {
        if (instructionPoints == null || instructionPoints.size() - 1 < instructionPosition)
            return 0;

        final GeoPoint userPoint = new GeoPoint(userLocation.getLatitude(), userLocation.getLongitude());
        final GeoPoint instructionPoint = instructionPoints.get(instructionPosition);
        final Instruction instructionInPosition = route.instructions.get(instructionPosition);
        final long userDistanceToInstructionPoint = userPoint.distanceTo(instructionPoint);

        showElapsedDistanceInInstruction(userDistanceToInstructionPoint);

        return instructionInPosition.distanceToDirection - userDistanceToInstructionPoint;
    }

    private long getResolvedTimeInInstruction(long resolvedDistance) {
        if (route == null || route.instructions == null || route.instructions.size() - 1 < instructionPosition)
            return 0;

        final Instruction instructionInPosition = route.instructions.get(instructionPosition);
        return resolvedDistance * instructionInPosition.timeToDirection / instructionInPosition.distanceToDirection;
    }

    private void calculateElapsedValuesWhenExitingFromInstruction(int position) {
        if (route.instructions.size() - 1 < position) return;

        final Instruction instruction = route.instructions.get(position);

        resolvedDistanceForInstructions += instruction.distanceToDirection;
        resolvedTimeForInstructions += instruction.timeToDirection;

        elapsedTime = route.duration - resolvedTimeForInstructions;
        elapsedDistance = route.distance - resolvedDistanceForInstructions;

        showElapsedTimeAndDistance();
    }

    @SuppressWarnings("all")
    private void showElapsedTimeAndDistance() {
        final String formattedTime = Utils.getFormattedTime(this, elapsedTime);
        final SpannableString formattedDistance = Utils.getFormattedDistance(this, elapsedDistance);

        tvElapsed.setText(formattedTime + ", " + formattedDistance);
    }

    @SuppressWarnings("all")
    private void showElapsedDistanceInInstruction(long elapsedDistanceToInstruction) {
        tvDistanceToInstruction.setText(Utils.getFormattedDistance(this, elapsedDistanceToInstruction));
    }


    public static final String START_RECORD = "start_with_navigator";
    public static final String STOP_RECORD = "stop_with_navigator";

    private void actionToUAROADSapp(String action) {
        if (!UAROADS_STATE.equals(action)) {
            Intent intent = new Intent();
            intent.setAction(action);
            intent.setComponent(new ComponentName("com.stfalcon.new_uaroads_android",
                    "com.stfalcon.new_uaroads_android.features." +
                            "record.service.RecordService"));
            intent.putExtra("action", action);
            startService(intent);
            UAROADS_STATE = action;

            getTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("Navigator Screen")
                    .setAction("UAROADS_STATE" + " record in navigator")
                    .build());
        }
    }
}
