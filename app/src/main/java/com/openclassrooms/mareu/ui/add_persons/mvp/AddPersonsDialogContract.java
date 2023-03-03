package com.openclassrooms.mareu.ui.add_persons.mvp;

import com.openclassrooms.mareu.core.SimpleMvp;
import com.openclassrooms.mareu.model.Person;

import java.util.Set;

/**
 * Contrat MVP AddPersonsDialog
 * Contrat entre la vue et le présentateur
 * Contrat entre le présentateur et le modèle
 */
public interface AddPersonsDialogContract extends SimpleMvp {

    /**
     * Model interface
     */
    interface Model extends SimpleMvp.Model {
        // return the persons added by the user
        Set<Person> getPersonsSet();

        // add a person to the list of invited persons to the meeting
        void addPerson(Person person);

        // the last saved persons list will be the last one, so we can notify the caller
        void commit();

        // set the initial persons in the set/list
        void setInitialPersons(Set<Person> initialPersons);

        // allow the model to notify a caller, that the persons set has changed for the last time
        interface OnPersonsSetFinalChangedListener {
            void onPersonsListFinalChanged(Set<Person> personList);
        }

        // set the listener to notify the caller, that the persons set has changed for the last time
        void setOnPersonsSetFinalChangedListener(
            OnPersonsSetFinalChangedListener onPersonsSetChangedListener
        );
    }

    /**
     * View interface
     */
    interface View extends SimpleMvp.View<Presenter> {
        // show the add persons dialog to the screen
        void showDialog();
        // update the ui person set
        void updatePersonsList(Set<Person> personsSet);
    }

    /**
     * Presenter interface
     */
    interface Presenter extends SimpleMvp.Presenter {
        // method called from the view, when the user click to add a person to the meeting
        void onPersonAdded(Person person);
        // ask explicitly the presenter to refresh the persons set from the model after view load
        void onViewLoaded();
        // method called from the view, when the user click to validate the persons list
        void commit();
    }

}
