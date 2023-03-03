package com.openclassrooms.mareu.ui.meetings_registration;

import com.openclassrooms.mareu.core.SimpleMvp;
import com.openclassrooms.mareu.model.Person;

import java.time.Instant;
import java.util.Set;

/**
 * Contrat en MVP de l'enregistrement de réunion
 * Contrat entre la vue et le présentateur
 * Contrat entre le présentateur et le modèle
 */
public interface MeetingRegistrationDialogContract {

    /**
     * Model interface
     */
    interface Model extends SimpleMvp.Model {
        // Enregistrer la date de la réunion
        void saveMeetingDate(Instant meetingDate);

        // enregistrer les personnes invitées à la réunion
        void saveInvitedPersons(Set<Person> persons);

        // enregistrer la réunion
        void saveMeeting(String place, String subject);

        // récupérer la date de la réunion
        Instant getMeetingDate();

        //récupérer les personnes invitées à la réunion
        Set<Person> getInvitedPersons();
    }

    /**
     * View interface
     */
    interface View extends SimpleMvp.View<Presenter> {

        // Afficher l'écran d'inscription à la réunion à l'écran
        void showDialog();

        // Fermer la boîte de dialogue d'inscription à la réunion
        void returnBackToMeetings();

        // Mettre à jour la date de la réunion
        void updateMeetingDate(Instant meetingDate);

        // Mettre à jour les personnes invitées à la réunion
        void updatePersonsInvitedToTheMeeting(String personsFlattenList);

        // Le sujet doit être défini.
        void setErrorTopicIsEmpty();
        // Le lieu doit être défini.
        void setErrorPlaceIsEmpty();
        //
        //La date doit être fixée.
        void setErrorDateIsEmpty();
        // La date est au mauvais format (voir la classe DateEasy utils class)
        void setErrorDateIsInWrongFormat();

        // déclencher la vue pour lancer la boîte de dialogue de sélection de date
        void triggerDatePickerDialog(Instant meetingDate);
        // déclencher la vue pour lancer la boîte de dialogue de sélection de l'heure
        void triggerTimePickerDialog(Instant meetingDate);
        // déclencher la vue pour lancer la boîte de dialogue Ajouter des personnes
        void triggerAddPersonsDialog(Set<Person> initialPersons);

    }

    /**
     * Presenter interface
     */
    interface Presenter extends SimpleMvp.Presenter {

        // Demande de reprise de la vue après rotation de l'écran
        void onResumeRequest();

        // Méthode appelée lorsque l'utilisateur clique sur le bouton pour sauvegarder la réunion
        void onCreateMeetingRequest(String topic, String meetingDateTextInput, String place);

        // méthode appelée lorsque l'utilisateur clique sur le champ de saisie de date pour lancer la boîte de dialogue de sélection de date
        void onMeetingDatePickRequest(String meetingDateTextInput);

        // méthode appelée lorsque l'utilisateur a sélectionné une date dans la boîte de dialogue de sélection de date
        void onMeetingDateSelected(Instant date);

        // méthode appelée lorsque l'utilisateur a sélectionné l'heure dans la boîte de dialogue de sélection d'heure
        void onMeetingTimeSelected(Instant mergedDateAndTime);

        // méthode appelée lorsque l'utilisateur clique sur le champ de texte des personnes pour lancer la boîte de dialogue d'ajout de personnes.
        void onAddPersonsRequest();

        // Méthode appelée lorsque l'utilisateur a sélectionné des personnes dans la boîte de dialogue Ajouter des personnes
        void onPersonsChanged(Set<Person> persons);
    }
}
