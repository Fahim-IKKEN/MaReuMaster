package com.openclassrooms.mareu.ui.add_persons;

import com.openclassrooms.mareu.model.Person;

import java.util.Set;

public interface AddPersonsDialogFactoryContract {

    interface OnPersonsSetFinalChangedListener {
        void onPersonsListFinalChanged(Set<Person> personList);
    }

    AddPersonsDialogDisplayable getFragment(
            Set<Person> initialPersons,
            OnPersonsSetFinalChangedListener onPersonsSetFinalChangedListener
    );

    AddPersonsDialogDisplayable getFragment(
            Set<Person> initialPersons,
            OnPersonsSetFinalChangedListener onPersonsSetFinalChangedListener,
            String patternToUse // MVP or MVVM
    );

}

