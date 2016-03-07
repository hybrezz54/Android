package org.technowolves.otpradar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment extends ListFragment implements Callback<List<Team>>,
        ActivityInteractionListener {

    public static String TBA_DATA_DIR = "downloaded";

    private FragmentInteractionListener mFragListener;
    private MainFragInteractionListener mListener;

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        TeamAdapter adapter = new TeamAdapter(getContext(), new ArrayList<Team>());
        setListAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainFragInteractionListener &&
                context instanceof FragmentInteractionListener) {
            mFragListener = (FragmentInteractionListener) context;
            mListener = (MainFragInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainFragInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragListener = null;
        mListener = null;
    }

    @Override
    public void onButtonPressed() {
        if (IoUtils.isFileExisting(getContext(), TBA_DATA_DIR, getFileName())) {
            Gson gson = new Gson();
            String json = IoUtils.readStringFromFile(getContext(), TBA_DATA_DIR, getFileName());
            List<Team> teams = gson.fromJson(json, new TypeToken<List<Team>>() {}.getType());

            ArrayAdapter<Team> adapter = (ArrayAdapter<Team>) getListAdapter();
            adapter.clear();
            adapter.addAll(teams);
            adapter.notifyDataSetChanged();
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.thebluealliance.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            String eventKey = prefs.getString(SettingsActivity.EVENT_KEY, "2016ncral");

            BlueAllianceService service = retrofit.create(BlueAllianceService.class);
            Call<List<Team>> call = service.listTeams(eventKey, "hybrezz54:ontheprowl:1");
            call.enqueue(this);
        }
    }

    @Override
    public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
        ArrayAdapter<Team> adapter = (ArrayAdapter<Team>) getListAdapter();
        adapter.clear();

        List<Team> teams = (List) response.body();
        Collections.sort(teams, new Comparator<Team>() {
            @Override
            public int compare(Team lhs, Team rhs) {
                Integer team1 = Integer.parseInt(lhs.getNumber());
                Integer team2 = Integer.parseInt(rhs.getNumber());
                return team1.compareTo(team2);
            }
        });

        adapter.addAll(teams);
        adapter.notifyDataSetChanged();

        if (IoUtils.isExternalStorageAvailable() || IoUtils.isExternalStorageReadOnly()) {
            String json = new Gson().toJson(teams);
            IoUtils.writeBytestoFile(getContext(), TBA_DATA_DIR, getFileName(), json.getBytes());
        }
    }

    @Override
    public void onFailure(Call<List<Team>> call, Throwable t) {
        mFragListener.onCreateSnackbar(getListView(),
                "Failed to download teams from The Blue Alliance.");
    }

    private String getFileName() {
        SharedPreferences manager = PreferenceManager.getDefaultSharedPreferences(getContext());

        String filename = manager.getString(SettingsActivity.EVENT_KEY, "2016ncral");
        filename += "_teams.json";

        return filename;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface MainFragInteractionListener {

    }

}
