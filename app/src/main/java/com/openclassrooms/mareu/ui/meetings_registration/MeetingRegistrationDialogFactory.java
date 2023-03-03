package com.openclassrooms.mareu.ui.meetings_registration;

import com.openclassrooms.mareu.repository.fake.MeetingRegistrationFakeRepository;
import com.openclassrooms.mareu.ui.meetings_registration.MeetingRegistrationDialogFragment;

/**
 * Factory for the MeetingRegistrationDialog
 */
public class MeetingRegistrationDialogFactory {

    /**
     * Create a MeetingRegistrationDialogFragment
     * @return the MeetingRegistrationDialogFragment
     */
    public MeetingRegistrationDialogFragment getFragment(){

        // créer le modèle
        MeetingRegistrationDialogContract.Model model = new MeetingRegistrationFakeRepository();

        // créer le fragment (view)
        MeetingRegistrationDialogFragment fragment = MeetingRegistrationDialogFragment.newInstance();

        // créer le presenter
        new MeetingRegistrationDialogPresenter(fragment, model);

        // retourne le fragment
        return fragment;

    }

}

