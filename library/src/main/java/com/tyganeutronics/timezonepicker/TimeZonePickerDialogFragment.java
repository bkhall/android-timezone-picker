package com.tyganeutronics.timezonepicker;

import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;

public class TimeZonePickerDialogFragment extends com.android.timezonepicker.TimeZonePickerDialog {

    public final static String TAG = TimeZonePickerDialogFragment.class.getSimpleName();

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
