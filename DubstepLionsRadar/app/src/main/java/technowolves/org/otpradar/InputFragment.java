package technowolves.org.otpradar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

public class InputFragment extends Fragment {

    private static final String PREFS_KEY = "technowolves.org.otpradar.InputFragment.PREFERENCE_FILE_KEY";
    private static final String TEAM1_KEY = "TEAM_ONE";
    private static final String TEAM2_KEY = "TEAM_TWO";
    private static final String TEAM3_KEY = "TEAM_THREE";
    private static final String TEAM4_KEY = "TEAM_FOUR";
    private static final String BAD_TEAM1_KEY = "BAD_TEAM_ONE";
    private static final String BAD_TEAM2_KEY = "BAD_TEAM_TWO";
    private static final String BAD_TEAM3_KEY = "BAD_TEAM_THREE";
    private static final String BAD_TEAM4_KEY = "BAD_TEAM_FOUR";
    private static final String STRATEGY_KEY = "STRATEGY";
    private static final String PROSPECT_KEY = "PROSPECT";
    private static final String REASON_KEY = "REASON";

    private static final String ARG_POS_NUMBER = "INPUT_FRAG_POS";
    private static final String ARG_EDITING = "INPUT_FRAG_STATE";

    private static final String[] HEADER = new String[] {"Team #", "Team Name", "Good Team #1", "Good Team #2", "Good Team #3", "Good Team #4",
            "Bad Team #1", "Bad Team #2", "Bad Team #3", "Bad Team #4", "Strategy", "Prospect"};
    private static final String[] PROSPECT = new String[] {"------", "Highly likely to get into semifinalist rounds", "Not Sure/Neutral",
            "Maybe next year"};
    private static final String[] REASON = new String[] {"------", "Ongoing Team Probz", "Uncooperative alliance member",
            "BEST ALLIANCE!!! (Others counter weaknesses)", "We are da' bomb! (Think they contributed more to their own success)"};

    private boolean isEditing;
    private int mPosition;

    private SharedPreferences mPrefs;

    private EditText mTeam1;
    private EditText mTeam2;
    private EditText mTeam3;
    private EditText mTeam4;
    private EditText mBadTeam1;
    private EditText mBadTeam2;
    private EditText mBadTeam3;
    private EditText mBadTeam4;
    private EditText mStrategy;
    private Spinner mProspect;
    private Spinner mReason;

    public static InputFragment newInstance(int position, boolean editing) {
        InputFragment fragment = new InputFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POS_NUMBER, position);
        args.putBoolean(ARG_EDITING, editing);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        Bundle args = this.getArguments();
        mPosition = args.getInt(ARG_POS_NUMBER);
        isEditing = args.getBoolean(ARG_EDITING);

        updatePrefs();

        ActionBar bar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setTitle("Team Inputs");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_team_input, container, false);
        mTeam1 = (EditText) rootView.findViewById(R.id.edtTeam1);
        mTeam2 = (EditText) rootView.findViewById(R.id.edtTeam2);
        mTeam3 = (EditText) rootView.findViewById(R.id.edtTeam3);
        mTeam4 = (EditText) rootView.findViewById(R.id.edtTeam4);
        mBadTeam1 = (EditText) rootView.findViewById(R.id.edtBadTeam1);
        mBadTeam2 = (EditText) rootView.findViewById(R.id.edtBadTeam2);
        mBadTeam3 = (EditText) rootView.findViewById(R.id.edtBadTeam3);
        mBadTeam4 = (EditText) rootView.findViewById(R.id.edtBadTeam4);
        mStrategy = (EditText) rootView.findViewById(R.id.strategy);
        mProspect = (Spinner) rootView.findViewById(R.id.prospect);
        mReason = (Spinner) rootView.findViewById(R.id.reason);

        initSpinner();
        loadValues();

        if (!isEditing)
            viewsEnabled(false);

        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveValues();
        super.onSaveInstanceState(outState);
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
                fm.popBackStack();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void export(final String[] teams) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
        builder.setTitle("Share via Bluetooth");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage("Do you really wish to send all team inputs via bluetooth?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CsvWriter writer = new CsvWriter(getActivity(), "team_input", HEADER,
                        getStringsFromFields(teams, HEADER.length));
                writer.writeFile();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/*");
                intent.setPackage("com.android.bluetooth");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(writer.getFile()));
                startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

    public void remove(int position) {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_KEY + position, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    public void updatePosition(int position) {
        mPosition = position;
        updatePrefs();
        loadValues();
    }

    public void updateEditing(boolean editing) {
        this.isEditing = editing;
        viewsEnabled(editing);
    }

    private String[] getStringsFromFields(String[] front, int length) {
        String[] values = new String[(front.length / 2) * length];
        int counter = 0;
        int index = 0;

        for (int i = 0; i < (front.length / 2); i++) {
            updatePosition(i);
            values[counter] = front[index];
            counter++;
            index++;
            values[counter] = front[index];
            counter++;
            index++;
            values[counter] = mPrefs.getString(TEAM1_KEY, "");
            counter++;
            values[counter] = mPrefs.getString(TEAM2_KEY, "");
            counter++;
            values[counter] = mPrefs.getString(TEAM3_KEY, "");
            counter++;
            values[counter] = mPrefs.getString(TEAM4_KEY, "");
            counter++;
            values[counter] = mPrefs.getString(BAD_TEAM1_KEY, "");
            counter++;
            values[counter] = mPrefs.getString(BAD_TEAM2_KEY, "");
            counter++;
            values[counter] = mPrefs.getString(BAD_TEAM3_KEY, "");
            counter++;
            values[counter] = mPrefs.getString(BAD_TEAM4_KEY, "");
            counter++;
            values[counter] = mPrefs.getString(STRATEGY_KEY, "");
            counter++;
            values[counter] = mPrefs.getString(STRATEGY_KEY, "");
            counter++;
            values[counter] = processProspect(mPrefs.getInt(PROSPECT_KEY, 0));
            counter++;
            values[counter] = processReason(mPrefs.getInt(REASON_KEY, 0));
            counter++;
        }

        return values;
    }

    private void loadValues() {
        String team1 = mPrefs.getString(TEAM1_KEY, "");
        String team2 = mPrefs.getString(TEAM2_KEY, "");
        String team3 = mPrefs.getString(TEAM3_KEY, "");
        String team4 = mPrefs.getString(TEAM4_KEY, "");
        String badteam1 = mPrefs.getString(BAD_TEAM1_KEY, "");
        String badteam2 = mPrefs.getString(BAD_TEAM2_KEY, "");
        String badteam3 = mPrefs.getString(BAD_TEAM3_KEY, "");
        String badteam4 = mPrefs.getString(BAD_TEAM4_KEY, "");
        String strategy = mPrefs.getString(STRATEGY_KEY, "");
        int prospect = mPrefs.getInt(PROSPECT_KEY, 0);
        int reason = mPrefs.getInt(REASON_KEY, 0);

        mTeam1.setText(team1);
        mTeam2.setText(team2);
        mTeam3.setText(team3);
        mTeam4.setText(team4);
        mBadTeam1.setText(badteam1);
        mBadTeam2.setText(badteam2);
        mBadTeam3.setText(badteam3);
        mBadTeam4.setText(badteam4);
        mStrategy.setText(strategy);
        mProspect.setSelection(prospect);
        mReason.setSelection(reason);
    }

    private void saveValues() {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(TEAM1_KEY, mTeam1.getText().toString());
        editor.putString(TEAM2_KEY, mTeam2.getText().toString());
        editor.putString(TEAM3_KEY, mTeam3.getText().toString());
        editor.putString(TEAM4_KEY, mTeam4.getText().toString());
        editor.putString(BAD_TEAM1_KEY, mBadTeam1.getText().toString());
        editor.putString(BAD_TEAM2_KEY, mBadTeam2.getText().toString());
        editor.putString(BAD_TEAM3_KEY, mBadTeam3.getText().toString());
        editor.putString(BAD_TEAM4_KEY, mBadTeam4.getText().toString());
        editor.putString(STRATEGY_KEY, mStrategy.getText().toString());
        editor.putInt(PROSPECT_KEY, mProspect.getSelectedItemPosition());
        editor.putInt(REASON_KEY, mReason.getSelectedItemPosition());
        editor.commit();
    }

    private void viewsEnabled(boolean isEditing) {
        mTeam1.setEnabled(isEditing);
        mTeam2.setEnabled(isEditing);
        mTeam3.setEnabled(isEditing);
        mTeam4.setEnabled(isEditing);
        mBadTeam1.setEnabled(isEditing);
        mBadTeam2.setEnabled(isEditing);
        mBadTeam3.setEnabled(isEditing);
        mBadTeam4.setEnabled(isEditing);
        mStrategy.setEnabled(isEditing);
        mProspect.setEnabled(isEditing);
        mReason.setEnabled(isEditing);
    }

    private void initSpinner() {
        ArrayAdapter<String> prospect = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, PROSPECT);
        ArrayAdapter<String> reason = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, REASON);

        mProspect.setAdapter(prospect);
        mReason.setAdapter(reason);
    }

    private void updatePrefs() {
        mPrefs = getActivity().getSharedPreferences(PREFS_KEY + mPosition, Context.MODE_PRIVATE);
    }

    private String processProspect(int index) {
        return PROSPECT[index];
    }

    private String processReason(int index) {
        return REASON[index];
    }

}
