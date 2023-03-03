package com.openclassrooms.mareu.ui.pickers.date;

import com.openclassrooms.mareu.core.SimpleMvp;

import java.time.Instant;

/**
 * DatePicker MVP Contract
 * Contract between the view and the presenter
 * Contract between the presenter and the model
 */
public interface DatePickerContract {

    /**
     * Model interface
     */
    interface Model extends SimpleMvp.Model {
        // configure the model to return only dates with a time at 23:59:59 (or not)
        void setEndOfDayMode(boolean endOfDayMode);
        // set the minimum date, before you can't select a date
        void setMinDate(Instant minDate);
        // set the initial date, before the picker will be launched
        void setInitialDate(Instant initialDate);

        // return the selected date by the user, or the initial date if the user didn't select a date
        Instant getDate();
        // return the minimum date, before you can't select a date
        Instant getMinDate();

        // save (year, month and day) returned by the picker, as a date
        void saveDate(int year, int month, int day);

        // allow the model to notify a caller, that the date has changed
        interface OnDateChangedListener {
            void onDateChanged(Instant date);
        }
        void setOnDateChangedListener(OnDateChangedListener onDateChangedListener);
    }

    /**
     * View interface
     */
    interface View extends SimpleMvp.View<Presenter> {
        // show the date picker to the screen
        void showDatePicker();
        // update the ui picker initial date
        void updateInitialDate(Instant date);
        // update the ui picker minimum date
        void updateMinDate(Instant minDate);
    }

    /**
     * Presenter interface
     */
    interface Presenter extends SimpleMvp.Presenter {
        // method called from the view, when the user click to choose a date from the date picker
        void onDatePicked(int year, int month, int day);
    }

}

