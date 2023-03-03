package com.openclassrooms.mareu.ui.add_persons.mvvm;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.model.Person;
import com.openclassrooms.mareu.ui.add_persons.AddPersonsDialogDisplayable;
import com.openclassrooms.mareu.utils.PersonsListFormatter;
import com.openclassrooms.mareu.utils.ui.SimpleTextWatcher;

import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPersonsDialogFragment extends DialogFragment
        implements AddPersonsDialogDisplayable {

    /**
     * Tell if the UI is ready to be used by the ViewModel
     */
    private Boolean isUIReady = false;

    /**
     * The ViewModel
     */
    private AddPersonsDialogViewModel mViewModel;

    /**
     * Declare Class tag for logging
     */
    private static final String TAG = "AddPersonsDialogFragmentMVVM";

    /**
     * Minimum characters required for an email to add that person
     */
    private static final int MINIMUM_EMAIL_LENGTH_TO_SHOW_ADD_BUTTON = 4;

    /**
     * Bind UI components
     */

    // represent the UI toolbar (with the "save" button)
    @BindView(R.id.fragment_add_persons_toolbar)
    Toolbar mAddPersonsToolbar;

    // represent the "add single person" button next to the UI text input
    @BindView(R.id.fragment_add_person_btn)
    ImageButton mAddPersonsButton;

    // represent the UI text input
    @BindView(R.id.fragment_add_person_text_input)
    TextInputLayout mAddPersonsTextInput;

    // represent the UI text view for displaying the list of persons
    @BindView(R.id.fragment_add_persons_full_list_text)
    TextView mAddPersonsFullListText;

    /**
     * Simple facility interface to notify the fragment is dismissed
     */
    public interface OnDismissListener {
        void onDismiss();
    }
    private OnDismissListener mOnDismissListener;

    /**
     * Constructor
     */
    public AddPersonsDialogFragment(
            AddPersonsDialogViewModel mViewModel,
            OnDismissListener onDismissListener
    ) {
        // always call the super class constructor
        super();
        // set the ViewModel
        this.mViewModel = mViewModel;
        // set the dismiss listener
        this.mOnDismissListener = onDismissListener;
    }

    /**
     * Create a new instance
     */
    public static AddPersonsDialogFragment newInstance(
            AddPersonsDialogViewModel mViewModel,
            OnDismissListener onDismissListener
    ) {
        return new AddPersonsDialogFragment(mViewModel, onDismissListener);
    }

    /**
     * Implements the display method, to show the UI
     */
    @Override
    public void display(FragmentManager fragmentManager) {
        show(fragmentManager, TAG);
        updatePersonsList(Objects.requireNonNull(mViewModel.getPersons().getValue()));
    }

    /**
     * Build the full list of persons as string, when the presenter request it
     */
    public void updatePersonsList(Set<Person> personsSet) {
        if(!isUIReady) return;

        // if the person set is empty, display a message
        if(personsSet.isEmpty()){
            mAddPersonsFullListText.setText("");
        } else {
            // create persons formatter
            PersonsListFormatter personsListFormatter = new PersonsListFormatter(personsSet);
            // set the list of persons in the UI
            mAddPersonsFullListText.setText(personsListFormatter.format());
        }
    }

    /**
     * We override onCreate, to define that we want to retain the state of the fragment
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // always call the super class method
        super.onCreate(savedInstanceState);
        // responsive purpose : if you rotate the device, the retained fragments
        // will remain there (they're not destroyed and recreated)
        setRetainInstance(true);
        // suppress the title bar, and adopt a light theme
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_LightDialog);
    }

    /**
     * We override onCreateView, to further define the behavior of the UI at startup
     * @param inflater the layout inflater
     * @param container the container
     * @param savedInstanceState the saved instance state
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // always call the super class method
        super.onCreateView(inflater, container, savedInstanceState);
        // inflate the layout
        View view = inflater.inflate(R.layout.fragment_add_people_dialog, container, false);
        // bind the UI components
        ButterKnife.bind(this, view);

        // configure the add person text input
        configureAddPersonText();
        // configure the add person button
        configureAddPersonButton();

        // start to observe the ViewModel Persons list
        mViewModel.getPersons().observe(this, new Observer<Set<Person>>() {
            @Override
            public void onChanged(Set<Person> persons) {
                updatePersonsList(persons);
            }
        });

        isUIReady = true;

        // return the view
        return view;
    }

    /**
     * You should inflate your layout in onCreateView but shouldn't
     * initialize other views (like inflating the menu) in onCreateView.
     *
     * So to avoid crash, we need to call such UI method in onViewCreated
     *
     * @param view the view
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // always call the super class method
        super.onViewCreated(view, savedInstanceState);
        // load (inflate) the menu into the toolbar
        mAddPersonsToolbar.inflateMenu(R.menu.add_persons_menu);
        // configure the return button on the toolbar to dismiss the dialog
        mAddPersonsToolbar.setNavigationOnClickListener(v -> dismiss());
        // configure the save button on the toolbar
        mAddPersonsToolbar.setOnMenuItemClickListener(item -> {
            // dismiss the dialog (return to the previous fragment)
            dismiss();
            // tell the caller the list of persons will no longer change, because of dismiss
            this.mOnDismissListener.onDismiss();
            // return true to consume the event
            return true;
        });
    }

    /**
     * Configure the add person text input
     */
    private void configureAddPersonText() {
        mAddPersonsTextInput.getEditText().addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // get the text len from the text (email) input
                int textLen = mAddPersonsTextInput.getEditText().getText().length();
                // if the text len is greater than MINIMUM_EMAIL_LEN, enable the add person button
                if (textLen > MINIMUM_EMAIL_LENGTH_TO_SHOW_ADD_BUTTON) {
                    // show the add button
                    mAddPersonsButton.setVisibility(View.VISIBLE);
                } else {
                    // hide the add button
                    mAddPersonsButton.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * Configure the add person button
     */
    private void configureAddPersonButton() {
        mAddPersonsButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                // retrieve the email from the text input
                String email = mAddPersonsTextInput.getEditText().getText().toString();
                // build the corresponding Person object from the email
                Person person = new Person(email);
                // tell the ViewModel to add the person to the list
                mViewModel.createPerson(person);
                // clear the text input
                mAddPersonsTextInput.getEditText().setText("");
                // hide the add button
                mAddPersonsButton.setVisibility(View.GONE);
            }
        });
    }



}
