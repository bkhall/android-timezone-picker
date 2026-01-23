package com.tyganeutronics.timezonepicker;

import android.os.Bundle;

import com.android.timezonepicker.TimeZonePickerDialog;

import java.util.TimeZone;

import androidx.annotation.Nullable;

public class TimeZonePickerFragment extends TimeZonePickerDialog {

    public static TimeZonePickerFragment newInstance(@Nullable OnTimeZoneSetListener listener) {
        return newInstance(0, null, false, listener);
    }

    public static TimeZonePickerFragment newInstance(long startTime, TimeZone timeZone,
                                                     @Nullable OnTimeZoneSetListener listener) {
        return newInstance(startTime, timeZone, false, listener);
    }

    public static TimeZonePickerFragment newInstance(boolean showNearList,
                                                     @Nullable OnTimeZoneSetListener listener) {
        return newInstance(0, null, showNearList, listener);
    }

    public static TimeZonePickerFragment newInstance(long startTime,
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

        TimeZonePickerFragment fragment = new TimeZonePickerFragment();
        fragment.setArguments(bundle);
        fragment.setOnTimeZoneSetListener(listener);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setShowsDialog(false);
    }

    @Override
    public void dismiss() {
        // Do nothing
    }

    @Override
    public void dismissAllowingStateLoss() {
        // Do nothing
    }
}
