package com.openclassrooms.mareu.utils;

import com.google.common.base.Joiner;
import com.openclassrooms.mareu.model.Person;

import java.util.Set;
import java.util.TreeSet;

/**
 * Une classe simple pour formater une liste de personnes
 */
public class PersonsListFormatter {

    /**
     * The persons list
     */
    private final Set<Person> mPersonsSet;

    /**
     * Constructor
     * @param personsSet the persons list
     */
    public PersonsListFormatter(Set<Person> personsSet) {
        mPersonsSet = personsSet;
    }

    /**
     * Format the persons list
     * @return the formatted persons list as string
     */
    public String format(){
        // build the email list from the Person domain model objects
        Set<String> emailsSet = new TreeSet<>();
        // for each person, add the email to the set
        for(Person person : mPersonsSet){
            // add the email
            emailsSet.add(person.getEmail());
        }
        // build the string from the string set
        return "Persons invited list:\n\n" + Joiner.on("\n").join(emailsSet);
    }

}
