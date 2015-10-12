package org.technowolves.otpradar.view.activity;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.technowolves.otpradar.R;

public class SettingsActivity extends AppCompatActivity {

    public static final String[] SEASONS = new String[] {"---Choose a season---", "2016: ?????",
            "2015: Recycle Rush"};

    //public static final String PREFS_KEY = "org.technowolves.otpradar.PREFS_KEY";
    public static final String PREF_NUMBER = "tm_number";
    public static final String PREF_NAME = "tm_name";
    public static final String PREF_SEASON = "frc_season";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
