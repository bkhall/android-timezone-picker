package com.tyganeutronics.timezonepicker;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.TimeZone;

import androidx.annotation.Nullable;

public class TimeZonePickerDialogFragment extends com.android.timezonepicker.TimeZonePickerDialog {

    public final static String TAG = TimeZonePickerDialogFragment.class.getSimpleName();

    public static TimeZonePickerDialogFragment newInstance(@Nullable OnTimeZoneSetListener listener) {
        return newInstance(0, null, false, listener);
    }

    public static TimeZonePickerDialogFragment newInstance(long startTime, TimeZone timeZone,
                                                           @Nullable OnTimeZoneSetListener listener) {
        return newInstance(startTime, timeZone, false, listener);
    }

    public static TimeZonePickerDialogFragment newInstance(boolean showNearList,
                                                           @Nullable OnTimeZoneSetListener listener) {
        return newInstance(0, null, showNearList, listener);
    }

    public static TimeZonePickerDialogFragment newInstance(long startTime,
                                                           @Nullable TimeZone timezone,
                                                           boolean showNearList,
                                                           @Nullable OnTimeZoneSetListener listener) {
        Bundle bundle = new Bundle();
        bundle.putLong(TimeZonePickerDialogFragment.BUNDLE_START_TIME_MILLIS, startTime);
        if (timezone == null) {
            bundle.putString(TimeZonePickerDialogFragment.BUNDLE_TIME_ZONE, null);
        } else {
            bundle.putString(TimeZonePickerDialogFragment.BUNDLE_TIME_ZONE,
                    timezone.toZoneId().getId());
        }
        bundle.putBoolean(TimeZonePickerDialogFragment.BUNDLE_SHOW_NEAR_LIST, showNearList);

        TimeZonePickerDialogFragment fragment = new TimeZonePickerDialogFragment();
        fragment.setArguments(bundle);
        fragment.setOnTimeZoneSetListener(listener);

        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = (int) (metrics.widthPixels * 0.875);

        getDialog().getWindow().setAttributes(params);
    }
}
