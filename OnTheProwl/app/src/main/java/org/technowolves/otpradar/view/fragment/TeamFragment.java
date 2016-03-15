package org.technowolves.otpradar.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.google.gson.Gson;

import org.technowolves.otpradar.ActivityInteractionListener;
import org.technowolves.otpradar.FragmentInteractionListener;
import org.technowolves.otpradar.R;
import org.technowolves.otpradar.SharedMap;
import org.technowolves.otpradar.model.Team;
import org.technowolves.otpradar.util.IoUtils;
import org.technowolves.otpradar.view.activity.SettingsActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TeamFragment.TeamFragInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamFragment extends Fragment implements ActivityInteractionListener {

    public static final String[] AWARDS = new String[] {"------", "Rookie All Star Award", "Chairman's Award", "Creativity Award", "Dean's List Award",
            "Engineering Excellence Award", "Engineering Inspiration Award", "Entrepreneurship Award", "Gracious Professionalism", "Imagery Award",
            "Industrial Design Award", "Industrial Safety Award", "Innovation in Control Award", "Media & Tech. Innovation Award",
            "Judges' Award", "Quality Award", "Team Spirit Award", "Woodie Flowers Finalist Award", "Rookie Inspiration Award",
            "Highest Rookie Seed", "Regional Finalist", "Regional Winner"};

    private FragmentInteractionListener mFragListener;
    private TeamFragInteractionListener mListener;
    private Team mTeam;

    private EditText website;
    private EditText location;
    private EditText rookie;
    private EditText competition;
    private Spinner award1;
    private EditText year1;
    private Spinner award2;
    private EditText year2;
    private EditText notes;
    private RatingBar driverExp;
    private RatingBar humanExp;

    public TeamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TeamFragment.
     */
    public static TeamFragment newInstance() {
        TeamFragment fragment = new TeamFragment();
        /*Bundle args = new Bundle();
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team, container, false);

        website = (EditText) view.findViewById(R.id.website);
        location = (EditText) view.findViewById(R.id.location);
        rookie = (EditText) view.findViewById(R.id.rookieYr);
        competition = (EditText) view.findViewById(R.id.numCompetition);
        award1 = (Spinner) view.findViewById(R.id.award1);
        year1 = (EditText) view.findViewById(R.id.year1);
        award2 = (Spinner) view.findViewById(R.id.award2);
        year2 = (EditText) view.findViewById(R.id.year2);
        notes = (EditText) view.findViewById(R.id.notes);
        driverExp = (RatingBar) view.findViewById(R.id.driverExp);
        humanExp = (RatingBar) view.findViewById(R.id.humanExp);

        ArrayAdapter<String> award = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, AWARDS);
        award1.setAdapter(award);
        award2.setAdapter(award);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TeamFragInteractionListener &&
                context instanceof FragmentInteractionListener) {
            mFragListener = (FragmentInteractionListener) context;
            mListener = (TeamFragInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RobotFragInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onButtonPressed() {
        if (mTeam != null) {
            mTeam.setWebsite(website.getText().toString());
            mTeam.setLocation(location.getText().toString());
            mTeam.setRookie(rookie.getText().toString());
            mTeam.setCompetition(competition.getText().toString());
            mTeam.setAward1(award1.getSelectedItemPosition());
            mTeam.setYear1(year1.getText().toString());
            mTeam.setAward2(award2.getSelectedItemPosition());
            mTeam.setYear2(year2.getText().toString());
            mTeam.setNotes(notes.getText().toString());
            mTeam.setDriverExp(driverExp.getRating());
            mTeam.setHumanExp(humanExp.getRating());

            if (IoUtils.isExternalStorageAvailable() || IoUtils.isExternalStorageReadOnly()) {
                String json = new Gson().toJson(mTeam);
                IoUtils.writeBytestoFile(getContext(), SharedMap.USER_DATA_DIR,
                        getFileName(mTeam.getNumber()), json.getBytes());
            }
        }
    }

    public void onListItemClick(Team team) {
        mTeam = team;

        if (team != null) {
            String number = team.getNumber();
            boolean fileExists = IoUtils.isFileExisting(getContext(),
                    SharedMap.USER_DATA_DIR, getFileName(number));

            if (fileExists) {

                Gson gson = new Gson();
                String json = IoUtils.readStringFromFile(getContext(),
                        SharedMap.USER_DATA_DIR, getFileName(number));
                mTeam = gson.fromJson(json, Team.class);
            }
        }

        setupViews();
    }

    private void setupViews() {
        if (mTeam != null) {
            website.setText(mTeam.getWebsite());
            location.setText(mTeam.getLocation());
            rookie.setText(mTeam.getRookie());
            competition.setText(mTeam.getCompetition());
            award1.setSelection(mTeam.getAward1());
            year1.setText(mTeam.getYear1());
            award2.setSelection(mTeam.getAward2());
            year2.setText(mTeam.getYear2());
            notes.setText(mTeam.getNotes());
            driverExp.setRating(mTeam.getDriverExp());
            humanExp.setRating(mTeam.getHumanExp());
        } else {
            website.setText("");
            location.setText("");
            rookie.setText("");
            competition.setText("");
            award1.setSelection(0);
            year1.setText("");
            award2.setSelection(0);
            year2.setText("");
            notes.setText("");
            driverExp.setRating(0);
            humanExp.setRating(0);
        }
    }

    private String getFileName(String number) {
        if (number.equals(""))
            number ="0";

        SharedPreferences manager = PreferenceManager.getDefaultSharedPreferences(getContext());
        String filename = manager.getString(SettingsActivity.EVENT_KEY, "2016ncral");
        filename += "_team_" + number + ".json";

        return filename;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface TeamFragInteractionListener {
    }
}
