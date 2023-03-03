package com.openclassrooms.mareu.ui.pickers.time;

import com.openclassrooms.mareu.repository.fake.TimePickerFakeRepository;

import java.time.Instant;

/**
 * Ease the creation of a TimePicker
 */
public class TimePickerFactory {

    /**
     * Create a TimePickerFragment
     * @param initialDateTime the initial date time (for displaying, and future merging)
     * @param onTimeChangedListener the listener to call when the time has changed
     * @return the TimePickerFragment
     */
    public TimePickerFragment getFragment(
            Instant initialDateTime,
            TimePickerContract.Model.OnTimeChangedListener onTimeChangedListener){

        // create the model
        TimePickerContract.Model model = new TimePickerFakeRepository();
        model.setInitialDateTime(initialDateTime);
        model.setOnTimeChangedListener(onTimeChangedListener);

        // create the fragment (view)
        TimePickerFragment fragment = TimePickerFragment.newInstance();

        // create the presenter
        new TimePickerPresenter(fragment, model);

        // return the fragment
        return fragment;
    }

}
