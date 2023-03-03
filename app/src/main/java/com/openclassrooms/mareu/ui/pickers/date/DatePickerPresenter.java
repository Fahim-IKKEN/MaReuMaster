package com.openclassrooms.mareu.ui.pickers.date;

/**
 * The date picker presenter.
 */
public class DatePickerPresenter implements DatePickerContract.Presenter {

    /**
     * The view.
     */
    private final DatePickerContract.View mView;

    /**
     * The model.
     */
    private final DatePickerContract.Model mModel;

    /**
     * Constructor.
     * @param view the view
     * @param model the model
     */
    public DatePickerPresenter(DatePickerContract.View view, DatePickerContract.Model model) {
        mView = view;
        mModel = model;
        // important : immediately attach the presenter to the view
        mView.attachPresenter(this);
    }

    /**
     * Called when the date is picked by the user from the view
     * @param year the year
     * @param month the month
     * @param day the day
     */
    @Override
    public void onDatePicked(int year, int month, int day) {
        mModel.saveDate(year, month, day);
    }

    /**
     * Init the view, and show it!
     */
    @Override
    public void init() {
        mView.updateInitialDate(mModel.getDate());
        mView.updateMinDate(mModel.getMinDate());
        mView.showDatePicker();
    }
}

