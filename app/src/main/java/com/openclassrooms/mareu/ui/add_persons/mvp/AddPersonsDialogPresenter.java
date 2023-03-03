package com.openclassrooms.mareu.ui.add_persons.mvp;

import com.openclassrooms.mareu.model.Person;

/**
 * Le présentateur pour AddPersonsDialog
 */
public class AddPersonsDialogPresenter implements AddPersonsDialogContract.Presenter {

    /**
    * The view
    */
    private final AddPersonsDialogContract.View mView;

    /**
    * The model
    */
    private final AddPersonsDialogContract.Model mModel;

    /**
    * Constructor for the presenter
    * @param view the view
    * @param model the model
    */
    public AddPersonsDialogPresenter(AddPersonsDialogContract.View view, AddPersonsDialogContract.Model model) {
        mView = view;
        mModel = model;
        // important : immediately attach the presenter to the view
        mView.attachPresenter(this);
    }

    /**
    * Add a person to the list (set) of persons
    * @param person the person to add
    */
    @Override
    public void onPersonAdded(Person person) {
        mModel.addPerson(person);
        mView.updatePersonsList(mModel.getPersonsSet());
    }

    /**
     * Ask explicitly the presenter to refresh the persons set from the model
     */
    @Override
    public void onViewLoaded() {
        mView.updatePersonsList(mModel.getPersonsSet());
    }

    /**
    * Tell the model that's the final list of persons that is currently set
    * So we can notify the listener
    */
    @Override
    public void commit() {
        mModel.commit();
    }

    /**
     * Init the view, and show it!
     */
    @Override
    public void init() {
        mView.showDialog();
    }
}

