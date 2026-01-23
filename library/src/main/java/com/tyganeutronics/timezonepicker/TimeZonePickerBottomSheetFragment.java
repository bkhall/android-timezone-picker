package com.tyganeutronics.timezonepicker;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.android.timezonepicker.TimeZonePickerDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TimeZonePickerBottomSheetFragment extends TimeZonePickerDialog {

    public final static String TAG = TimeZonePickerBottomSheetFragment.class.getSimpleName();

    public static TimeZonePickerBottomSheetFragment newInstance(@Nullable OnTimeZoneSetListener listener) {
        return newInstance(0, null, false, listener);
    }

    public static TimeZonePickerBottomSheetFragment newInstance(long startTime, TimeZone timeZone,
                                                                @Nullable OnTimeZoneSetListener listener) {
        return newInstance(startTime, timeZone, false, listener);
    }

    public static TimeZonePickerBottomSheetFragment newInstance(boolean showNearList,
                                                                @Nullable OnTimeZoneSetListener listener) {
        return newInstance(0, null, showNearList, listener);
    }

    public static TimeZonePickerBottomSheetFragment newInstance(long startTime,
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

        TimeZonePickerBottomSheetFragment fragment = new TimeZonePickerBottomSheetFragment();
        fragment.setArguments(bundle);
        fragment.setOnTimeZoneSetListener(listener);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new BottomSheetDialog(requireActivity(), getTheme());
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return dialog;
    }
}
