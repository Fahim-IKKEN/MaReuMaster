package com.openclassrooms.mareu.ui.meetings_list;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.model.Person;
import com.openclassrooms.mareu.utils.DateEasy;
import com.openclassrooms.mareu.utils.PersonsListFormatter;

import java.util.Set;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Titulaire de la vue des éléments de la liste des réunions
 * Représenter un seul élément de la liste
 */
public class MeetingsListViewHolder extends RecyclerView.ViewHolder {

    /**
     * La réunion à afficher comme élément de la vue recycleur
     */
    private Meeting mMeeting;

    /**
     * Composants de l'interface utilisateur
     */

    // sujet de l'étiquette de la réunion
    @BindView(R.id.fragment_meetings_item_subject_text)
    TextView mSubjectText;

    // bouton pour abandonner une réunion
    @BindView(R.id.fragment_meetings_item_delete_btn)
    ImageButton mDeleteButton;

    // date de la réunion étiquette
    @BindView(R.id.fragment_meetings_item_date_text)
    TextView mDateText;

    // étiquette du lieu de la réunion
    @BindView(R.id.fragment_meetings_item_place_text)
    TextView mPlaceText;

    // CardView extensible contenant les personnes invitées à la réunion (bouton développer)
    @BindView(R.id.fragment_meetings_item_expand_btn)
    ImageButton mExpandButton;

    //CardView extensible contenant les personnes invitées à la réunion (bouton réduire)
    @BindView(R.id.fragment_meetings_item_collapse_btn)
    ImageButton mCollapseButton;

    // liste des personnes invitées à la réunion label
    @BindView(R.id.fragment_meetings_item_persons_text)
    TextView mPersonsFlattenListText;

    // Chaîne de réunion vide (pas encore de personnes invitées)
    @BindString(R.string.empty_meeting_persons_list)
    String mEmptyMeetingInvitedPersonsList;

    // CardView extensible contenant les personnes invitées à la réunion (vue carte)
    @BindView(R.id.fragment_meetings_card_view)
    CardView mCardView;

    /**
     * Constructeur
     *
     * @param itemView la vue de l'article
     * @param onClickListener l'écouteur à appeler lorsque le bouton de suppression est cliqué
     */
    public MeetingsListViewHolder(@NonNull View itemView,
                                  MeetingsListAdapter.DropClickListener onClickListener) {
        // appelle toujours le super constructeur
        super(itemView);
        // lier les composants de l'interface utilisateur au code java
        ButterKnife.bind(this, itemView);
        // appeler l'auditeur lorsque le bouton de suppression est cliqué
        mDeleteButton.setOnClickListener(
                v -> onClickListener.onClick(v, getLayoutPosition())
        );
        // appeler l'écouteur lorsque le bouton de développement est cliqué
        mExpandButton.setOnClickListener(v -> expandOrCollapseInvitedPersons());
        // appeler l'écouteur lorsque le bouton Réduire est cliqué
        mCollapseButton.setOnClickListener(v -> expandOrCollapseInvitedPersons());
        // définir l'écouteur de clic sur l'ensemble de l'élément (afficher/masquer les détails de la réunion)
        itemView.setOnClickListener(v -> expandOrCollapseInvitedPersons());
    }

    /**
     * Définir la réunion à afficher en tant qu'élément de la vue recycleur
     * @param meeting la réunion à afficher comme élément de la vue recycleur
     */
    public void setMeeting(Meeting meeting) {
        // définir la réunion à afficher
        mMeeting = meeting;
        //mettre à jour l'interface utilisateur en conséquence (date, sujet, lieu)
        mDateText.setText(DateEasy.localeSpecialStringFromInstant(meeting.getDate()));
        mSubjectText.setText(meeting.getSubject());
        mPlaceText.setText(meeting.getPlace().getName());
        // mettre à jour les personnes invitées à la liste de réunion
        setPersonsList();
    }

    /**
     * Gérer la vue de la carte extensible, qui affiche la liste des personnes
     */
    private void expandOrCollapseInvitedPersons() {
        // si la vue de la carte est réduite, développez-la
        if (mPersonsFlattenListText.getVisibility() == View.GONE) {
            // afficher la liste des personnes
            mPersonsFlattenListText.setVisibility(View.VISIBLE);
            // afficher le bouton Réduire
            mCollapseButton.setVisibility(View.VISIBLE);
            // masquer le bouton de développement
            mExpandButton.setVisibility(View.GONE);
        } else {
            // else collapse it
            mPersonsFlattenListText.setVisibility(View.GONE);
            // masquer le bouton Réduire
            mCollapseButton.setVisibility(View.GONE);
            // afficher le bouton de développement
            mExpandButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Définir la liste des personnes invitées à la réunion dans l'interface utilisateur
     */
    private void setPersonsList() {
        // obtenir des personnes
        Set<Person> persons = mMeeting.getPersons();
        // créer un formateur de personnes
        PersonsListFormatter personsListFormatter = new PersonsListFormatter(persons);
        // si la liste des personnes est vide, affiche un message
        if(persons.isEmpty()) {
            mPersonsFlattenListText.setText(mEmptyMeetingInvitedPersonsList);
        } else {
            // sinon, afficher la liste des personnes
            mPersonsFlattenListText.setText(personsListFormatter.format());
        }
    }

}
