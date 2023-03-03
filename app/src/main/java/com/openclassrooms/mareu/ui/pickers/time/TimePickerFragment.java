package com.openclassrooms.mareu.ui.pickers.time;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.time.Instant;

import static com.google.common.base.Preconditions.checkNotNull;

import com.openclassrooms.mareu.utils.DateEasy;

/**
 * View (fragment) to pick a time
 *
 * A fragment that displays a dialog window, floating on top of its activity's window.
 * Control of the internal dialog must be done by the API, not with direct calls on the dialog.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerContract.View {

    /**
     * Declare Class tag for logging
     */
    private static final String TAG = "TimePickerFragment";

    /**
     * Declare the associated presenter
     */
    private TimePickerContract.Presenter mPresenter;

    /**
     * Buffer the initial date time
     */
    private Instant mDateTime;

    /**
     * Buffer the fragment manager
     */
    private FragmentManager mFragmentManager;

    /**
     * Constructor
     */
    public TimePickerFragment() {
        // always call the super class constructor
        super();
    }

    /**
     * Create a new instance
     */
    public static TimePickerFragment newInstance() {
        return new TimePickerFragment();
    }

    /**
     * Attach the presenter, to avoid circular dependency issue
     * (the view need the presenter to instantiate, and the presenter need the view to instantiate)
     * @param presenter the presenter
     */
    @Override
    public void attachPresenter(TimePickerContract.Presenter presenter) {
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
    @Override
    public void showTimePicker() {
        show(mFragmentManager, TAG);
    }

    /**
     * Update the initial date
     * @param initialDateTime the initial date
     */
    @Override
    public void updateInitialDateTime(Instant initialDateTime) {
        // as the UI is created in one shot, we need to buffer the initial date
        mDateTime = initialDateTime;
    }

    /**
     * We override onCreateDialog to create an entirely custom dialog (with its own content)
     * @param savedInstanceState the saved instance state
     * @return the dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // create the android time picker dialog
        return new TimePickerDialog(
                getContext(),
                // when android notify a time change, we notify the presenter
                (view, hour, minute) -> mPresenter.onTimePicked(hour, minute),
                // we extract hour component from Instant/datetime
                DateEasy.getZonedInstantHour(mDateTime),
                // we extract minute component from Instant/datetime
                DateEasy.getZonedInstantMinute(mDateTime),
                true
        );
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

