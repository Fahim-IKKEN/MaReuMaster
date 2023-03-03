package com.openclassrooms.mareu.model;

import java.time.Instant;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Objet modèle représentant une réunion, avec la capacité comparable
 */
public class Meeting implements Comparable<Meeting> {

    /**
     * Date of the meeting, using Java 8+ Instant
     */
    private Instant mDate;

    /**
     * Subject of the meeting
     */
    private String mSubject;

    /**
     * Place of the meeting
     */
    private Place mPlace;

    /**
     * List of persons invited to the meeting
     */
    private final Set<Person> mPersons;

    /**
     * Constructor with date, subject, place
     *
     * @param date   Date of the meeting
     * @param subject Subject of the meeting
     * @param place Place of the meeting
     */
    public Meeting(Instant date, String subject, Place place) {
        mPersons = new TreeSet<>();
        setDate(date);
        setSubject(subject);
        setPlace(place);
    }

    /**
     * Constructor with persons invited to the meeting additional argument
     *
     * @param date   Date of the meeting
     * @param subject Subject of the meeting
     * @param place Place of the meeting
     * @param persons Persons invited to the meeting
     */
    public Meeting(Instant date, String subject, Place place, Person... persons) {
        this(date, subject, place);
        addPeople(persons);
    }

    /**
     * Getter for date
     *
     * @return Date of the meeting
     */
    public Instant getDate() {
        return mDate;
    }

    /**
     * Setter for date
     * @param date  Date of the meeting
     */
    public void setDate(Instant date) {
        this.mDate = date;
    }

    /**
     * Getter for subject
     * @return Subject of the meeting
     */
    public String getSubject() {
        return mSubject;
    }

    /**
     * Setter for subject
     * @param subject Subject of the meeting
     */
    public void setSubject(String subject) {
        this.mSubject = subject;
    }

    /**
     * Getter for place
     * @return Place of the meeting
     */
    public Place getPlace() { return mPlace; }

    /**
     * Setter for place
     * @param place Place of the meeting
     */
    public void setPlace(Place place) { this.mPlace = place; }

    /**
     * Getter for persons invited to the meeting
     * @return Persons invited to the meeting
     */
    public Set<Person> getPersons() {
        return mPersons;
    }

    /**
     * Add persons to the meeting
     * @param p Persons to add
     */
    public void addPerson(Person p){ this.mPersons.add(p); }

    /**
     * Add persons to the meeting
     * @param p Persons to add
     */
    public void addPeople(Person... p){
        this.mPersons.addAll(Stream.of(p).collect(Collectors.toList()));
    }

    /**
     * Compare two meetings, by comparing their dates
     * @param o Meeting to compare to
     * @return 0 if equal, -1 if this is before o, 1 if this is after o
     */
    @Override
    public int compareTo(Meeting o) {
        return getDate().compareTo(o.getDate());
    }

    /**
     * Test if two meetings are equal, by comparing their dates and places
     * @param o Meeting to compare to
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        // if the object is compared with itself then return true
        if (this == o) return true;

        // if o is null or not an instance of Meeting then return false
        if (o == null || getClass() != o.getClass()) return false;

        // typecast o to Meeting so that we can compare their members
        Meeting meeting = (Meeting) o;

        // Compare their members and return accordingly
        if (!mDate.equals(meeting.mDate)) return false;
        return mPlace.equals(meeting.mPlace);
    }

    /**
     * Get the hashcode of the meeting, by using the hashcode of its date and place
     * @return Hashcode of the meeting
     */
    @Override
    public int hashCode() {
        int result = mDate.hashCode();
        result = 31 * result + mPlace.hashCode();
        return result;
    }
}
