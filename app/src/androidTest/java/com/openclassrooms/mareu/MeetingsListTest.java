package com.openclassrooms.mareu;


import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.di.DI;
import com.openclassrooms.mareu.model.Person;
import com.openclassrooms.mareu.model.Place;
import com.openclassrooms.mareu.service.MeetingsApiService;
import com.openclassrooms.mareu.ui.main.MainActivity;
import com.openclassrooms.mareu.utils.DateEasy;
import com.openclassrooms.mareu.utils.DropViewAction;
import com.openclassrooms.mareu.utils.MatcherFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.openclassrooms.mareu.utils.RecyclerViewCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNull.notNullValue;

import android.os.SystemClock;

import java.time.Instant;

/**
 * Instrumented test class for the meetings list
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class MeetingsListTest {

    /**
     * The meetings
     */
    private Meeting meetingOne;
    private Meeting meetingTwo;
    private Meeting meetingThree;
    private Meeting meetingFour;

    /**
     * The simplest scenario rule possible
     */
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityTestRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Declare the API service
     */
    private MeetingsApiService service;


    /**
     * Matcher factory
     */
    private MatcherFactory matcherFactory = new MatcherFactory();

    /**
     * Initialize the meetings
     */
    @Before
    public void setUp() {

        // Assert that we can correctly get the main activity
        mActivityTestRule.getScenario().onActivity(activity -> {
            assertThat(activity, notNullValue());
        });

        // Get the API service
        service = DI.getMeetingsApiService();

        // assert service is not null
        assertThat(service, notNullValue());

        // Build the places
        Place placeOne = new Place("Salle conference A");
        Place placeTwo = new Place("Salle conference B");

        // Build the persons
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

        // Build the meetings
        meetingOne = new Meeting(
                date1,
                "Réunion de service",
                placeOne,
                personOne,
                personTwo
        );

        meetingTwo = new Meeting(
                date2,
                "Atelier technique Android",
                placeOne,
                personTwo,
                personThree,
                personFour
        );

        meetingThree = new Meeting(
                date3,
                "Afterwork Repas Noel",
                placeTwo,
                personOne,
                personTwo,
                personThree,
                personFour
        );

        meetingFour = new Meeting(
                date4,
                "Mise en exploitaion MaReu",
                placeTwo,
                personThree,
                personFour
        );

        // add the meetings to the API service
        service.getMeetings().clear();
        service.addMeeting(meetingOne);
        service.addMeeting(meetingTwo);
        service.addMeeting(meetingThree);
        service.addMeeting(meetingFour);

        // expand and collapse the filters, to see them on screen before the test start
        onView(allOf(withId(R.id.fragment_meetings_card_view_filter), isDisplayed()))
                .perform(click());
        onView(allOf(withId(R.id.fragment_meetings_card_view_filter), isDisplayed()))
                .perform(click());
    }

    /**
     * Test the meetings list is not empty
     */
    @Test
    public void meetingsListTest_shouldNotBeEmpty() {
        onView(allOf(withId(R.id.fragment_meetings_recycler_view), isDisplayed()))
            .check(matches(hasMinimumChildCount(1)));
    }

    @Test
    public void meetingsListTest_shouldContainFourMeetings() {
        onView(allOf(withId(R.id.fragment_meetings_recycler_view), isDisplayed()))
                .check(withItemCount(4));
    }

    /**
     * Test a meeting has been successfully added
     */

    @Test
    public void meetingsListTest_createAction_shouldAddItem() {
        final int EXPECTED_ITEM_COUNT = 5;

        // click to add a meeting, and open the meeting registration form/dialog
        onView(allOf(withId(R.id.activity_meetings_add_meeting_fab), isDisplayed()))
                .perform(click());

        // fill the meeting registration form/dialog (set the subject)
        onView(allOf(matcherFactory.childAtPosition(
                matcherFactory.childAtPosition(
                        withId(R.id.fragment_meeting_registration_subject_text_input), 0
                ),
                0
        ),
                isDisplayed()
        ))
        .perform(replaceText("The subject"), closeSoftKeyboard());

        // fill the meeting registration form/dialog (set the place)
        onView(allOf(
                matcherFactory.childAtPosition(
                        matcherFactory.childAtPosition(
                                withId(R.id.fragment_meeting_registration_place_text_input), 0
                        ),
                        0
                ),
                isDisplayed()
        ))
        .perform(replaceText("A place"), closeSoftKeyboard());

        // perform save action on the toolbar
        onView(allOf(withId(R.id.meeting_creation_dialog_toolbar_save_item), isDisplayed()))
                .perform(click());

        // check the meeting has been added
        onView(allOf(withId(R.id.fragment_meetings_recycler_view), isDisplayed()))
                .check(withItemCount(EXPECTED_ITEM_COUNT));

    }

    /**
     * Test the meetings drop meeting action on the list (recycler view)
     */
    @Test
    public void meetingsListTest_deleteAction_shouldRemoveItem() {
        final int EXPECTED_MEETINGS_COUNT = 3;
        final int DROP_MEETING_AT_POSITION = 2;

        onView(allOf(withId(R.id.fragment_meetings_recycler_view), isDisplayed()))
            .perform(actionOnItemAtPosition(DROP_MEETING_AT_POSITION, new DropViewAction()))
            .check(withItemCount(EXPECTED_MEETINGS_COUNT));
    }

    @Test
    public void meetingsListTest_filterAction_shouldHideItem() {
        final int EXPECTED_MEETINGS_COUNT = 4;

        // expand the filters card view
        onView(allOf(withId(R.id.fragment_meetings_card_view_filter), isDisplayed()))
                .perform(click());

        // check if the filter by place is displayed
        onView(allOf(
                matcherFactory.childAtPosition(
                        matcherFactory.childAtPosition(
                                withId(R.id.fragment_meetings_card_view_filter_place), 0
                        ),
                        0),
                isDisplayed()
        ))
        // set the place filter to Salle
        .perform(replaceText("Salle"), closeSoftKeyboard());

        // validate the filters
        onView(allOf(withId(R.id.fragment_meetings_card_view_filter_apply_btn), isDisplayed()))
        .perform(click());

        // check the count output
        onView(allOf(withId(R.id.fragment_meetings_recycler_view), isDisplayed()))
        .check(withItemCount(EXPECTED_MEETINGS_COUNT));

    }
}
