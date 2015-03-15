package technowolves.org.otpradar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class TeamFragment extends Fragment {

    public static final String PREFS_KEY = "technowolves.org.otpradar.PREFERENCE_FILE_KEY";
    public static final String NUMBER_KEY = "TEAM_NUMBER";
    public static final String NAME_KEY = "TEAM_NAME";
    public static final String SITE_KEY = "WEB_SITE";
    public static final String LOCATION_KEY = "LOCATION";
    public static final String TOTAL_KEY = "TOTAL_YEARS";
    public static final String OTHER_KEY = "PARTICIPATION";
    public static final String AWARD1_KEY = "AWARD_ONE_KEY";
    public static final String AWARD2_KEY = "AWARD_TWO_KEY";
    public static final String AWARD3_KEY = "AWARD_THREE_KEY";
    public static final String YEAR1_KEY = "YEAR_ONE_KEY";
    public static final String YEAR2_KEY = "YEAR_TWO_KEY";
    public static final String YEAR3_KEY = "YEAR_THREE_KEY";
    public static final String NOTES_KEY = "NOTES_KEY";

    public static final String[] HEADER = new String[] {"Number", "Name", "Website", "Location", "Total Yrs.", "Another competition this year?", "Award 1",
            "Year 1", "Award 2", "Year 2", "Award 3", "Year 3", "Notes"};
    public static final String[] AWARDS = new String[] {"------", "Rookie All Star Award", "Chairman's Award", "Creativity Award", "Dean's List Award",
            "Engineering Excellence Award", "Engineering Inspiration Award", "Entrepreneurship Award", "Gracious Professionalism", "Imagery Award",
            "Industrial Design Award", "Industrial Safety Award", "Innovation in Control Award", "Media & Tech. Innovation Award",
            "Judges' Award", "Quality Award", "Team Spirit Award", "Woodie Flowers Finalist Award", "Rookie Inspiration Award",
            "Highest Rookie Seed", "Regional Finalist", "Regional Winner"};
    public static final String[] YEARS = new String[] {"------", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005",
            "2004", "2003", "2002", "2001", "2000"};
    public static final String[] SIMPLE = new String[] {"No", "Yes"};

    private static boolean isEditing;
    private static int mPosition;

    private EditText mNumber;
    private EditText mTeam;
    private EditText mSite;
    private EditText mLocation;
    private EditText mTotal;
    private Spinner mParticipation;
    private Spinner mAwardOne;
    private Spinner mYearOne;
    private Spinner mAwardTwo;
    private Spinner mYearTwo;
    private Spinner mAwardThree;
    private Spinner mYearThree;
    private EditText mNotes;

    public static TeamFragment newInstance(int position, boolean editing) {
        TeamFragment fragment = new TeamFragment();
        /*Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);*/
        mPosition = position;
        isEditing = editing;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        ActionBar bar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setTitle("Team Details");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (isEditing)
            getActivity().getMenuInflater().inflate(R.menu.save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fm = getFragmentManager();

        switch (id) {
            case android.R.id.home:
                fm.popBackStack();
                break;
            case R.id.action_save:
                saveValues();
                ((PrimaryListFragment)fm.findFragmentByTag("PrimaryListFragment1"))
                        .add(new Team(mNumber.getText().toString(), mTeam.getText().toString()));
                fm.popBackStack();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_team_data, container, false);
        mNumber = (EditText) rootView.findViewById(R.id.edtNumber);
        mTeam = (EditText) rootView.findViewById(R.id.edtName);
        mSite = (EditText) rootView.findViewById(R.id.edtWebsite);
        mLocation = (EditText) rootView.findViewById(R.id.edtLocation);
        mTotal = (EditText) rootView.findViewById(R.id.edtTotal);
        mParticipation = (Spinner) rootView.findViewById(R.id.participate);
        mAwardOne = (Spinner) rootView.findViewById(R.id.award1);
        mYearOne = (Spinner) rootView.findViewById(R.id.year1);
        mAwardTwo = (Spinner) rootView.findViewById(R.id.award2);
        mYearTwo = (Spinner) rootView.findViewById(R.id.year2);
        mAwardThree = (Spinner) rootView.findViewById(R.id.award3);
        mYearThree = (Spinner) rootView.findViewById(R.id.year3);
        mNotes = (EditText) rootView.findViewById(R.id.notes);

        initSpinner();
        loadValues();

        if (!isEditing)
            disableViews();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void initSpinner() {

        ArrayAdapter<String> simple = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, SIMPLE);
        ArrayAdapter<String> year = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, YEARS);
        ArrayAdapter<String> award = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, AWARDS);

        mParticipation.setAdapter(simple);
        mAwardOne.setAdapter(award);
        mAwardTwo.setAdapter(award);
        mAwardThree.setAdapter(award);
        mYearOne.setAdapter(year);
        mYearTwo.setAdapter(year);
        mYearThree.setAdapter(year);

    }

    private void disableViews() {
        mNumber.setEnabled(false);
        mTeam.setEnabled(false);
        mSite.setEnabled(false);
        mLocation.setEnabled(false);
        mTotal.setEnabled(false);
        mParticipation.setEnabled(false);
        mAwardOne.setEnabled(false);
        mYearOne.setEnabled(false);
        mAwardTwo.setEnabled(false);
        mYearTwo.setEnabled(false);
        mAwardThree.setEnabled(false);
        mYearThree.setEnabled(false);
        mNotes.setEnabled(false);
    }

    private void loadValues() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_KEY + mPosition, Context.MODE_PRIVATE);
        String number = prefs.getString(NUMBER_KEY, "");
        String name = prefs.getString(NAME_KEY, "");
        String site = prefs.getString(SITE_KEY, "");
        String location = prefs.getString(LOCATION_KEY, "");
        String total = prefs.getString(TOTAL_KEY, "");
        int other = prefs.getInt(OTHER_KEY, 0);
        int awardOne = prefs.getInt(AWARD1_KEY, 0);
        int awardTwo = prefs.getInt(AWARD2_KEY, 0);
        int awardThree = prefs.getInt(AWARD3_KEY, 0);
        int yearOne = prefs.getInt(YEAR1_KEY, 0);
        int yearTwo = prefs.getInt(YEAR2_KEY, 0);
        int yearThree = prefs.getInt(YEAR3_KEY, 0);
        String notes = prefs.getString(NOTES_KEY, "");

        mNumber.setText(number);
        mTeam.setText(name);
        mSite.setText(site);
        mLocation.setText(location);
        mTotal.setText(total);
        mParticipation.setSelection(other);
        mAwardOne.setSelection(awardOne);
        mAwardTwo.setSelection(awardTwo);
        mAwardThree.setSelection(awardThree);
        mYearOne.setSelection(yearOne);
        mYearTwo.setSelection(yearTwo);
        mYearThree.setSelection(yearThree);
        mNotes.setText(notes);
    }

    private void saveValues() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_KEY + mPosition, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(NUMBER_KEY, mNumber.getText().toString());
        editor.putString(NAME_KEY, mTeam.getText().toString());
        editor.putString(SITE_KEY, mSite.getText().toString());
        editor.putString(LOCATION_KEY, mLocation.getText().toString());
        editor.putString(TOTAL_KEY, mTotal.getText().toString());
        editor.putInt(OTHER_KEY, mParticipation.getSelectedItemPosition());
        editor.putInt(AWARD1_KEY, mAwardOne.getSelectedItemPosition());
        editor.putInt(AWARD2_KEY, mAwardTwo.getSelectedItemPosition());
        editor.putInt(AWARD3_KEY, mAwardThree.getSelectedItemPosition());
        editor.putInt(YEAR1_KEY, mYearOne.getSelectedItemPosition());
        editor.putInt(YEAR2_KEY, mYearTwo.getSelectedItemPosition());
        editor.putInt(YEAR3_KEY, mYearThree.getSelectedItemPosition());
        editor.putString(NOTES_KEY, mNotes.getText().toString());
        editor.commit();
    }

    /*public void removeValues(int index) {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_KEY + index, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }*/

}
