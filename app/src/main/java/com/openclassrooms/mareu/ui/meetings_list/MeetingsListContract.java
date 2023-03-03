package com.openclassrooms.mareu.ui.meetings_list;

import com.openclassrooms.mareu.core.SimpleMvp;
import com.openclassrooms.mareu.model.Meeting;

import java.time.Instant;
import java.util.List;

/**
 * Contrat MVP de la liste des réunions
 * Contrat entre la vue et le présenter
 * Contrat entre le présenter et le modèle
 */
public interface MeetingsListContract {

    /**
     * Interface du Model
     */
    interface Model extends SimpleMvp.Model {

        // Obtenir la liste des réunions, concernant le jeu de filtres actuel
        List<Meeting> getFilteredAndSortedMeetings();

        // Obtenir le texte du filtre du lieu de la réunion
        String getFilterPlace();

        // Obtenir la date de début du filtre
        Instant getFilterStartDate();

        // Obtenir la date de fin du filtre
        Instant getFilterEndDate();

        // Supprimer une réunion par sa position
        void deleteMeeting(Meeting meeting);

        // Définir le filtre de date de début
        void setFilterStartDate(Instant filterStartDate);

        // Définir le filtre de date de fin
        void setFilterEndDate(Instant filterEndDate);

        // Définir le filtre de lieu
        void setFilterPlace(String filterPlace);

    }

    /**
     * Interface de View
     */
    interface View extends SimpleMvp.View<Presenter> {

        // Mettre à jour la liste des réunions dans la vue
        void updateMeetings(List<Meeting> meetings);

        // Mettre à jour les étiquettes des filtres dans la vue
        void updateFilters(String filterPlace, String filterStartDate, String filterEndDate);

        // Déclencher la boîte de dialogue d'inscription à la réunion
        void triggerMeetingRegistrationDialog();

        // Développer ou réduire la vue de la carte de filtre
        void expandOrCollapseFilters();

        // Définir l'erreur sur la date de début du filtre
        void setErrorFilterStartDate();

        // Définir l'erreur sur la date de fin du filtre
        void setErrorFilterEndDate();

        // Déclencher la boîte de dialogue de sélection de date
        void triggerDatePickerDialog(Instant date, boolean beginOrEnd);

    }

    /**
     * Interface du Presenter
     */
    interface Presenter extends SimpleMvp.Presenter {

        // Actualiser la liste des réunions demandées
        void onRefreshMeetingsListRequested();

        // Lors de la création d'une nouvelle demande de réunion
        void onCreateMeetingRequested();

        // Lorsque les filtres ont changé
        void onFiltersChanged(String filterPlace, String filterStartDate, String filterEndDate);

        // Déposer une demande de rendez-vous (date et heure) de la réunion
        void dropMeetingRequested(int position);

        // Définir la date de début du filtre
        void setFilterStartDate(String filterStartDate);

        // Définir manuellement la date de début du filtre
        void setFilterStartDateManual(String filterStartDate);

        // Définir la date de fin du filtre
        void setFilterEndDate(String filterEndDate);

        // Définir manuellement la date de fin du filtre
        void setFilterEndDateManual(String filterEndDate);

        // Enregistrer la date du filtre
        void saveFilterDate(Instant date, boolean beginOrEnd);

        // Enregistrer l'emplacement du filtre
        void saveFilterPlace(String filterPlace);
    }

}
