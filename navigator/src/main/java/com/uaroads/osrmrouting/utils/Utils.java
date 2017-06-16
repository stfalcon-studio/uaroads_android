package com.uaroads.osrmrouting.utils;

import android.content.Context;
import android.location.LocationManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;

import com.uaroads.osrmrouting.R;

import java.util.Calendar;
import java.util.TimeZone;

public class Utils {

    public static final float TOTAL_TIME_STRING_SIZE_RATIO = 1.2f;

    /**
     * Checks for enabled GPS
     *
     * @return true - if GPS is enabled, false - otherwise.
     */
    public static boolean isGPSEnabled(Context context) {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static String getFormattedTime(Context context, long timeInSeconds) {
        if (timeInSeconds < 0) {
            return context.getString(R.string.text_time_format, 1);
        }

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        calendar.setTimeInMillis(timeInSeconds * 1000);

        final int hours = calendar.get(Calendar.HOUR) ;
        int minutes = calendar.get(Calendar.MINUTE);

        if (hours == 0 && minutes == 0) minutes = 1;

        if (hours == 0) {
            return context.getString(R.string.text_time_format, minutes);
        }

        return context.getString(R.string.text_full_time_format, hours, minutes);
    }

    public static SpannableString getFormattedDistance(Context context, long distanceInMeters) {
        SpannableString spannableString;
        if (distanceInMeters < 0) {
            spannableString = new SpannableString(context.getString(R.string.text_m_format, 0));
            spannableString.setSpan(new RelativeSizeSpan(TOTAL_TIME_STRING_SIZE_RATIO), 0, spannableString.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }

        if (distanceInMeters > 1000) {
            final float distance = ((float) distanceInMeters) / 1000;
            final String kmValue = String.format("%.1f", distance);
            spannableString = new SpannableString(context.getString(R.string.text_km_format, kmValue));
            spannableString.setSpan(new RelativeSizeSpan(TOTAL_TIME_STRING_SIZE_RATIO), 0, spannableString.length() - 3,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }

        spannableString = new SpannableString(context.getString(R.string.text_m_format, distanceInMeters));
        spannableString.setSpan(new RelativeSizeSpan(TOTAL_TIME_STRING_SIZE_RATIO), 0, spannableString.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
