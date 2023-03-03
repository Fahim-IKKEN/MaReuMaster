package com.openclassrooms.mareu.ui.pickers.date;

import com.openclassrooms.mareu.repository.fake.DatePickerFakeRepository;

import java.time.Instant;

/**
 * Ease the creation of a DatePicker
 */
public class DatePickerFactory {

    /**
     * Create a DatePickerFragment
     * @param initialDate the initial date to display
     * @param minDate the minimum date, before you can't select a date
     * @param endOfDayMode if true, the time will be set to 23:59:59
     * @param onDateChangedListener the listener to call when the date has changed
     * @return the DatePickerFragment
     */
    public DatePickerFragment getFragment(
            Instant initialDate,
            Instant minDate,
            Boolean endOfDayMode,
            DatePickerContract.Model.OnDateChangedListener onDateChangedListener){

        // create the model
        DatePickerContract.Model model = new DatePickerFakeRepository();
        model.setInitialDate(initialDate);
        model.setMinDate(minDate);
        model.setEndOfDayMode(endOfDayMode);
        model.setOnDateChangedListener(onDateChangedListener);

        // create the fragment (view)
        DatePickerFragment fragment = DatePickerFragment.newInstance();

        // create the presenter
        new DatePickerPresenter(fragment, model);

        // return the fragment
        return fragment;
    }

}
