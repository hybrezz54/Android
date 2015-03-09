package technowolves.org.dubsteplionsradar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class TeamFragment extends Fragment {

    private static final String PREFS_KEY = "technowolves.org.dubsteplionsradar.PREFERENCE_FILE_KEY";
    private static final String NUMBER_KEY = "TEAM_NUMBER";
    private static final String NAME_KEY = "TEAM_NAME";
    private static final String AWARD1_KEY = "AWARD_ONE_KEY";
    private static final String AWARD2_KEY = "AWARD_TWO_KEY";
    private static final String YEAR1_KEY = "YEAR_ONE_KEY";
    private static final String YEAR2_KEY = "YEAR_TWO_KEY";
    private static final String NOTES_KEY = "NOTES_KEY";

    private static boolean isEditing;
    private boolean saved = false;
    private static int mPosition;

    private TextView mNumber;
    private TextView mTeam;
    private Spinner mAwardOne;
    private Spinner mYearOne;
    private Spinner mAwardTwo;
    private Spinner mYearTwo;
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

        if (id == R.id.action_save) {
            saved = true;
            saveValues();
            ((PrimaryListFragment)fm.findFragmentByTag("PrimaryListFragment"))
                    .add(new Team(mNumber.getText().toString(), mTeam.getText().toString()));
            fm.popBackStack();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_team_data, container, false);
        mNumber = (TextView) rootView.findViewById(R.id.edtNumber);
        mTeam = (TextView) rootView.findViewById(R.id.edtName);
        mAwardOne = (Spinner) rootView.findViewById(R.id.award1);
        mYearOne = (Spinner) rootView.findViewById(R.id.year1);
        mAwardTwo = (Spinner) rootView.findViewById(R.id.award2);
        mYearTwo = (Spinner) rootView.findViewById(R.id.year2);
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

        ArrayAdapter<String> year = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                new String[] {"------", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005"});
        ArrayAdapter<String> award = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
                new String[] {"------", "Rookie All Star Award", "Chairman's Award", "Creativity Award", "Dean's List Award",
                "Engineering Excellence Award", "Engineering Inspiration Award", "Entrepreneurship Award", "Gracious Professionalism", "Imagery Award",
                "Industrial Design Award", "Industrial Safety Award", "Innovation in Control Award", "Media & Tech. Innovation Award",
                "Judges' Award", "Quality Award", "Team Spirit Award", "Woodie Flowers Finalist Award", "Rookie Inspiration Award",
                "Highest Rookie Seed", "Regional Finalist", "Regional Winner"});

        mAwardOne.setAdapter(award);
        mAwardTwo.setAdapter(award);
        mYearOne.setAdapter(year);
        mYearTwo.setAdapter(year);

    }

    private void disableViews() {
        mNumber.setEnabled(false);
        mTeam.setEnabled(false);
        mAwardOne.setEnabled(false);
        mYearOne.setEnabled(false);
        mAwardTwo.setEnabled(false);
        mYearTwo.setEnabled(false);
    }

    private void loadValues() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_KEY + mPosition, Context.MODE_PRIVATE);
        String number = prefs.getString(NUMBER_KEY, "");
        String name = prefs.getString(NAME_KEY, "");
        int awardOne = prefs.getInt(AWARD1_KEY, 0);
        int awardTwo = prefs.getInt(AWARD2_KEY, 0);
        int yearOne = prefs.getInt(YEAR1_KEY, 0);
        int yearTwo = prefs.getInt(YEAR2_KEY, 0);

        mNumber.setText(number);
        mTeam.setText(name);
        mAwardOne.setSelection(awardOne);
        mAwardTwo.setSelection(awardTwo);
        mYearOne.setSelection(yearOne);
        mYearTwo.setSelection(yearTwo);
    }

    private void saveValues() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_KEY + mPosition, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(NUMBER_KEY, mNumber.getText().toString());
        editor.putString(NAME_KEY, mTeam.getText().toString());
        editor.putInt(AWARD1_KEY, mAwardOne.getSelectedItemPosition());
        editor.putInt(AWARD2_KEY, mAwardTwo.getSelectedItemPosition());
        editor.putInt(YEAR1_KEY, mYearOne.getSelectedItemPosition());
        editor.putInt(YEAR2_KEY, mYearTwo.getSelectedItemPosition());
        editor.commit();
    }

}
