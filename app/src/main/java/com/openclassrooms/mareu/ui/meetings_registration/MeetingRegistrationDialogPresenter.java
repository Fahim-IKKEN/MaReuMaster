package com.openclassrooms.mareu.ui.meetings_registration;

import androidx.annotation.NonNull;

import com.openclassrooms.mareu.model.Person;
import com.openclassrooms.mareu.ui.meetings_registration.MeetingRegistrationDialogContract;
import com.openclassrooms.mareu.utils.DateEasy;
import com.openclassrooms.mareu.utils.PersonsListFormatter;

import java.time.Instant;
import java.util.Set;

/**
 * Le présenter de MeetingRegistrationDialog
 */
public class MeetingRegistrationDialogPresenter implements MeetingRegistrationDialogContract.Presenter {

    /**
     * La vue
     */
    private final MeetingRegistrationDialogContract.View mView;

    /**
     * Le model
     */
    private final MeetingRegistrationDialogContract.Model mModel;

    /**
    * Constructeur pour le présenter
    * @param view the view
    * @param model the model
    */
    public MeetingRegistrationDialogPresenter(
            @NonNull MeetingRegistrationDialogContract.View view,
            @NonNull MeetingRegistrationDialogContract.Model model) {
        // initialiser la vue
        mView = view;
        // initialiser le model
        mModel = model;
        // important : attachez immédiatement le présentateur à la vue
        mView.attachPresenter(this);
    }

    /**
     * Lorsque la vue est créée (ou recréée)
     */
    @Override
    public void onResumeRequest() {
        // actualiser les participants invités à la réunion
        onPersonsChanged(mModel.getInvitedPersons());
        // actualiser la date de la réunion
        mView.updateMeetingDate(mModel.getMeetingDate());
    }

    /**
     * La vue demande au présenter de créer une réunion
     * @param sujet le sujet de la réunion
     * @param la date de la réunion
     * @param le lieu de la réunion
     */
    @Override
    public void onCreateMeetingRequest(String topic, String dateText, String place) {

        // déclarer une date, afin de l'analyser à partir du texte
        Instant meetingDate = null;

        // vérification d'erreur
        boolean isError = false;

        // vérifier si le lieu est vide
        if (place.isEmpty()) {
            // le lieu est vide
            isError = true;
            // afficher l'erreur
            mView.setErrorPlaceIsEmpty();
        }

        // vérifier si le sujet est vide
        if (topic.isEmpty()) {
            // le sujet est vide
            isError = true;
            // afficher l'erreur
            mView.setErrorTopicIsEmpty();
        }

        // vérifier si la date est vide
        if (dateText.isEmpty()) {
            // la date est vide
            isError = true;
            // afficher l'erreur
            mView.setErrorDateIsEmpty();
        // analyser la date
        } else if ((meetingDate = DateEasy.parseDateTimeStringToInstant(dateText)) == null) {
            // la date a un mauvais format
            isError = true;
            // afficher l'erreur
            mView.setErrorDateIsInWrongFormat();
        }

        // s'il n'y a pas d'erreur
        if (!isError) {
            // sauvegarder cette date
            mModel.saveMeetingDate(meetingDate);
            // enregistrer la réunion
            mModel.saveMeeting(place, topic);
            // retour à la liste des réunions
            mView.returnBackToMeetings();
        }
    }

    /**
     * méthode appelée par la vue, lorsque l'utilisateur clique sur le champ date
     * @param meetingDateTextInput the date text input
     */
    @Override
    public void onMeetingDatePickRequest(String meetingDateTextInput) {
        // enregistrer la date dans le model
        mModel.saveMeetingDate(DateEasy.parseDateTimeOrDateOrReturnNow(meetingDateTextInput));
        // mettre à jour la vue de la date de la réunion
        mView.updateMeetingDate(mModel.getMeetingDate());
        // déclencher (afficher) la boîte de dialogue de sélection de date
        mView.triggerDatePickerDialog(mModel.getMeetingDate());
    }

    /**
     *méthode appelée par la vue, lorsqu'une date est sélectionnée par l'utilisateur
     * @param date the meeting date
     */
    @Override
    public void onMeetingDateSelected(Instant date) {
        // enregistrer la date sdans le model
        mModel.saveMeetingDate(date);
        // mettre à jour la vue de la date de la réunion
        mView.updateMeetingDate(mModel.getMeetingDate());
        // déclencher (afficher) la boîte de dialogue du sélecteur TIME, afin que l'utilisateur puisse choisir l'heure
        mView.triggerTimePickerDialog(mModel.getMeetingDate());
    }

    /**
     * méthode appelée par la vue, lorsqu'une heure est sélectionnée par l'utilisateur
     * @param mergedDateAndTime the merged date and time
     */
    @Override
    public void onMeetingTimeSelected(Instant mergedDateAndTime) {
        // enregistrer la date et l'heure fusionnées résultantes dans le model
        mModel.saveMeetingDate(mergedDateAndTime);
        // mettre à jour la vue de la date de la réunion
        mView.updateMeetingDate(mModel.getMeetingDate());
    }

    /**
     * méthode appeléepar la vue, lorsque l'utilisateur clique pour demander l'ajout de participants invités à la réunion
     */
    @Override
    public void onAddPersonsRequest() {
        mView.triggerAddPersonsDialog(mModel.getInvitedPersons());
    }

    /**
     * La vue restitue les participants invités à la réunion
     * @param persons the persons invited to the meeting
     */
    @Override
    public void onPersonsChanged(Set<Person> persons) {
        // enregistrer les participants
        mModel.saveInvitedPersons(persons);
        // créer un formateur de participants
        PersonsListFormatter personsListFormatter = new PersonsListFormatter(persons);
        // mettre à jour la vue (liste des participants invités à la réunion) en conséquence
        mView.updatePersonsInvitedToTheMeeting(personsListFormatter.format());
    }

    /**
     * Lancer et afficher la vue
     */
    @Override
    public void init() {
        mView.showDialog();
    }
}

