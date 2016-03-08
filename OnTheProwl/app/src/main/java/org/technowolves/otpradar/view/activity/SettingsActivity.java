package org.technowolves.otpradar.view.activity;

import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.technowolves.otpradar.BlueAllianceService;
import org.technowolves.otpradar.model.Event;
import org.technowolves.otpradar.EventAdapter;
import org.technowolves.otpradar.R;
import org.technowolves.otpradar.SharedMap;
import org.technowolves.otpradar.util.IoUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsActivity extends AppCompatActivity implements Callback<List<Event>> {

    public static final String EVENT_KEY = "event_key";
    public static final String EVENT_NAME = "event_name";
    public static final String EVENT_LOCATION = "event_location";
    public static final String TM_NUMBER = "tm_number";
    public static final String TM_NAME = "tm_name";
    public static final String FRC_SEASON = "frc_season";

    private AutoCompleteTextView eventInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        eventInfo = (AutoCompleteTextView) findViewById(R.id.eventInfo);
        eventInfo.setThreshold(1);
        eventInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventAdapter adapter = (EventAdapter) parent.getAdapter();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                prefs.edit().putString(EVENT_KEY, adapter.getItem(position).getKey())
                        .putString(EVENT_NAME, adapter.getItem(position).getName())
                        .putString(EVENT_LOCATION, adapter.getItem(position).getLocation())
                        .apply();
            }
        });

        getEvents();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Event event = new Event(prefs.getString(EVENT_KEY, ""), prefs.getString(EVENT_NAME, ""),
                prefs.getString(EVENT_LOCATION, ""));
        /*if (!event.getKey().equals("") && !event.getName().equals("") && !event.getLocation().equals(""))
            eventInfo.setSelection(((EventAdapter) eventInfo.getAdapter()).getPosition(event));*/
    }

    @Override
    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
        List<Event> events = (List) response.body();
        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event lhs, Event rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        EventAdapter adapter = new EventAdapter(this,
                android.R.layout.simple_dropdown_item_1line, events);
        eventInfo.setAdapter(adapter);

        if (IoUtils.isExternalStorageAvailable() || IoUtils.isExternalStorageReadOnly()) {
            String json = new Gson().toJson(events);
            IoUtils.writeBytestoFile(this, SharedMap.TBA_DATA_DIR, getFileName(),
                    json.getBytes());
        }
    }

    @Override
    public void onFailure(Call<List<Event>> call, Throwable t) {
        onCreateSnackbar(eventInfo, "Failed to download events from The Blue Alliance.");
    }

    public void getEvents() {
        if (!addTeamsFromStorage()) {
            SharedPreferences manager = PreferenceManager.getDefaultSharedPreferences(this);
            int idx = Integer.parseInt(manager.getString(FRC_SEASON, "0"));
            String year = getResources().getStringArray(R.array.season_names)[idx];
            year = year.substring(0, 4);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.thebluealliance.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            BlueAllianceService service = retrofit.create(BlueAllianceService.class);
            Call<List<Event>> call = service.listEvents(year, "hybrezz54:ontheprowl:1");
            call.enqueue(this);
        }
    }

    private boolean addTeamsFromStorage() {
        boolean fileExists = IoUtils.isFileExisting(this, SharedMap.TBA_DATA_DIR, getFileName());

        if (fileExists) {
            Gson gson = new Gson();
            String json = IoUtils.readStringFromFile(this, SharedMap.TBA_DATA_DIR, getFileName());
            List<Event> events = gson.fromJson(json, new TypeToken<List<Event>>() {}.getType());
            EventAdapter adapter = (EventAdapter) eventInfo.getAdapter();
            adapter.clear();
            adapter.addAll(events);
            adapter.notifyDataSetChanged();
        }

        return fileExists;
    }

    private String getFileName() {
        SharedPreferences manager = PreferenceManager.getDefaultSharedPreferences(this);
        int idx = Integer.parseInt(manager.getString(FRC_SEASON, "0"));
        String season = getResources().getStringArray(R.array.season_names)[idx];

        String filename = season.substring(0, 4);
        filename += "_events.json";

        return filename;
    }

    private void onCreateSnackbar(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .show();
    }

    public static class SettingsFragment extends PreferenceFragment implements
            SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            // show the current value in the settings screen
            for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
                initSummary(getPreferenceScreen().getPreference(i));
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            updatePreferences(findPreference(key));
        }

        private void initSummary(Preference p) {
            if (p instanceof PreferenceCategory) {
                PreferenceCategory cat = (PreferenceCategory) p;
                for (int i = 0; i < cat.getPreferenceCount(); i++) {
                    initSummary(cat.getPreference(i));
                }
            } else {
                updatePreferences(p);
            }
        }

        private void updatePreferences(Preference p) {
            if (p instanceof EditTextPreference) {
                EditTextPreference editTextPref = (EditTextPreference) p;
                p.setSummary(editTextPref.getText());
            } else if (p instanceof ListPreference) {
                ((SettingsActivity) getActivity()).getEvents();
            }
        }
    }

}
