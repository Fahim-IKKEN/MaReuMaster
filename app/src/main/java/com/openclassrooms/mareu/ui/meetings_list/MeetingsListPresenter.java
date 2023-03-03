package com.openclassrooms.mareu.ui.meetings_list;

import androidx.annotation.NonNull;

import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.utils.DateEasy;

import java.time.Instant;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Meetings List Presenter
 */
public class MeetingsListPresenter implements MeetingsListContract.Presenter {

    /**
     * La balise pour la journalisation
     */
    private static final String TAG = "MeetingsListPresenter";

    /**
     * La vue à mettre à jour
     */
    private final MeetingsListContract.View mView;

    /**
     * Le modèle à utiliser
     */
    private final MeetingsListContract.Model mModel;

    /**
     * Buffer la liste des réunions filtrées et triées
     */
    private List<Meeting> mFilteredAndSortedMeetings;

    /**
     * Constructeur
     * @param view la vue à mettre à jour
     * @param model le modèle à utiliser
     */
    public MeetingsListPresenter(@NonNull MeetingsListContract.View view,
                                 @NonNull MeetingsListContract.Model model) {
        mView = checkNotNull(view);
        mModel = checkNotNull(model);
        // important : attacher immédiatement le présentateur dans la vue
        mView.attachPresenter(this);
    }

    /**
     * Une fois la vue initialisée, mettez à jour la vue avec la liste des réunions
     */
    @Override
    public void init() {
        onRefreshMeetingsListRequested();
    }

    /**
     * Lors de la demande d'actualisation de la liste des réunions, mettez à jour la vue avec la liste des réunions
     * Selon les filtres définis, la liste peut être vide
     */
    @Override
    public void onRefreshMeetingsListRequested() {
        // obtenir la liste des réunions, filtrées ou non
        List<Meeting> meetings = mModel.getFilteredAndSortedMeetings();
        // buffer the list, in order to ease the deletion of a meeting
        mFilteredAndSortedMeetings = meetings;
        // mettre à jour la vue avec la nouvelle liste de réunions
        mView.updateMeetings(meetings);
        // mettre à jour la vue avec les filtres à jour
        mView.updateFilters(
            mModel.getFilterPlace(),
            DateEasy.localeDateTimeStringFromInstant(mModel.getFilterStartDate()),
            DateEasy.localeDateTimeStringFromInstant(mModel.getFilterEndDate())
        );
    }

    /**
     * L'enregistrement d'une nouvelle réunion est demandé par la vue
     */
    @Override
    public void onCreateMeetingRequested() {
        // nous déclenchons donc la boîte de dialogue d'inscription à la réunion
        mView.triggerMeetingRegistrationDialog();
    }

    /**
     * L'abandon d'une réunion est demandé par la vue
     * @param position la position de la réunion dans la liste
     */
    @Override
    public void dropMeetingRequested(int position) {
        // abandonner réunion
        mModel.deleteMeeting(mFilteredAndSortedMeetings.get(position));
        // actualiser la liste des réunions
        onRefreshMeetingsListRequested();
    }

    /**
     * Appelé lorsque l'utilisateur a modifié les filtres
     * @param filterPlace le filtre de lieux
     * @param filterStartDate le filtre de date de début
     * @param filterEndDate le filtre de date de fin
     */
    @Override
    public void onFiltersChanged(String filterPlace, String filterStartDate, String filterEndDate){
        // définir l'erreur sur faux
        boolean isError = false;

        // vérifier si la date de début est vide
        if (filterStartDate.isEmpty()) {
            // définir la date de début sur null
            mModel.setFilterStartDate(null);
        } else {
            // analyser la date de début
            Instant tmp = DateEasy.parseDateStringToInstant(filterStartDate);
            if (tmp == null) {
                // si la date de début n'est pas valide, définir l'indicateur d'erreur sur true
                isError = true;
                mView.setErrorFilterStartDate();
            } else {
                // si la date de début est valide, définissez le filtre
                mModel.setFilterStartDate(tmp);
            }
        }

        // vérifier si la date de fin est vide
        if (filterEndDate.isEmpty()) {
            // définir la date de fin sur null
            mModel.setFilterEndDate(null);
        } else {
            // analyser la date de fin
            Instant tmp = DateEasy.parseDateStringToInstant(filterEndDate);
            // si la date de fin n'est pas valide, définir le drapeau d'erreur sur true
            if (tmp == null) {
                // si la date de fin n'est pas valide, définissez le drapeau d'erreur sur true
                isError = true;
                mView.setErrorFilterEndDate();
            } else {
                // si la date de fin est valide, définissez le filtre
                mModel.setFilterEndDate(tmp);
            }
        }
        // s'il n'y a pas d'erreur
        mModel.setFilterPlace(filterPlace);
        // actualiser la liste des réunions
        onRefreshMeetingsListRequested();
        // développer ou réduire les filtres
        if (!isError) {
            mView.expandOrCollapseFilters();
        }
    }

    /**
     * Définir le filtre sur la date de début
     * @param filterStartDate le filtre de date de début
     */
    @Override
    public void setFilterStartDate(String filterStartDate) {
        if (filterStartDate.isEmpty()) {
            mModel.setFilterStartDate(null);
        } else {
            Instant tmp = DateEasy.parseDateStringToInstant(filterStartDate);
            if (tmp != null) {
                mModel.setFilterStartDate(tmp);
            }
        }
        mView.triggerDatePickerDialog(mModel.getFilterStartDate(), true);
    }

    /**
     * Définir le filtre sur la date de début avec le mode manuel (ne pas déclencher le sélecteur de date à la fin
     * @param filterStartDate the start date filter
     */
    @Override
    public void setFilterStartDateManual(String filterStartDate) {
        if (filterStartDate.isEmpty()) {
            mModel.setFilterStartDate(null);
        } else {
            Instant tmp = DateEasy.parseDateStringToInstant(filterStartDate);
            if (tmp != null) {
                mModel.setFilterStartDate(tmp);
            } else {
                mView.setErrorFilterStartDate();
            }
        }
        onRefreshMeetingsListRequested();
    }

    /**
     * Définir le filtre sur la date de fin
     * @param filterEndDate the end date filter
     */
    @Override
    public void setFilterEndDate(String filterEndDate) {
        if (filterEndDate.isEmpty()) {
            mModel.setFilterEndDate(null);
        } else {
            Instant tmp = DateEasy.parseDateStringToInstant(filterEndDate);
            if (tmp != null) {
                mModel.setFilterEndDate(tmp);
            }
        }
        mView.triggerDatePickerDialog(mModel.getFilterEndDate(), false);
    }

    /**
     * Définir le filtre sur la date de fin en mode manuel (ne pas déclencher le sélecteur de date à la fin)
     * @param filterEndDate the end date filter
     */
    @Override
    public void setFilterEndDateManual(String filterEndDate) {
        if (filterEndDate.isEmpty()) {
            mModel.setFilterEndDate(null);
        } else {
            Instant tmp = DateEasy.parseDateStringToInstant(filterEndDate);
            if (tmp != null) {
                mModel.setFilterEndDate(tmp);
            } else {
                mView.setErrorFilterEndDate();
            }
        }
        onRefreshMeetingsListRequested();
    }

    /**
     * Enregistrer la date après l'avoir sélectionnée dans le sélecteur de date
     * @param date le filtre de dates
     * @param beginOrEnd vrai si le filtre est sur la date de début, faux s'il est sur la date de fin
     */
    @Override
    public void saveFilterDate(Instant date, boolean beginOrEnd) {
        if (beginOrEnd) {
            mModel.setFilterStartDate(date);
        } else {
            mModel.setFilterEndDate(date);
        }
        onRefreshMeetingsListRequested();
    }

    /**
     * Enregistrer le filtre sur place
     * @param filterPlace the place filter
     */
    @Override
    public void saveFilterPlace(String filterPlace) {
        mModel.setFilterPlace(filterPlace);
        onRefreshMeetingsListRequested();
    }
}

