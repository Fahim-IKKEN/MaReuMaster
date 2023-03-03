package com.openclassrooms.mareu.ui.meetings_registration;

import static com.google.common.base.Preconditions.checkNotNull;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.model.Person;
import com.openclassrooms.mareu.ui.add_persons.AddPersonsDialogDisplayable;
import com.openclassrooms.mareu.ui.add_persons.AddPersonsDialogFactory;
import com.openclassrooms.mareu.ui.main.MainActivity;
import com.openclassrooms.mareu.ui.pickers.date.DatePickerFactory;
import com.openclassrooms.mareu.ui.pickers.date.DatePickerFragment;
import com.openclassrooms.mareu.ui.pickers.time.TimePickerFactory;
import com.openclassrooms.mareu.ui.pickers.time.TimePickerFragment;
import com.openclassrooms.mareu.utils.DateEasy;
import com.openclassrooms.mareu.utils.ui.SimpleTextWatcher;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment de dialogue pour l'enregistrement de réunion
 */
public class MeetingRegistrationDialogFragment extends DialogFragment implements MeetingRegistrationDialogContract.View {

    /**
     * Étiquette pour la déconnexion
     */
    private static final String TAG = "MeetingRegistrationDialogFragment";

    /**
     * Longueur minimale d'entrée de texte pour les entrées de texte
     */
    private static final int TEXT_INPUT_MIN_LENGTH = 0;

    /**
     * Le presenter
     */
    private MeetingRegistrationDialogContract.Presenter mPresenter;

    /**
     * Gérer le gestionnaire de fragments
     */
    private FragmentManager mFragmentManager;

    /**
     * Composants d'interface utilisateur
     */

    // Enregistrer la barre d'outils
    @BindView(R.id.fragment_meeting_registration_dialog_toolbar)
    Toolbar mRegistrationToolbar;

    // Entrée de texte du sujet de la réunion
    @BindView(R.id.fragment_meeting_registration_subject_text_input)
    TextInputLayout mSubjectTextInput;

    // Entrée de texte du lieux de la réunion
    @BindView(R.id.fragment_meeting_registration_place_text_input)
    TextInputLayout mPlaceTextInput;

    // Texte d'entrée de de date
    @BindView(R.id.fragment_meeting_registration_date_text_input)
    TextInputLayout mDateTextInput;

    // Conteneur de vue de carte de personnes
    @BindView(R.id.fragment_meetings_registration_persons_card_view)
    CardView mPersonsCardView;

    // Vue texte de la liste complète des participants
    @BindView(R.id.fragment_meeting_registration_persons_full_list_text)
    TextView mPersonsFullListText;

    /**
     * Constructor
     */
    public MeetingRegistrationDialogFragment() {
        // Toujours appeler le constructeur par défaut
        super();
    }

    /**
     * Créer une nouvelle instance du fragment de dialogue
     */
    public static MeetingRegistrationDialogFragment newInstance() {
        return new MeetingRegistrationDialogFragment();
    }

    /**
     * Attachez le présenter, pour éviter les problèmes de dépendance circulaire
     * (la vue a besoin du présenter pour instantier, et le présenter a besoin de la vue pour s'instancier)
     * @param presenter the presenter
     */
    @Override
    public void attachPresenter(@NonNull MeetingRegistrationDialogContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * Implémentation de la méthode d'affichage de l'interface utilisateur.
     */
    public void display(FragmentManager fragmentManager) {
        mFragmentManager = checkNotNull(fragmentManager);
        // Signaler au présenter que nous voulons initialiser et afficher la boîte de dialogues
        mPresenter.init();
    }

    /**
     * affichage de l'UI
     */
    @Override
    public void showDialog() {
        show(mFragmentManager, TAG);
    }

    /**
     * Nous remplaçons onCreate pour définir que nous voulons conserver l'état du fragment lorsque l'activité est recréée.
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // appelez toujours la méthode de la super classe
        super.onCreate(savedInstanceState);

        // Si on fait pivoter l'appareil, les fragments retenus y resteront (ils ne sont pas détruits et recréés)
        setRetainInstance(true);
        // adopter une boîte de dialogue à thème clair, mais avec un style normal (barre de titre)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_LightDialog);
    }

    /**
     * Nous remplaçons onCreateView, pour définir plus précisément le comportement de l'interface utilisateur au démarrage.
     * @param inflater the layout inflater
     * @param container the container
     * @param savedInstanceState the saved instance state
     * @return the view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // appelez toujours la méthode de la super classe
        super.onCreateView(inflater, container, savedInstanceState);
        // Adapter le layout
        View view = inflater.inflate(R.layout.fragment_meeting_registration_dialog, container,
                false);
        // lier les composants de l'interface utilisateur
        ButterKnife.bind(this, view);
        // configurer la saisie du texte du sujet de la réunion
        configureSubjectTextInput();
        // configurer la saisie de texte de lieu de la réunion
        configurePlaceTextInput();
        // configurer la saisie de texte de da date da la réunion
        configureDateTextInput();
        // configurer la vue de la carte "ajouter des personnes à la réunion"
        configureAddPersonsCardView();
        // retourner la vue
        return view;
    }

    /**
     * On doit réadapter le layout dans onCreateView, mais ne pas initialiser d'autres vues (comme le menu)
     * dans onCreateView. Par conséquent, pour éviter les plantages, nous devons appeler cette méthode d'interface utilisateur
     * dans onViewCreated ou onActivityCreated.
     * @param view the view
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // appelez toujours la méthode de la super classe
        super.onViewCreated(view, savedInstanceState);
        // charger (réadapter) le menu dans la barre d'outils
        mRegistrationToolbar.inflateMenu(R.menu.meeting_registration_menu);
        // configurer le bouton de retour sur la barre d'outils pour fermer la boîte de dialogue
        mRegistrationToolbar.setNavigationOnClickListener(v -> dismiss());
        // configurer le bouton enregistrer sur la barre d'outils
        mRegistrationToolbar.setOnMenuItemClickListener(item -> {
            // demander au présenter de créer la réunionte
            mPresenter.onCreateMeetingRequest(
                    // sujet de la réunion
                    Objects.requireNonNull(mSubjectTextInput.getEditText()).getText().toString(),
                    // date de la réunion
                    Objects.requireNonNull(mDateTextInput.getEditText()).getText().toString(),
                    // lieu de la réunion
                    Objects.requireNonNull(mPlaceTextInput.getEditText()).getText().toString()
            );
            // retourner true pour consommer l'événement
            return true;
        });
    }

    /**
     * Configurer la saisie du texte du sujet de la réunion
     */
    private void configureSubjectTextInput() {
        // définir l'action après la modification du texte sur cette entrée
        Objects.requireNonNull(mSubjectTextInput.getEditText())
                .addTextChangedListener(new SimpleTextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        // vérifier si la longueur est valide, sinon, afficher un message d'erreur
                        if (mSubjectTextInput.getEditText().getText().length() > TEXT_INPUT_MIN_LENGTH) {
                            // déclencher un message d'erreur
                            mSubjectTextInput.setErrorEnabled(false);
                        }
                    }
                });
    }

    /**
     * Configurer la saisie de texte du lieu de la réunion
     */
    private void configurePlaceTextInput() {
        // définir l'action après la modification du texte sur cette entrée
        Objects.requireNonNull(mPlaceTextInput.getEditText())
                .addTextChangedListener(new SimpleTextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        // vérifier si la longueur est valide, sinon, afficher un message d'erreur
                        if (mPlaceTextInput.getEditText().getText().length() > TEXT_INPUT_MIN_LENGTH) {
                            // déclencher un message d'erreur
                            mPlaceTextInput.setErrorEnabled(false);
                        }
                    }
                });
    }

    /**
     * Configurer la saisie de texte de la date de la réunion
     */
    private void configureDateTextInput() {
        // si l'utilisateur clique sur la saisie de texte de date ..
        Objects.requireNonNull(mDateTextInput.getEditText()).setOnClickListener(
                // .. on notifie le présenter, afin d'afficher une boîte de dialogue de sélection de date..
                v -> mPresenter.onMeetingDatePickRequest(
                        mDateTextInput.getEditText().getText().toString()
                )
        );

        // si l'utilisateur crée le focus sur la saisie de texte de date ..
        mDateTextInput.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // désactiver le message d'erreur
                mDateTextInput.setErrorEnabled(false);
                // .. nous notifions le présentter, afin d'afficher une boîte de dialogue de sélection de date..
                mPresenter.onMeetingDatePickRequest(
                        mDateTextInput.getEditText().getText().toString()
                );
            }
        });

        // si la date sous forme de texte a changé
        mDateTextInput.getEditText().addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // vérifier la longueur du texte
                if (mDateTextInput.getEditText().getText().length() > TEXT_INPUT_MIN_LENGTH) {
                    // si non vide, désactiver l'erreur
                    mDateTextInput.setErrorEnabled(false);
                }
            }
        });

        // au démarrage, on fixe la date à la date du jour
        mDateTextInput.getEditText().setText(DateEasy.localeDateTimeStringFromNow());
    }

    /**
     * Configurer la vue de la carte "ajouter des participants à la réunion"
     */
    private void configureAddPersonsCardView() {
        // définir l'action lorsque la vue de la carte est cliquée
        mPersonsCardView.setOnClickListener(v -> mPresenter.onAddPersonsRequest());
    }

    /**
     * Conserver l'état de la boîte de dialogue lors de la rotation de l'appareil
     */
    @Override
    public void onResume() {
        super.onResume();
        // lors de la rotation de l'écran, on conserve l'état de la boîte de dialogue
        mPresenter.onResumeRequest();
    }

    /**
     * Lorsque nous quittons la boîte de dialogue d'inscription à la réunion, nous devons mettre à jour la liste des réunions
     * dans l'activité principale
     * @param dialogInterface the dialog interface
    /**/
    @Override
    public void onDismiss(@NonNull DialogInterface dialogInterface) {
        // Nous devons mettre à jour la liste des réunions sur l'activité principale
        ((MainActivity) Objects.requireNonNull(getContext())).updateMeetingsFragments();
    }


    /**
     * Retour à l'activité principale
     */
    @Override
    public void returnBackToMeetings() {
        dismiss();
    }

    /**
     *Mettre à jour la date de la réunion
     * @param meetingDate the meeting date
     */
    @Override
    public void updateMeetingDate(Instant meetingDate) {
        Objects.requireNonNull(mDateTextInput
                        .getEditText())
                .setText(DateEasy.localeDateTimeStringFromInstant(meetingDate));
    }

    /**
     * Mettre à jour la liste complète des personnes de l'interface utilisateur
     * @param personsFlattenList la liste compressée des participants invités à la réunion
     */
    @Override
    public void updatePersonsInvitedToTheMeeting(String personsFlattenList) {
        mPersonsFullListText.setText(personsFlattenList);
        mPersonsFullListText.setVisibility(View.VISIBLE);
    }

    /**
     * La date est vide (erreur)
     */
    @Override
    public void setErrorDateIsEmpty() {
        mDateTextInput.setError("Must be set");
    }

    /**
     * La date est dans le mauvais format (erreur)
     */
    @Override
    public void setErrorDateIsInWrongFormat() {
        mDateTextInput.setError("Date in wrong format");
    }

    /**
     * Le champs sujet est vide (erreur)
     */
    @Override
    public void setErrorTopicIsEmpty() {
        mSubjectTextInput.setError("Must be set");
    }

    /**
     * Le champs lieu est vide (erreur)
     */
    @Override
    public void setErrorPlaceIsEmpty() {
        mPlaceTextInput.setError("Must be set");
    }

    /**
     * Déclencher la boîte de dialogue de sélection de date
     * @param meetingDate la date initiale à définir dans le sélecteur de date
     */
    @Override
    public void triggerDatePickerDialog(Instant meetingDate) {
        // créer la factory de sélecteur de date
        DatePickerFactory factory = new DatePickerFactory();
        // obtenir le fragment de sélecteur de date
        DatePickerFragment fragment = factory.getFragment(
                // date initiale
                meetingDate,
                // dater de
                meetingDate,
                //la date résultante ne doit pas être fixée à la fin de la journée
                false,
                // lorsqu'une date est sélectionnée par l'utilisateur, la propager au présenter
                (date) -> mPresenter.onMeetingDateSelected(date)
        );
        // afficher le fragment de sélecteur de date à l'écran
        fragment.display(getFragmentManager());
    }

    /**
     * Déclencher la boîte de dialogue du sélecteur de temps
     * @param meetingDate the initial date/time to set in the time picker
     */
    @Override
    public void triggerTimePickerDialog(Instant meetingDate) {
        // créer la factory (fabrique) de sélecteurs de l'heure
        TimePickerFactory factory = new TimePickerFactory();
        // obtenir le fragment de sélecteur de l'heure
        TimePickerFragment fragment = factory.getFragment(
                // date initiale
                meetingDate,
                // lorsqu'une heure est sélectionnée par l'utilisateur, la propager au présentateur
                (date) -> mPresenter.onMeetingTimeSelected(date)
        );
        // afficher le fragment de sélecteur de l'heure à l'écran
        fragment.display(getFragmentManager());
    }

    /**
     * Déclencher la boîte de dialogue d'ajout de participants à la réunion en cours
     * @param initialPersons les participants initiaux à définir dans la boîte de dialogue d'ajout de participants
     */
    @Override
    public void triggerAddPersonsDialog(Set<Person> initialPersons) {
        // créer la factory (fabrique) de dialogue d'ajout de participants
        AddPersonsDialogFactory factory = new AddPersonsDialogFactory();
        // obtenir le fragment de dialogue d'ajout de participants
        AddPersonsDialogDisplayable fragment = factory
                .getFragment(
                        // participants initiaux
                        initialPersons,
                        // lorsque des participants sont enregistrés par l'utilisateur, le propager au présenter
                        (persons) -> mPresenter.onPersonsChanged(persons)
                );
        // afficher le fragment de dialogue d'ajout de participants à l'écran
        fragment.display(getFragmentManager());
    }

}

