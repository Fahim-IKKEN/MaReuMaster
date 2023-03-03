package com.openclassrooms.mareu.ui.main;

import android.os.Bundle;
import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.ui.meetings_list.MeetingsListFragment;
import com.openclassrooms.mareu.repository.fake.MeetingsListFakeRepository;
import com.openclassrooms.mareu.ui.meetings_list.MeetingsListPresenter;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Main Activity
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The underlying fragment presenter
     */
    private MeetingsListPresenter mMeetingsListPresenter;

    /**
     * called when the activity is created (ui life cycle android)
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // call the super method
        super.onCreate(savedInstanceState);
        // set the content view
        setContentView(R.layout.activity_main);

        // try to recover the existing fragment
        MeetingsListFragment mMeetingsListFragment = (MeetingsListFragment)
                getSupportFragmentManager()
                .findFragmentById(R.id.activity_meetings);

        // if the fragment doesn't exist, create it
        if (mMeetingsListFragment == null) {
            // create the fragment
            mMeetingsListFragment = MeetingsListFragment.newInstance();
            // add the fragment to the activity
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_meetings, mMeetingsListFragment)
                    .commit();
        }

        // create the model
        MeetingsListFakeRepository meetingsListFakeRepository = new MeetingsListFakeRepository();

        // create the presenter
        mMeetingsListPresenter = new MeetingsListPresenter(mMeetingsListFragment, meetingsListFakeRepository);
    }

    /**
     * called when a back button is pressed on the registration (add) meeting fragment
     */
    public void updateMeetingsFragments() {
        mMeetingsListPresenter.onRefreshMeetingsListRequested();
    }

}
