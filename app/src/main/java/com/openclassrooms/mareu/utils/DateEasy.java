package com.openclassrooms.mareu.utils;

import android.util.Log;

import com.openclassrooms.mareu.BuildConfig;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.TimeZone;

/**
 * Une classe Date utils, qui regroupe les fonctions statiques liées à l'analyse et au formatage de Java 8's Instant
 */
public class DateEasy {

    /**
     * Set logger TAG
     */
    private static final String TAG = "DateEasy";

    /**
     * Bind the app TimeZone
     */
    private static final String sDebugZone = "Europe/Paris";
    private static final ZoneId sLocaleZone = getZoneId();
    static ZoneId getZoneId() {
        ZoneId ret = null;
        // If we are in DEBUG mode ..
        if (BuildConfig.DEBUG) {
            // .. return the debug TimeZone
            Log.d(TAG,"Debug Mode. Override TimeZone to " + sDebugZone);
            ret = ZoneId.of(sDebugZone);
        } else {
            // .. get the current TimeZone
            ZoneId currentZone = TimeZone.getDefault().toZoneId();
            Log.d(TAG,"Release Mode. TimeZone is " + currentZone.getId());
            ret = currentZone;
        }
        return ret;
    }

    /**
     * DateTime Formatters
     */
    // full date time formatter, used by pickers
    private static final DateTimeFormatter sDateTimeFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
    // full date formatter, used by pickers
    private static final DateTimeFormatter sDateFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yy");
    // special formatter for list item, used by recycler view
    private static final DateTimeFormatter sSpecialFormatter =
            DateTimeFormatter.ofPattern("dd MMMM HH:mm");

    /**
     * Parse DateTime string (regarding full date time formatter) to Instant
     * @param date the date string in full date time format
     * @return the corresponding Instant, or null, if the input String is in wrong format
     */
    public static Instant parseDateTimeStringToInstant(String date){
        try {
            // parse the "date" parameter, as ZonedDateTime
            ZonedDateTime zdt = ZonedDateTime.parse(
                    date,
                    // take into account the full date time formatter, and the appropriate TimeZone
                    sDateTimeFormatter.withZone(sLocaleZone)
            );
            // return the corresponding Instant
            return zdt.toInstant();
        } catch(DateTimeParseException e){
            // always log the exception
            Log.e(TAG, "DateTimeParseException", e);
            // return null
            return null;
        }
    }

    /**
     * Parse Date string (regarding full date formatter) to Instant
     * @param date the date string in full date format
     * @return the corresponding Instant, or null, if the input String is in wrong format
     */
    public static Instant parseDateStringToInstant(String date){
        try {
            // parse the "date" parameter, as ZonedDateTime
            // take into account the full date formatter, and the appropriate TimeZone
            LocalDate localDate = LocalDate.parse( date, sDateFormatter);
            ZonedDateTime zdt = localDate.atStartOfDay(sLocaleZone);
            // return the corresponding Instant
            return zdt.toInstant();
        } catch(DateTimeParseException e){
            // always log the exception
            Log.e(TAG, "DateTimeParseException", e);
            // return null
            return null;
        }
    }

    /**
     * Try to parse :
     *  - DateTime String as Instant
     *  - Date String as Instant
     *  - if nothing matches, return now()
     * @param date the date string in full date (or date time) format
     * @return the corresponding Instant, or now(), if the input String is in wrong format
     */
    public static Instant parseDateTimeOrDateOrReturnNow(String date){
        Instant ret;
        if (
                // try to parse the "date" parameter, as date and time
                (ret = DateEasy.parseDateTimeStringToInstant(date)) == null
                &&
                // if the first parsing failed, try to parse as date
                (ret = DateEasy.parseDateStringToInstant(date)) == null
        ) {
            // if nothing matches, return now()
            ret = DateEasy.now();
        }
        return ret;
    }
    
    /**
     * Return now as Instant
     */
    public static Instant now(){
        return Instant.now();
    }

    /**
     * Compute Instant from Local (Zoned) Date
     * @param year The year
     * @param month The month
     * @param day The day
     * @return
     */
    public static Instant computeInstantFromLocalDate(int year, int month, int day) {
        return ZonedDateTime
            .now(sLocaleZone)
            .withYear(year)
            // instant API start at month 1, not 0
            .withMonth(month+1)
            .withDayOfMonth(day)
            .toInstant();
    }

    /**
     * Merge current Instant with local Zone Date, and return a new Instant
     * @param date the date
     * @param hour the hour to merge
     * @param minute the minute to merge
     * @return resulting Instant
     */
    public static Instant mergeInstantAndLocalZonedTime(Instant date, int hour, int minute) {
        ZonedDateTime zdt = date.atZone(sLocaleZone);
        return zdt
            .withHour(hour)
            .withMinute(minute)
            .withSecond(0)
            .toInstant();
    }

    /**
     * Get Instant component (year, month, day, hour, minute, ...), with Zone in mind
     * @param date the date
     * @return the corresponding component
     */
    public static int getZonedInstantYear(Instant date) {
        ZonedDateTime zdt = date.atZone(sLocaleZone);
        return zdt.getYear();
    }
    public static int getZonedInstantMonth(Instant date){
        ZonedDateTime zdt = date.atZone(sLocaleZone);
        return zdt.getMonthValue() - 1;
    }
    public static int getZonedInstantDay(Instant date){
        ZonedDateTime zdt = date.atZone(sLocaleZone);
        return zdt.getDayOfMonth();
    }
    public static int getZonedInstantHour(Instant date){
        ZonedDateTime zdt = date.atZone(sLocaleZone);
        return zdt.getHour();
    }
    public static int getZonedInstantMinute(Instant date){
        ZonedDateTime zdt = date.atZone(sLocaleZone);
        return zdt.getMinute();
    }

    /**
     * Add one year to a date
     * @param date the date
     * @return the resulting date
     */
    public static Instant plusOneYear(Instant date){
        ZonedDateTime zdt = date.atZone(sLocaleZone);
        return zdt.plusYears(1).toInstant();
    }

    /**
     * Set the time to a given date to 23:59:59
     * @param date the date
     * @return the resulting date
     */
    public static Instant endOfDay(Instant date){
        ZonedDateTime zdt = date.atZone(sLocaleZone);
        return zdt.withHour(23).withMinute(59).withSecond(59).toInstant();
    }

    /**
     * Set the time to a given date to 00:00:00
     * @param date the date
     * @return the resulting date
     */
    public static Instant startOfDay(Instant date){
        ZonedDateTime zdt = date.atZone(sLocaleZone);
        return zdt.withHour(0).withMinute(0).withSecond(0).toInstant();
    }

    /**
     * Add days
     */
    public static Instant plusDays(Instant date, int days) {
        ZonedDateTime zdt = date.atZone(sLocaleZone);
        return zdt.plusDays(days).toInstant();
    }

    /**
     * Get current date time, and return as special formatting String
     * @return the current locale date time formatted as string
     */
    public static String localeDateTimeStringFromNow(){
        return localeDateTimeStringFromInstant(now());
    }

    /**
     * Get current date, and return as special formatting String
     * @param instant the instant
     * @return the current locale date formatted as string
     */
    public static String localeDateTimeStringFromInstant(Instant instant){
        if(instant == null) return null;
        ZonedDateTime zdt = instant.atZone(sLocaleZone);
        return zdt.format(sDateTimeFormatter);
    }

    /**
     * Convert given date to a special formatting String
     * @param instant the instant
     * @return the locale date time formatted as string
     */
    public static String localeSpecialStringFromInstant(Instant instant){
        ZonedDateTime zdt = instant.atZone(sLocaleZone);
        return zdt.format(sSpecialFormatter);
    }

}
