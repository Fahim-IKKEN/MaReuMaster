package com.openclassrooms.mareu.ui.meetings_list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.model.Meeting;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Meetings List Adapter
 */
public class MeetingsListAdapter extends RecyclerView.Adapter<MeetingsListViewHolder> {

    /**
     * Interface pour gérer le drop click sur une réunion
     */
    public interface DropClickListener {
        void onClick(View v, int position);
    }

    /**
     * La liste des réunions à afficher
     */
    private List<Meeting> mMeetings;

    /**
     * L'écouteur à notifier lorsqu'une réunion est abandonnée
     */
    private final DropClickListener mOnDropClickListener;

    /**
     * Constructor
     *
     * @param meetings la liste des réunions à afficher
     * @param onDropClickListener l'écouteur doit être averti lorsqu'une réunion est abandonnée
     */
    public MeetingsListAdapter(List<Meeting> meetings, DropClickListener onDropClickListener) {
        mOnDropClickListener = onDropClickListener;
        setMeetings(meetings);
    }

    /**
     * Créez le support de vue, qui sera utilisé pour afficher une réunion
     *
     * @param parent the parent view
     * @param viewType the view type
     * @return
     */
    @NonNull
    @Override
    public MeetingsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // contexte de la vue paren
        Context context = parent.getContext();
        // gdapter la disposition du porte-vue
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_mettings_list_item, parent, false);
        // retourner le détenteur de la vue
        return new MeetingsListViewHolder(view, mOnDropClickListener);
    }

    /**
     * Mettre à jour le détenteur de la vue avec la réunion à afficher
     *
     * @param holder the vie holder
     * @param position la position afin d'afficher la réunion correspondante
     */
    @Override
    public void onBindViewHolder(@NonNull MeetingsListViewHolder holder, int position) {
        holder.setMeeting(mMeetings.get(position));
    }

    /**
     * Obtenir le nombre de réunions à afficher
     *
     * @return the number of meetings to be displayed
     */
    @Override
    public int getItemCount() {
        return mMeetings.size();
    }

    /**
     * Définir la liste des réunions à afficher
     * @param meetings the list of meetings to be displayed
     */
    @SuppressLint("NotifyDataSetChanged")
    public void updateMeetings(List<Meeting> meetings) {
        // définir la liste des réunions à afficher
        setMeetings(meetings);
        // notifier que l'ensemble de données a changé globalement
        notifyDataSetChanged();
    }

    /**
     * Paramètre de la liste des réunions à afficher
     * @param meetings the list of meetings to be displayed
     */
    private void setMeetings(List<Meeting> meetings) {
        mMeetings = checkNotNull(meetings);
    }

}

