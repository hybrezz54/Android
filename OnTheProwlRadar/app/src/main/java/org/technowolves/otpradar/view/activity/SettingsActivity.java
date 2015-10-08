package org.technowolves.otpradar.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import org.technowolves.otpradar.R;

public class SettingsActivity extends AppCompatActivity {

    public static final String[] SEASONS = new String[] {"---Choose a season---", "2015: Recycle Rush", "2014: Aerial Assist",
            "2013: Ultimate Ascent", "2012: Rebound Rumble"};

    //public static final String PREFS_KEY = "org.technowolves.otpradar.PREFS_KEY";
    public static final String PREFS_SEASON = "frc_season";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
