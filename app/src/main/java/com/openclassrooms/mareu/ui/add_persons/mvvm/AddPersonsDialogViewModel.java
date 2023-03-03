package com.openclassrooms.mareu.ui.add_persons.mvvm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.mareu.model.Person;
import com.openclassrooms.mareu.repository.PersonListRepository;

import java.util.Set;

public class AddPersonsDialogViewModel extends ViewModel {

    private final PersonListRepository mPersonListRepository;

    public AddPersonsDialogViewModel(PersonListRepository mPersonListRepository) {
        this.mPersonListRepository = mPersonListRepository;
    }

    public LiveData<Set<Person>> getPersons() {
        return mPersonListRepository.getPersons();
    }

    public void createPerson(Person person) {
        mPersonListRepository.createPerson(person);
    }

}

