package com.uaroads.osrmrouting.view;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class LocationTracking implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private Context mContext;
    public Location mCurrentLocation;

    private LocationFindCallback mCallback;

    public LocationTracking(Context context, LocationFindCallback locationFindCallback) {
        buildGoogleApiClient(context);
        mContext = context;
        mCallback = locationFindCallback;
    }

    private void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void connectClient() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    public void disconnectClient() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mCurrentLocation =  LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mCurrentLocation != null) {
            Log.d("TAG", "Current location: " + mCurrentLocation.toString());
            mCallback.onLocationFound(mCurrentLocation);
            return;
        }
        mCallback.onLocationNotFound();
    }

    public Location getCurrentLocation() {
        return mCurrentLocation;
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(mContext, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(mContext, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            connectClient();
            return;
        }
        Toast.makeText(mContext, "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
    }

    public interface LocationFindCallback {
        void onLocationFound(Location location);
        void onLocationNotFound();
    }
}
