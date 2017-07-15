package com.techmali.smartteam.ui.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appeaser.sublimepickerlibrary.SublimePicker;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeListenerAdapter;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.techmali.smartteam.R;


import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * The Sublime date picker fragment.
 *
 * @author Wasim
 */
public class SublimeDatePickerFragment extends DialogFragment {

    /**
     * The date formatter.
     */
// Date & Time formatter used for formatting
    // text on the switcher button
    DateFormat mDateFormatter;

    /**
     * The sublime picker.
     */
// Picker
    private SublimePicker mSublimePicker;

    /**
     * The callback.
     */
// Callback to activity
    private Callback mCallback;

    /**
     * The Current date.
     */
    private String currentDate;

    /**
     * The Dialog identifier.
     */
    private int dialogIdentifier;
    /**
     * The Max day.
     */
    private long maxDay = Long.MIN_VALUE;

    private long minDay = Long.MIN_VALUE;
    /**
     * The Is max date require.
     */
    private boolean isMaxDateRequire;
    /**
     * The Is min date require.
     */
    private boolean isMinDateRequire;
    /**
     * The Is today max date.
     */
    private boolean isTodayMaxDate;

    /**
     * New instance sublime date picker fragment.
     *
     * @param dialogIdentifier  the dialog identifier
     * @param currentDate       the current date
     * @param misMaxDateRequire the mis max date require
     * @param misMinDateRequire the mis min date require
     * @param misTodateMaxDate  the mis today max date
     * @param minDayLimit       the min day limit
     * @param maxDayLimit       the max day limit
     * @param datepickerListner the datepicker listner
     * @return the sublime date picker fragment
     */
    public static SublimeDatePickerFragment newInstance(int dialogIdentifier,
                                                        String currentDate,
                                                        boolean misMaxDateRequire,
                                                        boolean misMinDateRequire,
                                                        boolean misTodateMaxDate,
                                                        long minDayLimit,
                                                        long maxDayLimit,
                                                        Callback datepickerListner) {

        SublimeDatePickerFragment dateFragment = new SublimeDatePickerFragment();
        dateFragment.mCallback = datepickerListner;
        dateFragment.currentDate = currentDate;
        dateFragment.dialogIdentifier = dialogIdentifier;

        dateFragment.isMaxDateRequire = misMaxDateRequire;
        dateFragment.isMinDateRequire = misMinDateRequire;
        dateFragment.isTodayMaxDate = misTodateMaxDate;
        dateFragment.maxDay = maxDayLimit;
        dateFragment.minDay = minDayLimit;

        return dateFragment;

    }

    /**
     * Instantiates a new Sublime date picker fragment.
     */
    public SublimeDatePickerFragment() {
        // Initialize formatters
        mDateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        /*mTimeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        mTimeFormatter.setTimeZone(TimeZone.getDefault());*/
    }

    // Set activity callback
   /* public void setCallback(Callback callback)
   {
        mCallback = callback;
    }*/

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

    /**
     * The interface Callback.
     */
// For communicating with the activity
    public interface Callback {
        /**
         * On cancelled.
         */
        void onCancelled();

        /**
         * On date time recurrence set.
         *
         * @param identifier  the identifier
         * @param year        the year
         * @param monthOfYear the month of year
         * @param dayOfMonth  the day of month
         */
        void onDateTimeRecurrenceSet(int identifier, int year, int monthOfYear, int dayOfMonth);
    }

    /**
     * The M listener.
     */
    SublimeListenerAdapter mListener = new SublimeListenerAdapter() {
        @Override
        public void onDateTimeRecurrenceSet(SublimePicker sublimeMaterialPicker,
                                            SelectedDate selectedDate, int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {

            if (mCallback != null) {

                int year = selectedDate.getFirstDate().get(Calendar.YEAR);
                int month = selectedDate.getFirstDate().get(Calendar.MONTH);
                int day = selectedDate.getFirstDate().get(Calendar.DAY_OF_MONTH);

                mCallback.onDateTimeRecurrenceSet(dialogIdentifier, year, month, day);
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

       /* @Override
        public void onDateTimeRecurrenceSet(SublimePicker sublimePicker,
                                            int year, int monthOfYear, int dayOfMonth,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {

        }*/

        // You can also override 'formatDate(Date)' & 'formatTime(Date)'
        // to supply custom formatters.
    };


    private Pair<Boolean, SublimeOptions> getOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;

        displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;

        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);

        options.setDisplayOptions(displayOptions);

        Calendar cal = Calendar.getInstance();

        if (isMaxDateRequire) {
            options.setDateRange(minDay, maxDay);
        }

        if (isMinDateRequire)
            options.setDateRange(cal.getTimeInMillis(), Long.MIN_VALUE);

        if (isTodayMaxDate) {
            cal.set(cal.get(Calendar.YEAR) - 18, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            options.setDateRange(Long.MIN_VALUE, cal.getTimeInMillis());
        }
        Calendar c = Calendar.getInstance();

        int year = 0;
        int month = 0;
        int day = 0;

        if (!TextUtils.isEmpty(currentDate)) {

            String[] splitDate = currentDate.split("-");
            month = Integer.parseInt(splitDate[1]) - 1;
            day = Integer.parseInt(splitDate[0]);
            year = Integer.parseInt(splitDate[2]);
        } else {
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }

        options.setDateParams(year, month, day);

       // If 'displayOptions' is zero, the chosen options are not valid
        return new Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }
}
