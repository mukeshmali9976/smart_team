package com.techmali.smartteam.ui.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appeaser.sublimepickerlibrary.SublimePicker;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeListenerAdapter;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.techmali.smartteam.R;
import com.techmali.smartteam.utils.Utils;


import java.util.Calendar;

/**
 * @author Vijay Desai on 22/2/17.
 */
public class SublimeTimePickerFragment extends DialogFragment {

    // Picker
    private SublimePicker mSublimePicker;

    // Callback to activity
    private Callback mCallback;

    private String currentTime;
    private String[] spliteTime = new String[2];
    private boolean isToTime, is24HrFormat;

    private int dialogIdentifier;


    public static SublimeTimePickerFragment newInstance(int dialogIdentifier,
                                                        String currentTime,
                                                        boolean IsToTime,
                                                        boolean Is24HrFormat,
                                                        Callback mCallBackListener) {

        SublimeTimePickerFragment dateFragment = new SublimeTimePickerFragment();
        dateFragment.mCallback = mCallBackListener;
        dateFragment.dialogIdentifier = dialogIdentifier;
        dateFragment.isToTime = IsToTime;
        dateFragment.currentTime = currentTime;
        dateFragment.is24HrFormat = Is24HrFormat;


        return dateFragment;

    }

    public SublimeTimePickerFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSublimePicker = (SublimePicker) getActivity()
                .getLayoutInflater().inflate(R.layout.sublime_picker, container);

        // Retrieve SublimeOptions
        //Bundle arguments = getArguments();

        // Options can be null, in which case, default
        // options are used.

        SublimeOptions options = getOptions().second;

        mSublimePicker.initializePicker(options, mListener);


        return mSublimePicker;
    }

    // For communicating with the activity
    public interface Callback {
        void onCancelled();

        void onDateTimeRecurrenceSet(int identifier, int hourOfDay, int minute, boolean is24HrFormat);
    }

    SublimeListenerAdapter mListener = new SublimeListenerAdapter() {
        @Override
        public void onDateTimeRecurrenceSet(SublimePicker sublimeMaterialPicker, SelectedDate selectedDate, int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {
            if (mCallback != null) {
                mCallback.onDateTimeRecurrenceSet(dialogIdentifier, hourOfDay, minute, is24HrFormat);
            }

            // Should actually be called by activity inside `Callback.onCancelled()`
            dismiss();
        }

        @Override
        public void onCancelled() {
            if (mCallback != null) {
                mCallback.onCancelled();
            }

            // Should actually be called by activity inside `Callback.onCancelled()`
            dismiss();
        }


        // You can also override 'formatDate(Date)' & 'formatTime(Date)'
        // to supply custom formatters.
    };


    private Pair<Boolean, SublimeOptions> getOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;

        displayOptions |= SublimeOptions.ACTIVATE_TIME_PICKER;

        options.setPickerToShow(SublimeOptions.Picker.TIME_PICKER);

        options.setDisplayOptions(displayOptions);

        Calendar cal = Calendar.getInstance();
        int hr, min;

        if (!Utils.isEmptyString(currentTime)) {
            spliteTime = currentTime.split(" ");

            String[] HrMin = spliteTime[0].split(":");


            if (spliteTime[1].equals("AM")) {
                hr = Integer.parseInt(HrMin[0]);
            } else {
                hr = Integer.parseInt(HrMin[0]) + 12;
            }

            min = Integer.parseInt(HrMin[1]);
        } else {
            if (isToTime)
                cal.add(Calendar.MINUTE, 15);

            hr = cal.get(Calendar.HOUR_OF_DAY);
            min = cal.get(Calendar.MINUTE);
            spliteTime[1] = "";
        }

        options.setTimeParams(hr, min, false);


        // If 'displayOptions' is zero, the chosen options are not valid
        return new Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }
}
