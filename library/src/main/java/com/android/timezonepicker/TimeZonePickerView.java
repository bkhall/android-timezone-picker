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

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tyganeutronics.timezonepicker.R;

import java.time.ZoneId;
import java.util.TimeZone;

import androidx.annotation.Nullable;

@SuppressLint("ViewConstructor")
public class TimeZonePickerView extends LinearLayout implements TextWatcher, OnItemClickListener,
        OnClickListener {
    private static final String TAG = TimeZonePickerView.class.getSimpleName();

    private final AutoCompleteTextView      mAutoCompleteTextView;
    private final TimeZoneFilterTypeAdapter mFilterAdapter;
    private       boolean                   mHideFilterSearchOnStart = false;
    private       boolean                   mFirstTime               = true;

    TimeZoneResultAdapter mResultAdapter;

    private final ImageButton mClearButton;

    public interface OnTimeZoneSetListener {
        void onTimeZoneSet(TimeZoneInfo tzi);
    }

    public TimeZonePickerView(Context context, String timeZone, long timeMillis,
                              boolean showNearList, OnTimeZoneSetListener l,
                              boolean hideFilterSearch) {
        this(context, null, timeZone, timeMillis, showNearList, l, hideFilterSearch);
    }

    public TimeZonePickerView(Context context, @Nullable AttributeSet attrs, String timeZone,
                              long timeMillis, boolean showNearList, OnTimeZoneSetListener l,
                              boolean hideFilterSearch) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.timezonepickerview, this, true);

        mHideFilterSearchOnStart = hideFilterSearch;

        TimeZoneData tzd = new TimeZoneData(context, timeZone, timeMillis);

        mResultAdapter = new TimeZoneResultAdapter(context, tzd, l);

        // TODO - migrate this to a recycler view
        ListView timeZoneList = findViewById(R.id.timezonelist);
        timeZoneList.setAdapter(mResultAdapter);
        timeZoneList.setOnItemClickListener(mResultAdapter);

        mFilterAdapter = new TimeZoneFilterTypeAdapter(context, tzd, mResultAdapter);

        mAutoCompleteTextView = findViewById(R.id.searchBox);
        mAutoCompleteTextView.addTextChangedListener(this);
        mAutoCompleteTextView.setOnItemClickListener(this);
        mAutoCompleteTextView.setOnClickListener(this);

        mClearButton = findViewById(R.id.clear_search);
        mClearButton.setOnClickListener(v -> mAutoCompleteTextView.getEditableText().clear());

        if (showNearList) {
            ZoneId zid = TimeZone.getDefault().toZoneId();
            int index = tzd.findIndexByTimeZoneIdSlow(zid.getId());
            TimeZoneInfo tzi = tzd.get(index);

            mResultAdapter.onSetFilter(1, tzi.mCountry, 0);
        }
    }

    public void showFilterResults(int type, String string, int time) {
        if (mResultAdapter != null) {
            mResultAdapter.onSetFilter(type, string, time);
        }
    }

    public boolean hasResults() {
        return mResultAdapter != null && mResultAdapter.hasResults();
    }

    public int getLastFilterType() {
        return mResultAdapter != null ? mResultAdapter.getLastFilterType() : -1;
    }

    public String getLastFilterString() {
        return mResultAdapter != null ? mResultAdapter.getLastFilterString() : null;
    }

    public int getLastFilterTime() {
        return mResultAdapter != null ? mResultAdapter.getLastFilterType() : -1;
    }

    public boolean getHideFilterSearchOnStart() {
        return mHideFilterSearchOnStart;
    }

    // Implementation of TextWatcher
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    // Implementation of TextWatcher
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mFirstTime && mHideFilterSearchOnStart) {
            mFirstTime = false;

            return;
        }

        filterOnString(s.toString());
    }

    // Implementation of TextWatcher
    @Override
    public void afterTextChanged(Editable s) {
        if (mClearButton != null) {
            mClearButton.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Hide the keyboard since the user explicitly selected an item.
        InputMethodManager manager =
                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(mAutoCompleteTextView.getWindowToken(), 0);
        // An onClickListener for the view item because I haven't figured out a
        // way to update the AutoCompleteTextView without causing an infinite loop.
        mHideFilterSearchOnStart = true;
        mFilterAdapter.onClick(view);
    }

    @Override
    public void onClick(View v) {
        if (mAutoCompleteTextView != null && !mAutoCompleteTextView.isPopupShowing()) {
            filterOnString(mAutoCompleteTextView.getText().toString());
        }
    }

    // This method will set the adapter if no adapter has been set.  The adapter is initialized
    // here to prevent the drop-down from appearing uninvited on orientation change, as the
    // AutoCompleteTextView.setText() will trigger the drop-down if the adapter has been set.
    private void filterOnString(String string) {
        if (mAutoCompleteTextView.getAdapter() == null) {
            mAutoCompleteTextView.setAdapter(mFilterAdapter);
        }

        mHideFilterSearchOnStart = false;
        mFilterAdapter.getFilter().filter(string);
    }
}
