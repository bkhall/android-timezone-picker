package com.tyganeutronics.timezonepicker;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.android.timezonepicker.TimeZonePickerDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.annotation.NonNull;

public class TimeZonePickerBottomSheetFragment extends TimeZonePickerDialog {

    public final static String TAG = TimeZonePickerBottomSheetFragment.class.getSimpleName();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new BottomSheetDialog(requireActivity(), getTheme());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return dialog;
    }
}
