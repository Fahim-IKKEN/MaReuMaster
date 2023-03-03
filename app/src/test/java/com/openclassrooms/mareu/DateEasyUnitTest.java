package com.openclassrooms.mareu;

import static org.junit.Assert.assertEquals;

import com.openclassrooms.mareu.utils.DateEasy;

import org.junit.Test;

import java.time.Instant;

/**
 * Unit test on DateEasy class
 */
public class DateEasyUnitTest {

    /**
     * Test if the parsed date converted to instant from string,
     * is correctly formatted back to a string
     */
    @Test
    public void timezoneManipulation_isCorrect() {
        // create a date string
        String input = "03/12/23 10:15";
        // parse it to Instant (by taking into account the app TimeZone)
        Instant instant = DateEasy.parseDateTimeStringToInstant(input);
        // format it back to string (by taking into account the app TimeZone)
        String output = DateEasy.localeDateTimeStringFromInstant(instant);
        // check if the output is the same as the input
        assertEquals(input, output);
    }

    /**
     * Test the parsing of a date string to an instant
     */
    @Test
    public void parseDateStringToInstant_isCorrect() {
        // create a date string
        String input = "03/12/23";
        // parse it to Instant (by taking into account the app TimeZone)
        Instant instant = DateEasy.parseDateStringToInstant(input);
        // format it back to string (by taking into account the app TimeZone)
        String output = DateEasy.localeDateTimeStringFromInstant(instant);
        // check if the output is the same as the input
        assertEquals(input+ " 00:00", output);
    }

    /**
     * Try the parsing of a date time string to an instant
     */
    @Test
    public void tryToParseDateTimeStringToInstant(){
        // create a date string
        String input = "03/12/23 10:15";
        // parse it to Instant (by taking into account the app TimeZone)
        Instant instant = DateEasy.parseDateTimeOrDateOrReturnNow(input);
        // format it back to string (by taking into account the app TimeZone)
        String output = DateEasy.localeDateTimeStringFromInstant(instant);
        // check if the output is the same as the input
        assertEquals(input, output);
    }

    /**
     * Try the parsing of a date string to an instant
     */
    @Test
    public void tryToParseDateStringToInstant(){
        // create a date string
        String input = "03/12/23";
        // parse it to Instant (by taking into account the app TimeZone)
        Instant instant = DateEasy.parseDateTimeOrDateOrReturnNow(input);
        // format it back to string (by taking into account the app TimeZone)
        String output = DateEasy.localeDateTimeStringFromInstant(instant);
        // check if the output is the same as the input
        assertEquals(input + " 00:00", output);
    }

    /**
     * Test if the function return now if the parsing failed
     */
    @Test
    public void tryToParseWeirdStringToInstant(){
        // create a date string
        String input = "03/12/ not a proper date 23 10:15";
        // parse it to Instant (by taking into account the app TimeZone)
        Instant instant = DateEasy.parseDateTimeOrDateOrReturnNow(input);
        // get now as Instant
        Instant output = DateEasy.now();
        // check if the output is the same as the input
        assertEquals(instant, output);
    }

}

