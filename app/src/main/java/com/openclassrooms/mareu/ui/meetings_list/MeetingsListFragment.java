package com.openclassrooms.mareu.ui.meetings_list;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.mareu.R;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.ui.meetings_registration.MeetingRegistrationDialogFactory;
import com.openclassrooms.mareu.ui.pickers.date.DatePickerFactory;
import com.openclassrooms.mareu.ui.pickers.date.DatePickerFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.mareu.utils.ui.SimpleTextWatcherFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * DialogFragment pour le fragment de liste de réunions
 */
public class MeetingsListFragment extends Fragment implements MeetingsListContract.View {

    /**
     * Balise pour le journal
     */
    private static final String TAG = "MeetingsListFragment";

    /**
     * Présenter de la liste des réunions
     */
    private MeetingsListContract.Presenter mPresenter;

    /**
     * Adapter de liste de réunions
     */
    private MeetingsListAdapter mMeetingsListAdapter;

    /**
     * Composants de l'interface utilisateur
     */

    // le RecyclerView pour afficher la liste des réunions
    @BindView(R.id.fragment_meetings_recycler_view)
    RecyclerView mRecyclerView;

    // le CardView pour permettre à l'utilisateur de filtrer la liste des réunions
    @BindView(R.id.fragment_meetings_card_view_filter)
    CardView mFilterCardView;

    // le bouton de développement des filtres
    @BindView(R.id.fragment_meetings_card_view_filter_expand_btn)
    ImageButton mFilterExpandButton;

    // le bouton de réduction des filtres
    @BindView(R.id.fragment_meetings_card_view_filter_collapse_btn)
    ImageButton mFilterCollapseButton;

    // le bouton d'application des filtres
    @BindView(R.id.fragment_meetings_card_view_filter_apply_btn)
    MaterialButton mFilterApplyButton;

    // le filtre de saisie de texte de lieu
    @BindView(R.id.fragment_meetings_card_view_filter_place)
    TextInputLayout mFilterPlaceTextInput;

    // le filtre de saisie de texte de la date de début
    @BindView(R.id.fragment_meetings_card_view_filter_start_date)
    TextInputLayout mFilterStartDateTextInput;

    // le filtre de saisie de texte de la date de fin
    @BindView(R.id.fragment_meetings_card_view_filter_end_date)
    TextInputLayout mFilterEndDateTextInput;

    // l'icône du lieu
    @BindDrawable(R.drawable.ic_baseline_place_24dp)
    Drawable mPlaceIconDrawable;

    // l'icône de l'horloge
    @BindDrawable(R.drawable.ic_baseline_access_time_24dp)
    Drawable mDateTimeIconDrawable;

    // l'icône de développement
    @BindDrawable(R.drawable.ic_baseline_expand_more_24dp)
    Drawable mExpandIconDrawable;

    // l'icône d'effondrement
    @BindDrawable(R.drawable.ic_baseline_clear_24dp)
    Drawable mClearIconDrawable;

    /**
     * Constructor
     */
    public MeetingsListFragment() {
        // appel du super constructeur
        super();
    }

    /**
     * Créer une nouvelle instance du fragment
     *
     * @return the fragment
     */
    public static MeetingsListFragment newInstance() {
        return new MeetingsListFragment();
    }

    /**
     * Joindre le présenter, pour éviter les problèmes de dépendance circulaire
     * (la vue a besoin que le présenter instancie, et le présenter a besoin de la vue pour instancier)
     * @param presenter the presenter
     */
    @Override
    public void attachPresenter(@NonNull MeetingsListContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * On surcharge onCreateView, pour définir plus précisément le comportement de l'interface utilisateur au démarrage
     * @param inflater the layout inflater
     * @param container the container
     * @param savedInstanceState the saved instance state
     * @return the view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Adapter la mise en page pour ce fragment
        View view = inflater.inflate(R.layout.fragment_mettings_list, container, false);
        // Lier les composants de l'interface utilisateur
        ButterKnife.bind(this, view);
        // Trouvez le bouton d'action flottant (ajouter une réunion), puis ajoutez la logique setOnClickListener
        Objects.requireNonNull(getActivity())
            .findViewById(R.id.activity_meetings_add_meeting_fab)
            .setOnClickListener(v -> mPresenter.onCreateMeetingRequested());
        // Construire la vue du recycleur
        mMeetingsListAdapter = new MeetingsListAdapter(
                // liste vide des réunions au démarrage
                new ArrayList<>(),
                // l'action sur cliquer pour abandonner une réunion
                (v, position) -> mPresenter.dropMeetingRequested(position)
        );
        // Définir le gestionnaire de mise en page de la vue recycleur
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Définir l'adaptateur de la vue recycleur
        mRecyclerView.setAdapter(mMeetingsListAdapter);
        // Configurer les filtres initiaux
        configureFilters();
        // Renvoyer la vue
        return view;
    }

    /**
     * Gérer les erreurs possibles, lorsque l'utilisateur interagit avec l'interface utilisateur
     */
    @Override
    public void setErrorFilterStartDate() {
        mFilterStartDateTextInput.setError("Please use dd/mm/yy format or leave empty");
    }

    @Override
    public void setErrorFilterEndDate() {
        mFilterEndDateTextInput.setError("Please use the dd/mm/yy format or leave empty");
    }

    /**
     * Configurer les filtres
     */
    private void configureFilters() {

        // Construire un écouteur qui déclenche la méthode onFiltersChanged du présenter
        View.OnClickListener listener = v -> mPresenter.onFiltersChanged(
                // Définir la valeur du filtre de saisie de texte de lieu
                Objects.requireNonNull(mFilterPlaceTextInput.getEditText())
                        .getText()
                        .toString(),
                // Définir la valeur du filtre de saisie de texte de la date de début
                Objects.requireNonNull(mFilterStartDateTextInput.getEditText())
                        .getText()
                        .toString(),
                // Définir la valeur du filtre de saisie de texte de la date de fin
                Objects.requireNonNull(mFilterEndDateTextInput.getEditText())
                        .getText()
                        .toString()
        );


        // Au clic sur le bouton Appliquer, définissez l'écouteur que vous venez de créer
        mFilterApplyButton.setOnClickListener(listener);
        // Au click sur le CardView, définir l'écouteur que l'on vient de créer
        mFilterCardView.setOnClickListener(listener);
        // Au click Sur le bouton de développement, définir l'écouteur que vous l'on vient de créer
        mFilterExpandButton.setOnClickListener(listener);
        // Au click Sur le bouton Réduire, définir l'écouteur que l'on vient de crée
        mFilterCollapseButton.setOnClickListener(listener);

        // Configurer les filtres de saisie de texte
        configureStartDateTextInput();
        configureEndDateTextInput();
        configurePlaceTextInput();
    }

    /**
     * Configurer le filtre de saisie de texte de lieu
     */
    @SuppressLint("ClickableViewAccessibility")
    private void configurePlaceTextInput() {

        // lors du changement de focus sur le filtre de saisie de texte de lieu, informer le présenter en conséquenceon
        Objects.requireNonNull(mFilterPlaceTextInput.getEditText()).setOnFocusChangeListener(
            (v, hasFocus) -> {
                final EditText editText = mFilterPlaceTextInput.getEditText();
                // avertir le présenter que le filtre de saisie de texte du lieu a peut-être changé
                mPresenter.saveFilterPlace(editText.getText().toString());
            }
        );

        // au toucher sur le filtre de saisie de texte de lieu, informer le présenter en conséquenceon
        mFilterPlaceTextInput.getEditText().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int CHOSEN_DRAWABLE = 2; // pick the clear icon
                final EditText editText = mFilterPlaceTextInput.getEditText(); // get the edit text
                Drawable clearIcon = editText.getCompoundDrawablesRelative()[CHOSEN_DRAWABLE];
                // si l'icône d'effacement est touchée, informer le présenter que le lieu a été réinitialisé
                if (
                        event.getAction() == MotionEvent.ACTION_UP &&
                        clearIcon != null &&
                        clearIcon.equals(mClearIconDrawable)
                ) {
                    // la position tactile x, est à côté des limites de l'icône
                    if (event.getRawX() >= (mFilterPlaceTextInput.getEditText().getRight()
                            - clearIcon.getBounds().width())
                    ) {
                        // réinitialiser le texte du lieu
                        editText.setText("");
                        mPresenter.saveFilterPlace("");
                        // consommer l'événement
                        return true;
                    }
                }
                // ne pas consommer l'événement
                return false;
            }
        });

        // lors du changement de texte sur le filtre de saisie du lieu, informer le présenter en conséquence
        SimpleTextWatcherFactory factory = new SimpleTextWatcherFactory();
        mFilterPlaceTextInput.getEditText().addTextChangedListener(
                factory.getDefault(
                    mFilterPlaceTextInput,
                    mPlaceIconDrawable,
                    mClearIconDrawable,
                    mPlaceIconDrawable,
                    null
                )
        );
    }

    @SuppressLint("ClickableViewAccessibility")
    private void configureStartDateTextInput() {

        // lors du changement de focus sur le filtre de saisie de la date de début, informer le présenter en conséquence
        Objects.requireNonNull(mFilterStartDateTextInput.getEditText())
                .setOnFocusChangeListener((v, hasFocus) -> {
                    final EditText editText = mFilterStartDateTextInput.getEditText();
                    if (hasFocus) {
                        // si nous avons le focus, réinitialiser l'erreur
                        mFilterStartDateTextInput.setErrorEnabled(false);
                        mPresenter.setFilterStartDate(editText.toString());
                    } else {
                        // informer le présenter que nous n'avons plus besoin de déclencher la boîte de dialogue de sélection de date
                        mPresenter.setFilterStartDateManual(editText.toString());
                    }
                });

        // Lors de la modification de la date de début, informer le présenter en conséquenceon
        mFilterStartDateTextInput.getEditText().setOnClickListener(
                v -> mPresenter.setFilterStartDate(
                        mFilterStartDateTextInput.getEditText().getText().toString()
                )
        );

        // en cliquant sur le filtre de saisie de la date de début, effacer le filtre de saisie de la date de débuton
        mFilterStartDateTextInput.getEditText().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int CHOSEN_DRAWABLE = 2; // choisir l'icône claire
                final EditText editText = mFilterStartDateTextInput.getEditText(); // obtenir le texte d'édition
                Drawable clearIcon = editText.getCompoundDrawablesRelative()[CHOSEN_DRAWABLE];
                // si l'icône d'effacement est touchée, informer le présenter que le lieu a été réinitialisé
                if (
                    event.getAction() == MotionEvent.ACTION_UP &&
                    clearIcon != null &&
                    clearIcon.equals(mClearIconDrawable)
                ) {
                    // la position tactile x, est à côté des limites de l'icône
                    if (event.getRawX() >= (mFilterStartDateTextInput.getEditText().getRight()
                            - clearIcon.getBounds().width())
                    ) {
                        // réinitialiser le texte du lieu
                        editText.setText("");
                        // consommer l'événement
                        return true;
                    }
                }
                // ne pas consommer l'événement
                return false;
            }
        });

        // en cas de changement de texte sur le filtre de saisie de la date de début, informer le présenter en conséquence
        SimpleTextWatcherFactory factory = new SimpleTextWatcherFactory();
        mFilterStartDateTextInput.getEditText().addTextChangedListener(
                factory.getDefault(
                    mFilterStartDateTextInput,
                    mDateTimeIconDrawable,
                    mClearIconDrawable,
                    mDateTimeIconDrawable,
                    mExpandIconDrawable
                )
        );
    }

    @SuppressLint("ClickableViewAccessibility")
    private void configureEndDateTextInput() {

        // lors du changement de focus sur le filtre de saisie de texte de la date de début, puis informez le présentateur en conséquence
        Objects.requireNonNull(mFilterEndDateTextInput.getEditText())
                .setOnFocusChangeListener((v, hasFocus) -> {
                    final EditText editText = mFilterEndDateTextInput.getEditText();
                    if (hasFocus) {
                        // si nous avons le focus, réinitialisez l'erreur
                        mFilterEndDateTextInput.setErrorEnabled(false);
                        mPresenter.setFilterEndDate(editText.toString());
                    } else {
                        // nous utilisons un appel différent au présenter, pour lui notifier que
                        // nous n'avons plus besoin de déclencher la boîte de dialogue de sélection de date
                        mPresenter.setFilterEndDateManual(editText.toString());
                    }
                });


        // à la modification de la date de début, informer le présenter en conséquenceon
        mFilterEndDateTextInput.getEditText().setOnClickListener(
                v -> mPresenter.setFilterStartDate(
                        mFilterEndDateTextInput.getEditText().getText().toString()
                )
        );

        // au toucher sur le filtre de saisie de la date de fin, effacer le filtre de saisie de la date de fin
        mFilterEndDateTextInput.getEditText().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int CHOSEN_DRAWABLE = 2; // choisissez l'icône claire
                final EditText editText = mFilterEndDateTextInput.getEditText(); // obtenir le texte d'édition
                Drawable clearIcon = editText.getCompoundDrawablesRelative()[CHOSEN_DRAWABLE];
                // si l'icône d'effacement est touchée, informez le présentateur que le lieu a été réinitialisé
                if (
                    event.getAction() == MotionEvent.ACTION_UP &&
                    clearIcon != null &&
                    clearIcon.equals(mClearIconDrawable)
                ) {
                    // la position tactile x, est à côté des limites de l'icône
                    if (event.getRawX() >= (mFilterEndDateTextInput.getEditText().getRight()
                            - clearIcon.getBounds().width())
                    ) {
                        // réinitialiser le texte du lieu
                        editText.setText("");
                        // consommer l'événement
                        return true;
                    }
                }
                // ne pas consommer l'événement
                return false;
            }
        });

        // en cas de changement de texte sur le filtre de saisie de la date de fin, informer le présenter en conséquence
        SimpleTextWatcherFactory factory = new SimpleTextWatcherFactory();
        mFilterEndDateTextInput.getEditText().addTextChangedListener(
                factory.getDefault(
                        mFilterEndDateTextInput,
                        mDateTimeIconDrawable,
                        mClearIconDrawable,
                        mDateTimeIconDrawable,
                        mExpandIconDrawable
                )
        );
    }

    /**
     * À la reprise, demander au présenter d'actualiser les données (liste des réunions)
     */
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.init();
    }

    /**
     * Méthode appelée par le présenter, pour définir les réunions à afficher
     * @param meetings the meetings to display
     */
    @Override
    public void updateMeetings(List<Meeting> meetings) {
        mMeetingsListAdapter.updateMeetings(meetings);
    }

    /**
     * Méthode appelée par le présenter, pour afficher la boîte de dialogue d'inscription (ajout) à la réunion
     */
    @Override
    public void triggerMeetingRegistrationDialog() {
        MeetingRegistrationDialogFactory factory = new MeetingRegistrationDialogFactory();
        factory
                .getFragment()
                .display(getFragmentManager());
    }

    /**
     * Développer ou réduire les filtres
     */
    @Override
    public void expandOrCollapseFilters() {
        if (mFilterExpandButton.getVisibility() == View.VISIBLE) {
            mFilterExpandButton.setVisibility(View.GONE);
            mFilterStartDateTextInput.setVisibility(View.VISIBLE);
            mFilterEndDateTextInput.setVisibility(View.VISIBLE);
            mFilterApplyButton.setVisibility(View.VISIBLE);
            mFilterPlaceTextInput.setVisibility(View.VISIBLE);
            mFilterCollapseButton.setVisibility(View.VISIBLE);
        } else {
            mFilterStartDateTextInput.setVisibility(View.GONE);
            mFilterCollapseButton.setVisibility(View.GONE);
            mFilterApplyButton.setVisibility(View.GONE);
            mFilterEndDateTextInput.setVisibility(View.GONE);
            mFilterPlaceTextInput.setVisibility(View.GONE);
            mFilterExpandButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Mettre à jour l'étiquette de saisie de texte des filtres et réinitialisez les icônes autour de chaque saisie de texte de filtre
     * Mettre également à jour la position du curseur dans la saisie de texte, pour répondre aux attentes de l'utilisateur
     * @param filterPlace the place filter
     * @param filterStartDate the start date filter
     * @param filterEndDate the end date filter
     */
    @Override
    public void updateFilters(String filterPlace, String filterStartDate, String filterEndDate) {
        // mettre à jour le filtre de texte de lieu
        Objects.requireNonNull(mFilterPlaceTextInput.getEditText()).setText(filterPlace);
        // mettre à jour le filtre de texte de la date de début
        Objects.requireNonNull(mFilterStartDateTextInput.getEditText()).setText(
                filterStartDate == null ? "": filterStartDate
        );
        // mettre à jour le filtre de texte de la date de fin
        Objects.requireNonNull(mFilterEndDateTextInput.getEditText()).setText(
                filterEndDate == null ? "" : filterEndDate
        );

        // si le filtre de lieux est vide, ne pas afficher l'icône d'effacement. Sinon, l'afficher
        setCompoundDrawables(mFilterPlaceTextInput, mPlaceIconDrawable,
                filterPlace.isEmpty() ? null : mClearIconDrawable);
        // réinitialiser les icônes de date de début, à l'heure et effacer les icônes
        setCompoundDrawables(mFilterStartDateTextInput, mDateTimeIconDrawable, mClearIconDrawable);
        // réinitialiser les icônes de date de fin, à l'heure et effacer les icônes
        setCompoundDrawables(mFilterEndDateTextInput, mDateTimeIconDrawable, mClearIconDrawable);

        // placer le curseur de sélection à la fin du texte
        mFilterPlaceTextInput.getEditText().setSelection(
            mFilterPlaceTextInput.getEditText().getText().length()
        );
        mFilterStartDateTextInput.getEditText().setSelection(
            mFilterStartDateTextInput.getEditText().getText().length()
        );
        mFilterEndDateTextInput.getEditText().setSelection(
            mFilterEndDateTextInput.getEditText().getText().length()
        );
    }

    /**
     * Modifier les icônes autour de la disposition de saisie de texte
     * @param textInputLayout the text input layout
     * @param drawableStart the start icon
     * @param drawableEnd the end icon
     */
    private void setCompoundDrawables(TextInputLayout textInputLayout,
                                      Drawable drawableStart,
                                      Drawable drawableEnd
    ){

        Objects.requireNonNull(textInputLayout.getEditText())
            .setCompoundDrawablesRelativeWithIntrinsicBounds(
                drawableStart,
                null,
                drawableEnd,
                null
            );

    }

    /**
     * Méthode appelée par le présenter, pour afficher la boîte de dialogue du sélecteur de date
     * @param date the date to display
     * @param isBegin true if the date is the start date, false if the date is the end date
     */
    @Override
    public void triggerDatePickerDialog(Instant date, boolean isBegin) {
        // créer la Factory de sélecteur de date
        DatePickerFactory factory = new DatePickerFactory();
        // obtenir le fragment ..
        DatePickerFragment fragment = factory.getFragment(
                date,
                null,
                !isBegin,
                // à la date fixée, aviser le présenter
                (datePicked) -> mPresenter.saveFilterDate(datePicked, isBegin)
        );
        // .. et l'afficher
        fragment.display(getFragmentManager());
    }
}

