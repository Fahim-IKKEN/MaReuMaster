package com.openclassrooms.mareu.repository.fake;

import com.openclassrooms.mareu.di.DI;
import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.service.MeetingsApiService;
import com.openclassrooms.mareu.ui.meetings_list.MeetingsListContract;
import com.openclassrooms.mareu.utils.DateEasy;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Model/Repository for the MVP MeetingsList
 * Note that, in a real app, this content MUST be dispatched to DAO, models, services, etc.
 */
public class MeetingsListFakeRepository implements MeetingsListContract.Model {

    /**
     * The start date filter
     * AVOID TO STORE THIS INSTANT HERE, IN A REAL APP, USE A DAO
     */
    private Instant mFilterStartDate;

    /**
     * The end date filter
     * AVOID TO STORE THIS INSTANT HERE, IN A REAL APP, USE A DAO
     */
    private Instant mFilterEndDate;

    /**
     * The place filter
     * AVOID TO STORE THIS STRING HERE, IN A REAL APP, USE A DAO
     */
    private String mFilterPlace;

    /**
     * The external service
     */
    private final MeetingsApiService mMeetingsApiService;

    /**
     * Constructor
     */
    public MeetingsListFakeRepository() {
        // init the external model service, thanks to dependency injection
        mMeetingsApiService = DI.getMeetingsApiService();
        // init the start date to now
        mFilterStartDate = DateEasy.startOfDay(DateEasy.now());
        // init the end date to now plus one year
        mFilterEndDate = DateEasy.endOfDay(DateEasy.plusOneYear(DateEasy.now()));
        // init the place to empty string
        mFilterPlace = "";
    }

    /**
     * Get the meetings list
     * @return the meetings list
     */
    @Override
    public List<Meeting> getFilteredAndSortedMeetings() {
        // get the meetings list from the api service
        List<Meeting> meetings = mMeetingsApiService.getMeetings();

        // create a empty list of meetings and push the initial meetings list to the new list
        List<Meeting> filteredMeetings = new ArrayList<>(meetings);
        // filter the meetings list
        filterByPlace(filteredMeetings);
        // sort the meetings list
        filterByTimeSpan(filteredMeetings);
        // sort the meetings list
        Collections.sort(filteredMeetings);

        // return the meetings list
        return filteredMeetings;
    }

    /**
     * Filter the meetings list by place
     * @param meetings the meetings list to be filtered
     */
    private void filterByPlace(List<Meeting> meetings) {
        // create an iterator on the meetings list
        Iterator<Meeting> meetingIterator = meetings.iterator();

        if(mFilterPlace != null && !mFilterPlace.equals("")) {
            // loop on the meetings list
            while (meetingIterator.hasNext()) {
                // if the next meeting place do not match the filter place
                if (!meetingIterator
                        .next()
                        .getPlace()
                        .getName()
                        .toLowerCase()
                        .startsWith(mFilterPlace.toLowerCase())) {
                    // drop it from the list
                    meetingIterator.remove();
                }
            }
        }
    }

    /**
     * Filter the meetings list by time span
     * @param meetings the meetings list to be filtered
     */
    private void filterByTimeSpan(List<Meeting> meetings) {
        // create an iterator on the meetings list
        Iterator<Meeting> meetingIterator = meetings.iterator();

        // loop on the meetings list
        while (meetingIterator.hasNext()) {
            // if the next meeting date is not included in the time span
            Meeting meeting = meetingIterator.next();
            if (
                    // the meeting date before the filter start date
                    (mFilterStartDate != null && meeting.getDate().compareTo(mFilterStartDate) < 0) ||
                    // the meeting date after the filter end date
                    (mFilterEndDate != null && meeting.getDate().compareTo(mFilterEndDate) > 0)
            ) {
                // drop it from the list
                meetingIterator.remove();

            }
        }

    }

    /**
     * Get the filter place
     * @return the filter place
     */
    @Override
    public String getFilterPlace() {
        return mFilterPlace;
    }

    /**
     * Get the filter start date
     * @return the filter start date
     */
    @Override
    public Instant getFilterStartDate() {
        return mFilterStartDate;
    }

    /**
     * Get the filter end date
     * @return the filter end date
     */
    @Override
    public Instant getFilterEndDate() {
        return mFilterEndDate;
    }

    /**
     * Drop a meeting
     * @param meeting the meeting to be dropped
     */
    @Override
    public void deleteMeeting(Meeting meeting) {
        mMeetingsApiService.deleteMeeting(meeting);
    }

    /**
     * Set the start date filter
     * @param filterStartDate the start date filter
     */
    @Override
    public void setFilterStartDate(Instant filterStartDate) {
        mFilterStartDate = DateEasy.startOfDay(filterStartDate);
    }

    /**
     * Set the end date filter
     * @param filterEndDate the end date filter
     */
    @Override
    public void setFilterEndDate(Instant filterEndDate) {
        mFilterEndDate = DateEasy.endOfDay(filterEndDate);
    }

    /**
     * Set the place filter
     * @param filterPlace the place filter
     */
    @Override
    public void setFilterPlace(String filterPlace) {
        mFilterPlace = filterPlace;
    }

}
