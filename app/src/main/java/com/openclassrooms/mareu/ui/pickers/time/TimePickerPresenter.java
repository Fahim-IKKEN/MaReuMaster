package com.openclassrooms.mareu.ui.pickers.time;

/**
 * The time picker presenter.
 */
public class TimePickerPresenter implements TimePickerContract.Presenter {

    /**
     * The view.
     */
    private final TimePickerContract.View mView;

    /**
     * The model.
     */
    private final TimePickerContract.Model mModel;

    /**
     * Constructor.
     * @param view the view
     * @param model the model
     */
    public TimePickerPresenter(TimePickerContract.View view, TimePickerContract.Model model) {
        mView = view;
        mModel = model;
        // important : immediately attach the presenter to the view
        mView.attachPresenter(this);
    }

    /**
     * Init the view, and show it!
     */
    @Override
    public void init() {
        // update the view with the current time
        mView.updateInitialDateTime(mModel.getDateTime());
        // show the time picker
        mView.showTimePicker();
    }

    /**
     * Called when the time is picked by the user from the view
     * @param hour the hour
     * @param minute the minute
     */
    @Override
    public void onTimePicked(int hour, int minute) {
        mModel.saveTime(hour, minute);
    }
}
