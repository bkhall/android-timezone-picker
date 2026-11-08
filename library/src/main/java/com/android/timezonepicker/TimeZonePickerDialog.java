/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.timezonepicker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class TimeZonePickerDialog extends DialogFragment implements
        TimeZonePickerView.OnTimeZoneSetListener {
    public static final String TAG = TimeZonePickerDialog.class.getSimpleName();

    protected static final String BUNDLE_START_TIME_MILLIS = "bundle_event_start_time";
    protected static final String BUNDLE_TIME_ZONE         = "bundle_event_time_zone";
    protected static final String BUNDLE_SHOW_NEAR_LIST    = "bundle_show_near_list";

    private static final String KEY_HAS_RESULTS        = "has_results";
    private static final String KEY_LAST_FILTER_STRING = "last_filter_string";
    private static final String KEY_LAST_FILTER_TYPE   = "last_filter_type";
    private static final String KEY_LAST_FILTER_TIME   = "last_filter_time";
    private static final String KEY_HIDE_FILTER_SEARCH = "hide_filter_search";

    private OnTimeZoneSetListener mTimeZoneSetListener;
    private TimeZonePickerView    mView;

    public interface OnTimeZoneSetListener {
        void onTimeZoneSet(TimeZoneInfo tzi);
    }

    /**
     * Sets the listener for the selected time zone. A listener set here is not kept when the
     * fragment is recreated (e.g. after a rotation); the picker then reports to its parent
     * fragment or host activity if either implements {@link OnTimeZoneSetListener}.
     */
    public void setOnTimeZoneSetListener(OnTimeZoneSetListener l) {
        mTimeZoneSetListener = l;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (mTimeZoneSetListener == null) {
            Fragment parent = getParentFragment();
            if (parent instanceof OnTimeZoneSetListener) {
                mTimeZoneSetListener = (OnTimeZoneSetListener) parent;
            } else if (context instanceof OnTimeZoneSetListener) {
                mTimeZoneSetListener = (OnTimeZoneSetListener) context;
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        long timeMillis;
        String timeZone;
        boolean showNearList;

        Bundle b = getArguments();
        if (b == null) {
            timeMillis = 0;
            timeZone = null;
            showNearList = true;
        } else {
            timeMillis = b.getLong(BUNDLE_START_TIME_MILLIS);
            timeZone = b.getString(BUNDLE_TIME_ZONE);
            showNearList = b.getBoolean(BUNDLE_SHOW_NEAR_LIST) | TextUtils.isEmpty(timeZone);
        }

        boolean hideFilterSearch = false;

        if (savedInstanceState != null) {
            hideFilterSearch = savedInstanceState.getBoolean(KEY_HIDE_FILTER_SEARCH);
        }

        mView = new TimeZonePickerView(requireContext(), timeZone, timeMillis, showNearList, this,
                hideFilterSearch);

        if (savedInstanceState != null && savedInstanceState.getBoolean(KEY_HAS_RESULTS, false)) {
            mView.showFilterResults(savedInstanceState.getInt(KEY_LAST_FILTER_TYPE),
                    savedInstanceState.getString(KEY_LAST_FILTER_STRING),
                    savedInstanceState.getInt(KEY_LAST_FILTER_TIME));
        }

        return mView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_HAS_RESULTS, mView != null && mView.hasResults());

        if (mView != null) {
            outState.putInt(KEY_LAST_FILTER_TYPE, mView.getLastFilterType());
            outState.putString(KEY_LAST_FILTER_STRING, mView.getLastFilterString());
            outState.putInt(KEY_LAST_FILTER_TIME, mView.getLastFilterTime());
            outState.putBoolean(KEY_HIDE_FILTER_SEARCH, mView.getHideFilterSearchOnStart());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setAdjustResize(dialog);

        return dialog;
    }

    /**
     * Shrinks the dialog above the keyboard so the results list stays visible.
     * <p>
     * SOFT_INPUT_ADJUST_RESIZE is deprecated in favour of handling IME insets in an edge-to-edge
     * window, but these dialog windows are not edge-to-edge (BottomSheetDialog manages that
     * itself), and it still works for them. Without it the bottom sheet pans instead, and the
     * keyboard covers the list.
     */
    @SuppressWarnings("deprecation")
    protected static void setAdjustResize(@NonNull Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    @Override
    public void onTimeZoneSet(TimeZoneInfo tzi) {
        if (mTimeZoneSetListener != null) {
            mTimeZoneSetListener.onTimeZoneSet(tzi);
        }

        dismiss();
    }
}
