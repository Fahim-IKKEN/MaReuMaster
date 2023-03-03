package com.openclassrooms.mareu.repository.fake;

import com.openclassrooms.mareu.ui.pickers.date.DatePickerContract;
import com.openclassrooms.mareu.utils.DateEasy;

import java.time.Instant;

/**
 * Model/Repository for the MVP date picker
 * Note that, in a real app, this content MUST be dispatched to DAO, models, services, etc.
 */
public class DatePickerFakeRepository implements DatePickerContract.Model {

    /**
     * When a date is saved, always convert to the same date but at the end of the day
     * NEVER STORE THIS BOOLEAN DIRECTLY, IN A REAL APP, USE A DAO
     */
    private Boolean endOfDayMode;

    /**
     * Current date
     * NEVER STORE THIS INSTANT DIRECTLY, IN A REAL APP, USE A DAO
     */
    private Instant mDate;

    /**
     * Min date
     * NEVER STORE THIS INSTANT DIRECTLY, IN A REAL APP, USE A DAO
     */
    private Instant mMinDate;

    /**
     * Initial date
     * NEVER STORE THIS INSTANT DIRECTLY, IN A REAL APP, USE A DAO
     */
    private Instant mInitialDate;

    /**
     * Listener to notify when the date has changed
     */
    private DatePickerContract.Model.OnDateChangedListener mOnDateChangedListener;

    /**
     * When a date is saved, always convert to the same date but at the end of the day
     * @param endOfDayMode true or false
     */
    @Override
    public void setEndOfDayMode(boolean endOfDayMode) {
        this.endOfDayMode = endOfDayMode;
    }

    /**
     * Set the min date
     * @param minDate min date
     */
    @Override
    public void setMinDate(Instant minDate) {
        mMinDate = minDate;
    }

    /**
     * Set the initial date
     * @param initialDate initial date
     */
    @Override
    public void setInitialDate(Instant initialDate) {
        mInitialDate = initialDate;
    }

    /**
     * Get the saved date
     * @return date
     */
    @Override
    public Instant getDate() {
        if(mDate == null)
            return mInitialDate;
        return mDate;
    }

    /**
     * Get the min date
     * @return min date
     */
    @Override
    public Instant getMinDate() {
        return mMinDate;
    }

    /**
     * Save the date from year, month, and day
     * @param year year
     * @param month month
     * @param day day
     */
    @Override
    public void saveDate(int year, int month, int day) {
        if (endOfDayMode) {
            mDate = DateEasy.endOfDay(DateEasy.computeInstantFromLocalDate(year, month, day));
        } else {
            mDate = DateEasy.computeInstantFromLocalDate(year, month, day);
        }
        mOnDateChangedListener.onDateChanged(mDate);
    }

    /**
     * Set the listener to notify when the date has changed
     * @param onDateChangedListener listener
     */
    @Override
    public void setOnDateChangedListener(OnDateChangedListener onDateChangedListener) {
        mOnDateChangedListener = onDateChangedListener;
    }
}

