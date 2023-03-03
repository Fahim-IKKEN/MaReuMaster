package com.openclassrooms.mareu.ui.pickers.date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.time.Instant;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

import com.openclassrooms.mareu.utils.DateEasy;

/**
 * View (fragment) to pick a date
 *
 * A fragment that displays a dialog window, floating on top of its activity's window.
 * Control of the internal dialog must be done by the API, not with direct calls on the dialog.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerContract.View {

    /**
     * Declare Class tag for logging
     */
    private static final String TAG = "DatePickerFragment";

    /**
     * Declare the associated presenter
     */
    private DatePickerContract.Presenter mPresenter;

    /**
     * Buffer the initial date
     */
    private Instant mDate;

    /**
     * Buffer the min date
     */
    private Instant mMinDate;

    /**
     * Buffer the fragment manager
     */
    private FragmentManager mFragmentManager;

    /**
     * Constructor
     */
    public DatePickerFragment() {
        // always call the super class constructor
        super();
    }

    /**
     * Create a new instance
     */
    public static DatePickerFragment newInstance() {
        return new DatePickerFragment();
    }

    /**
     * Attach the presenter, to avoid circular dependency issue
     * (the view need the presenter to instantiate, and the presenter need the view to instantiate)
     * @param presenter the presenter
     */
    @Override
    public void attachPresenter(DatePickerContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * Implements the display method, to show the UI
     */
    public void display(FragmentManager fragmentManager) {
        mFragmentManager = checkNotNull(fragmentManager);
        // signal the presenter we want to init and display the dialog
        mPresenter.init();
    }

    /**
     * Show the UI
     */
    public void showDatePicker() {
        show(mFragmentManager, TAG);
    }

    /**
     * Update the initial date
     * @param date the initial date
     */
    public void updateInitialDate(Instant date) {
        // as the UI is created in one shot, we need to buffer the initial date
        mDate = date;
    }

    /**
     * Update the min date
     * @param minDate the min date
     */
    public void updateMinDate(Instant minDate) {
        // as the UI is created in one shot, we need to buffer the min date
        mMinDate = minDate;
    }

    /**
     * We override onCreateDialog to create an entirely custom dialog (with its own content)
     * @param savedInstanceState the saved instance state
     * @return the dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // create the android date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Objects.requireNonNull(getContext()),
                // when android notify a date change, we notify the presenter
                (view, year, month, day) -> mPresenter.onDatePicked(year, month, day),
                // set the initial date
                DateEasy.getZonedInstantYear(mDate),
                DateEasy.getZonedInstantMonth(mDate),
                DateEasy.getZonedInstantDay(mDate)
        );
        // if the min date is defined
        if (mMinDate != null) {
            // set the min date to the android date picker dialog
            datePickerDialog.getDatePicker().setMinDate(mMinDate.toEpochMilli());
        }
        // return back the picker dialog to the system
        return datePickerDialog;
    }

    /**
     * We override onCreate, to define that we want to retain the state of the fragment
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // responsive purpose : if you rotate the device, the retained fragments
        // will remain there (they're not destroyed and recreated)
        setRetainInstance(true);
    }

}
