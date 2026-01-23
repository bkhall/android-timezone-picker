package com.tyganeutronics.timezonepicker;

import android.os.Bundle;

import com.android.timezonepicker.TimeZonePickerDialog;

public class TimeZonePickerFragment extends TimeZonePickerDialog {

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
