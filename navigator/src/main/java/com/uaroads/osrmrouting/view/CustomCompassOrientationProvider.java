package com.uaroads.osrmrouting.view;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.osmdroid.views.overlay.compass.IOrientationConsumer;
import org.osmdroid.views.overlay.compass.IOrientationProvider;

public class CustomCompassOrientationProvider  implements SensorEventListener, IOrientationProvider {

    static final float ALPHA = 0.15f;

    private IOrientationConsumer mOrientationConsumer;
    private final SensorManager mSensorManager;
    private float mAzimuth;

    public CustomCompassOrientationProvider(Context context)
    {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    //
    // IOrientationProvider
    //

    /**
     * Enable orientation updates from the internal compass sensor and show the compass.
     */
    @Override
    public boolean startOrientationProvider(IOrientationConsumer orientationConsumer)
    {
        mOrientationConsumer = orientationConsumer;
        boolean result = false;

        final Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        if (sensor != null) {
            result = mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        }
        return result;
    }

    @Override
    public void stopOrientationProvider()
    {
        mOrientationConsumer = null;
        mSensorManager.unregisterListener(this);
    }

    @Override
    public float getLastKnownOrientation()
    {
        return mAzimuth;
    }

    //
    // SensorEventListener
    //

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy)
    {
        // This is not interesting for us at the moment
    }

    private float[] accelVals;

    @Override
    public void onSensorChanged(final SensorEvent event)
    {

        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
            accelVals = lowPass( event.values.clone(), accelVals );

        if (accelVals != null) {
            mAzimuth = accelVals[0];
            if (mOrientationConsumer != null)
                mOrientationConsumer.onOrientationChanged(mAzimuth, this);

        }
    }

    private float[] lowPass( float[] input, float[] output ) {
        if ( output == null ) return input;

        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }
}
