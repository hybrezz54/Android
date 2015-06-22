package org.technowolves.otpradar.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.technowolves.otpradar.R;
import org.technowolves.otpradar.framework.TeamInfoItem;
import org.technowolves.otpradar.framework.TeamListItem;

public class TeamInfoFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_ITEM_ID = "id";
    private static final String ARG_TEAM_NUMBER = "team_number";
    private static final String ARG_TEAM_NAME = "team_name";
    private static final String ARG_EDIT_MODE = "edit_mode";

    public static final String[] HEADER = new String[] {"Number", "Name", "Website", "Location", "Total Yrs.", "Another competition this year?", "Award 1",
            "Year 1", "Award 2", "Year 2", "Award 3", "Year 3", "Notes", "Driver #1 Name", "Driver #2 Name", "Drive Coach Name", "Is drive coach mentor?",
            "Driver Rating", "Human Player Rating", "HP: Can load totes?", "HP: Can load litter?", "HP: Can throw litter?"};
    public static final String[] AWARDS = new String[] {"------", "Rookie All Star Award", "Chairman's Award", "Creativity Award", "Dean's List Award",
            "Engineering Excellence Award", "Engineering Inspiration Award", "Entrepreneurship Award", "Gracious Professionalism", "Imagery Award",
            "Industrial Design Award", "Industrial Safety Award", "Innovation in Control Award", "Media & Tech. Innovation Award",
            "Judges' Award", "Quality Award", "Team Spirit Award", "Woodie Flowers Finalist Award", "Rookie Inspiration Award",
            "Highest Rookie Seed", "Regional Finalist", "Regional Winner"};
    public static final String[] YEARS = new String[] {"------", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005",
            "2004", "2003", "2002", "2001", "2000"};
    public static final String[] COMPETITION = new String[] {"No - 1st competition this year", "Yes - 2nd competition this year"};
    public static final String[] SIMPLE = new String[] {"------", "No", "Yes"};

    private OnFragmentInteractionListener mListener;

    private static String mNumber;
    private static String mName;
    private static boolean mEditMode;

    private TeamInfoItem mTeam;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TeamInfoFragment newInstance(TeamListItem teamListItem, TeamInfoItem teamInfoItem,
                                               boolean editMode) {
        TeamInfoFragment fragment = new TeamInfoFragment();
        fragment.setTeamInfoItem(teamInfoItem);

        Bundle args = new Bundle();
        args.putString(ARG_TEAM_NUMBER, teamListItem.getNumber());
        args.putString(ARG_TEAM_NAME, teamListItem.getName());
        args.putBoolean(ARG_EDIT_MODE, editMode);
        fragment.setArguments(args);

        return fragment;
    }

    public TeamInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mNumber = getArguments().getString(ARG_TEAM_NUMBER);
            mName = getArguments().getString(ARG_TEAM_NAME);
            mEditMode = getArguments().getBoolean(ARG_EDIT_MODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_team_info, container, false);

        TextView txtTeamInfo = (TextView) rootView.findViewById(R.id.txtTeamInfo);
        txtTeamInfo.setText(mNumber + " - " + mName);

        final EditText edtLocation = (EditText) rootView.findViewById(R.id.edtLocation);
        final EditText edtTotal = (EditText) rootView.findViewById(R.id.edtTotal);
        final Spinner spnParticipate = (Spinner) rootView.findViewById(R.id.participate);
        final Spinner spnAward1 = (Spinner) rootView.findViewById(R.id.award1);
        final Spinner spnYear1 = (Spinner) rootView.findViewById(R.id.year1);
        final Spinner spnAward2 = (Spinner) rootView.findViewById(R.id.award2);
        final Spinner spnYear2 = (Spinner) rootView.findViewById(R.id.year2);
        final Spinner spnAward3 = (Spinner) rootView.findViewById(R.id.award3);
        final Spinner spnYear3 = (Spinner) rootView.findViewById(R.id.year3);
        final EditText edtNotes = (EditText) rootView.findViewById(R.id.notes);
        final Spinner spnCoach = (Spinner) rootView.findViewById(R.id.coachMentor);
        final RatingBar rbDriver = (RatingBar) rootView.findViewById(R.id.driverRating);
        final RatingBar rbHp = (RatingBar) rootView.findViewById(R.id.hpRating);

        ArrayAdapter<String> comp = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, COMPETITION);
        ArrayAdapter<String> simple = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, SIMPLE);
        ArrayAdapter<String> year = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, YEARS);
        ArrayAdapter<String> award = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, AWARDS);

        spnParticipate.setAdapter(comp);
        spnAward1.setAdapter(award);
        spnAward2.setAdapter(award);
        spnAward3.setAdapter(award);
        spnYear1.setAdapter(year);
        spnYear2.setAdapter(year);
        spnYear3.setAdapter(year);
        spnCoach.setAdapter(simple);

        if (mEditMode) {
            edtLocation.setEnabled(false);
            edtTotal.setEnabled(false);
            spnParticipate.setEnabled(false);
            spnAward1.setEnabled(false);
            spnYear1.setEnabled(false);
            spnAward2.setEnabled(false);
            spnYear2.setEnabled(false);
            spnAward3.setEnabled(false);
            spnYear3.setEnabled(false);
            edtNotes.setEnabled(false);
            spnCoach.setEnabled(false);
            rbDriver.setEnabled(false);
            rbHp.setEnabled(false);
        }

        if (mTeam != null) {
            edtLocation.setText(mTeam.getLocation());
            edtTotal.setText(mTeam.getTotalYears());
            spnParticipate.setSelection(mTeam.getParticipate());
            spnAward1.setSelection(mTeam.getAward1());
            spnYear1.setSelection(mTeam.getYear1());
            spnAward2.setSelection(mTeam.getAward2());
            spnYear2.setSelection(mTeam.getYear2());
            spnAward3.setSelection(mTeam.getAward3());
            spnYear3.setSelection(mTeam.getYear3());
            edtNotes.setText(mTeam.getNotes());
            spnCoach.setSelection(mTeam.getCoachMentor());
            rbDriver.setRating(mTeam.getDriver());
            rbHp.setRating(mTeam.getHp());
        }

        FloatingActionButton fabSave = (FloatingActionButton) rootView.findViewById(R.id.fabSave);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean update = false;

                if (mTeam != null)
                    update = true;
                else
                    mTeam = new TeamInfoItem();

                mTeam.setLocation(edtLocation.getText().toString());
                mTeam.setTotalYears(edtTotal.getText().toString());
                mTeam.setParticipate(spnParticipate.getSelectedItemPosition());
                mTeam.setAward1(spnAward1.getSelectedItemPosition());
                mTeam.setYear1(spnYear1.getSelectedItemPosition());
                mTeam.setAward2(spnAward2.getSelectedItemPosition());
                mTeam.setYear2(spnYear2.getSelectedItemPosition());
                mTeam.setAward3(spnAward3.getSelectedItemPosition());
                mTeam.setYear3(spnYear3.getSelectedItemPosition());
                mTeam.setNotes(edtNotes.getText().toString());
                mTeam.setCoachMentor(spnCoach.getSelectedItemPosition());
                mTeam.setDriver(rbDriver.getRating());
                mTeam.setHp(rbHp.getRating());

                mListener.saveTeamInfoValues(mTeam, update);

            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setTeamInfoItem(TeamInfoItem team) {
        mTeam = team;
    }

    public interface OnFragmentInteractionListener {
        void saveTeamInfoValues(TeamInfoItem team, boolean update);
    }

}
