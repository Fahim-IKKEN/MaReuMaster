package com.openclassrooms.mareu.service;

import com.openclassrooms.mareu.model.Meeting;

import java.util.List;

/**
 * Service d'API pour gérer les données des réunions
 */
public interface MeetingsApiService {

    /**
     * Get all the Meetings
     * @return {@link List}
     */
    List<Meeting> getMeetings();

    /**
     * Add a Meeting
     * @param meeting Meeting to add
     */
    void addMeeting(Meeting meeting);

    /**
     * Delete a Meeting
     * @param meeting Meeting to delete
     */
    void deleteMeeting(Meeting meeting);

}

