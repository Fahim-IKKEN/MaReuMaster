package com.openclassrooms.mareu.ui.pickers.time;

import com.openclassrooms.mareu.core.SimpleMvp;

import java.time.Instant;

/**
 * TimePicker MVP Contract
 * Contract between the view and the presenter
 * Contract between the presenter and the model
 */
public interface TimePickerContract {

    /**
     * Model interface
     */
    interface Model extends SimpleMvp.Model {
        // set the initial time, before the picker will be launched
        void setInitialDateTime(Instant initialTime);

        // return the initial date merged with the time choose by the user
        Instant getDateTime();

        // save data (minute, and hour), returned by the picker
        void saveTime(int hour, int minute);

        // allow the model to notify a caller, that the time has changed
        interface OnTimeChangedListener {
            // the full merged initial date with the chosen time is returned
            void onTimeChanged(Instant newDateTime);
        }
        void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener);
    }

    /**
     * View interface
     */
    interface View extends SimpleMvp.View<Presenter> {
        // show the time picker to the screen
        void showTimePicker();
        // update the ui picker initial date
        void updateInitialDateTime(Instant time);
    }

    /**
     * Presenter interface
     */
    interface Presenter extends SimpleMvp.Presenter {
        // method called from the view, when the user click to choose a time from the time picker
        void onTimePicked(int hour, int minute);
    }

}
