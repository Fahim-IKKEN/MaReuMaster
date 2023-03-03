package com.openclassrooms.mareu;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.openclassrooms.mareu.di.DI;
import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.model.Person;
import com.openclassrooms.mareu.model.Place;
import com.openclassrooms.mareu.service.MeetingsApiService;
import com.openclassrooms.mareu.utils.DateEasy;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Test class to test the MeetingsApiService
 */
public class MeetingsApiServiceUnitTest {

    /**
     * MeetingsApiService object as a base for testing
     */
    private MeetingsApiService mMeetingsApiService;

    /**
     * List of four concrete meetings as a base for testing
     */
    private Meeting mMeetingOne;
    private Meeting mMeetingTwo;
    private Meeting mMeetingThree;
    private Meeting mMeetingFour;

    /**
     * Instantiate the service, and the four concrete meetings
     */
    @Before
    public void setup() {
        // instantiate the MeetingsApiService object
        mMeetingsApiService = DI.getNewInstanceMeetingsApiService();

        // create two places
        Place placeOne = new Place("Salle conférence A");
        Place placeTwo = new Place("Salle conférence B");

        // create four persons
        Person personOne = new Person("jean@entreprise.fr");
        Person personTwo = new Person("luc@entreprise.fr");
        Person personThree = new Person("dora@entreprise.fr");
        Person personFour = new Person("lucie@entreprise.fr");

        // Create dates depending on the current time
        // to successfully pass the filters (now and now + 1 year)
        Instant now = DateEasy.now();
        Instant date1 = DateEasy.plusDays(now,10);
        Instant date2 = DateEasy.plusDays(now,20);
        Instant date3 = DateEasy.plusDays(now,30);
        Instant date4 = DateEasy.plusDays(now,40);

        // create the first meeting
        mMeetingOne = new Meeting(
                date1,
                "Réunion de service",
                placeOne,
                personOne,
                personTwo
        );

        // create the second meeting
        mMeetingTwo = new Meeting(
                date2,
                "Atelier technique Android",
                placeOne,
                personTwo,
                personThree,
                personFour
        );

        // create the third meeting
        mMeetingThree = new Meeting(
                date3,
                "Afterwork Repas Noel",
                placeTwo,
                personOne,
                personTwo,
                personThree,
                personFour
        );

        // create the fourth meeting
        mMeetingFour = new Meeting(
                date4,
                "Mise en exploitaion MaReu",
                placeTwo,
                personThree,
                personFour
        );

    }

    /**
     * Test if the meeting service has no meeting at startup
     */
    @Test
    public void emptyMeetingsAtServiceStartup(){
        // get the list of meetings
        List<Meeting> meetings = mMeetingsApiService.getMeetings();
        // check if the list is empty
        assertEquals(0, meetings.size());
    }

    /**
     * Test if the meeting service has four meetings (after adding four meetings)
     */
    @Test
    public void getMeetings(){
        // add four meetings to the service
        mMeetingsApiService.addMeeting(mMeetingOne);
        mMeetingsApiService.addMeeting(mMeetingTwo);
        mMeetingsApiService.addMeeting(mMeetingThree);
        mMeetingsApiService.addMeeting(mMeetingFour);

        // add four meetings to a list
        List<Meeting> expectedMeetings = new ArrayList<>();
        expectedMeetings.add(mMeetingOne);
        expectedMeetings.add(mMeetingTwo);
        expectedMeetings.add(mMeetingThree);
        expectedMeetings.add(mMeetingFour);

        // get the list of meetings from the service
        List<Meeting> actualMeetings = mMeetingsApiService.getMeetings();

        // check if the two lists are equal
        assertEquals(actualMeetings,expectedMeetings);
    }

    /**
     * Test the meetings are correctly added to the service
     */
    @Test
    public void addMeetings(){

        // add four meeting to the service
        mMeetingsApiService.addMeeting(mMeetingOne);
        assertTrue(mMeetingsApiService.getMeetings().contains(mMeetingOne));
        mMeetingsApiService.addMeeting(mMeetingTwo);
        assertTrue(mMeetingsApiService.getMeetings().contains(mMeetingTwo));
        mMeetingsApiService.addMeeting(mMeetingThree);
        assertTrue(mMeetingsApiService.getMeetings().contains(mMeetingThree));
        mMeetingsApiService.addMeeting(mMeetingFour);
        assertTrue(mMeetingsApiService.getMeetings().contains(mMeetingFour));

        // check if the objects from the service are the same as the objects created (date check)
        assertEquals(mMeetingOne.getDate(), mMeetingsApiService.getMeetings().get(0).getDate());
        assertEquals(mMeetingTwo.getDate(), mMeetingsApiService.getMeetings().get(1).getDate());
        assertEquals(mMeetingThree.getDate(), mMeetingsApiService.getMeetings().get(2).getDate());
        assertEquals(mMeetingFour.getDate(), mMeetingsApiService.getMeetings().get(3).getDate());

        // check if the objects from the service are the same as the objects created (place check)
        assertEquals(mMeetingOne.getPlace(), mMeetingsApiService.getMeetings().get(0).getPlace());
        assertEquals(mMeetingTwo.getPlace(), mMeetingsApiService.getMeetings().get(1).getPlace());
        assertEquals(mMeetingThree.getPlace(), mMeetingsApiService.getMeetings().get(2).getPlace());
        assertEquals(mMeetingFour.getPlace(), mMeetingsApiService.getMeetings().get(3).getPlace());

        // check if the objects from the service are the same as the objects created (subject check)
        assertEquals(mMeetingOne.getSubject(), mMeetingsApiService.getMeetings().get(0).getSubject());
        assertEquals(mMeetingTwo.getSubject(), mMeetingsApiService.getMeetings().get(1).getSubject());
        assertEquals(mMeetingThree.getSubject(), mMeetingsApiService.getMeetings().get(2).getSubject());
        assertEquals(mMeetingFour.getSubject(), mMeetingsApiService.getMeetings().get(3).getSubject());

        // check if the objects from the service are the same as the objects created
        // (rely on equals() method)
        assertEquals(mMeetingOne, mMeetingsApiService.getMeetings().get(0));
        assertEquals(mMeetingTwo, mMeetingsApiService.getMeetings().get(1));
        assertEquals(mMeetingThree, mMeetingsApiService.getMeetings().get(2));
        assertEquals(mMeetingFour, mMeetingsApiService.getMeetings().get(3));

        // check if the persons added in each meeting are those expected
        assertTrue(
            mMeetingsApiService.getMeetings().get(0).getPersons().containsAll(mMeetingOne.getPersons())
        );
        assertTrue(
            mMeetingsApiService.getMeetings().get(1).getPersons().containsAll(mMeetingTwo.getPersons())
        );
        assertTrue(
            mMeetingsApiService.getMeetings().get(2).getPersons().containsAll(mMeetingThree.getPersons())
        );
        assertTrue(
            mMeetingsApiService.getMeetings().get(3).getPersons().containsAll(mMeetingFour.getPersons())
        );
    }

    /**
     * Test the meetings are correctly deleted from the service
     */
    @Test
    public void deleteMeetings(){
        // add the first meeting to the service
        mMeetingsApiService.addMeeting(mMeetingOne);
        // check the first service is correctly added
        assertTrue(mMeetingsApiService.getMeetings().contains(mMeetingOne));
        // add the second meeting to the service
        mMeetingsApiService.addMeeting(mMeetingTwo);
        // check the second service is correctly added
        assertTrue(mMeetingsApiService.getMeetings().contains(mMeetingTwo));
        // add the third meeting to the service
        mMeetingsApiService.addMeeting(mMeetingThree);
        // check the third service is correctly added
        assertTrue(mMeetingsApiService.getMeetings().contains(mMeetingThree));
        // add the fourth meeting to the service
        mMeetingsApiService.addMeeting(mMeetingFour);
        // check the fourth service is correctly added
        assertTrue(mMeetingsApiService.getMeetings().contains(mMeetingFour));

        // delete the first meeting
        mMeetingsApiService.deleteMeeting(mMeetingOne);
        // check if the meeting is not in the list
        assertFalse(mMeetingsApiService.getMeetings().contains(mMeetingOne));
        // check if the other meetings are still in the list
        assertTrue(mMeetingsApiService.getMeetings().contains(mMeetingTwo));
        assertTrue(mMeetingsApiService.getMeetings().contains(mMeetingThree));
        assertTrue(mMeetingsApiService.getMeetings().contains(mMeetingFour));

        // delete the second meeting
        mMeetingsApiService.deleteMeeting(mMeetingTwo);
        // check if the two first meeting are not in the list
        assertFalse(mMeetingsApiService.getMeetings().contains(mMeetingOne));
        assertFalse(mMeetingsApiService.getMeetings().contains(mMeetingTwo));
        // check if the other meetings are still in the list
        assertTrue(mMeetingsApiService.getMeetings().contains(mMeetingThree));
        assertTrue(mMeetingsApiService.getMeetings().contains(mMeetingFour));

        // delete the third meeting
        mMeetingsApiService.deleteMeeting(mMeetingThree);
        // check if the three first meeting are not in the list
        assertFalse(mMeetingsApiService.getMeetings().contains(mMeetingOne));
        assertFalse(mMeetingsApiService.getMeetings().contains(mMeetingTwo));
        assertFalse(mMeetingsApiService.getMeetings().contains(mMeetingThree));
        // check if the other meetings are still in the list
        assertTrue(mMeetingsApiService.getMeetings().contains(mMeetingFour));

        // delete the fourth meeting
        mMeetingsApiService.deleteMeeting(mMeetingFour);
        // check if the four first meeting are not in the list
        assertFalse(mMeetingsApiService.getMeetings().contains(mMeetingOne));
        assertFalse(mMeetingsApiService.getMeetings().contains(mMeetingTwo));
        assertFalse(mMeetingsApiService.getMeetings().contains(mMeetingThree));
        assertFalse(mMeetingsApiService.getMeetings().contains(mMeetingFour));

    }

    /**
     * Test the meetings are correctly sorted by date
     */
    @Test
    public void sortMeetingsByDatetime(){

        // add four meeting to the service
        mMeetingsApiService.addMeeting(mMeetingTwo);
        mMeetingsApiService.addMeeting(mMeetingFour);
        mMeetingsApiService.addMeeting(mMeetingOne);
        mMeetingsApiService.addMeeting(mMeetingThree);

        // check if the objects are correctly unordered
        assertEquals(mMeetingOne, mMeetingsApiService.getMeetings().get(2));
        assertEquals(mMeetingTwo, mMeetingsApiService.getMeetings().get(0));
        assertEquals(mMeetingThree, mMeetingsApiService.getMeetings().get(3));
        assertEquals(mMeetingFour, mMeetingsApiService.getMeetings().get(1));

        // sort the meetings by date
        Collections.sort(mMeetingsApiService.getMeetings());

        // check if the objects are correctly ordered
        assertEquals(mMeetingOne, mMeetingsApiService.getMeetings().get(0));
        assertEquals(mMeetingTwo, mMeetingsApiService.getMeetings().get(1));
        assertEquals(mMeetingThree, mMeetingsApiService.getMeetings().get(2));
        assertEquals(mMeetingFour, mMeetingsApiService.getMeetings().get(3));
    }

}

