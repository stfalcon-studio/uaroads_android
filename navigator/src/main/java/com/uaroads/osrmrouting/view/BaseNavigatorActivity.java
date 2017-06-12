package com.uaroads.osrmrouting.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by alexandr on 15/06/15.
 */
abstract class BaseNavigatorActivity extends AppCompatActivity {

    public static final String TRACKER = "UA-44978148-13";

    private final String TAG = BaseNavigatorActivity.class.getName();
    private Tracker mTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Obtain the shared Tracker instance.
        getDefaultTracker();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
            if (this instanceof NavigatorActivity) {
                mTracker.setScreenName(NavigatorActivity.class.getSimpleName());
            }
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    synchronized public Tracker getDefaultTracker() {
        try {
            if (mTracker == null) {
                GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
                // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
                mTracker = analytics.newTracker(TRACKER);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mTracker;
    }

    public Tracker getTracker(){
      return mTracker;
    }
}