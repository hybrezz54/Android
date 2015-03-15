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
import android.widget.RatingBar;
import android.widget.Spinner;

public class RobotFragment extends Fragment {

    private static final String PREFS_KEY = "technowolves.org.otpradar.RobotFragment.PREFERENCE_FILE_KEY";
    private static final String STYLE_KEY = "ROBOT_STYLE";
    private static final String DRIVE_KEY = "DRIVE_TRAIN";
    private static final String WHEELS_KEY = "WHEELS";
    private static final String RATE_KEY = "ROBOT_RATE";
    private static final String NOTE_KEY = "ROBOT_NOTES";

    private static final String ARG_POS_NUMBER = "ROBOT_FRAG_POS";
    private static final String ARG_EDITING = "ROBOT_FRAG_STATE";

    private static final String[] HEADER = new String[] {"Team #", "Team Name", "Robot Style", "Drive Train", "Wheel Type", "Robot Rating", "Robot Notes"};
    private static final String[] ROBOT_STYLE = new String[] {"------", "Insane Tote Stacker/Lifter", "Recycle container picker-upper", "Tote hauler/pusher"};
    private static final String[] DRIVE_TRAIN = new String[] {"------", "Tank", "Mecanum", "Swerve", "Slide", "Holonomic"};
    private static final String[] WHEELS = new String[] {"------", "Mecanum", "Omni", "Tread", "Other"};

    private boolean isEditing;
    private int mPosition;

    private SharedPreferences mPrefs;

    private Spinner mRobotStyle;
    private Spinner mDriveTrain;
    private Spinner mWheels;
    private RatingBar mRate;
    private EditText mNotes;

    public static RobotFragment newInstance(int position, boolean editing) {
        RobotFragment fragment = new RobotFragment();
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
        bar.setTitle("Robot Details");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_robot_data, container, false);
        mRobotStyle = (Spinner) rootView.findViewById(R.id.robotStyle);
        mDriveTrain = (Spinner) rootView.findViewById(R.id.driveTrain);
        mWheels = (Spinner) rootView.findViewById(R.id.wheels);
        mRate = (RatingBar) rootView.findViewById(R.id.robotRate);
        mNotes = (EditText) rootView.findViewById(R.id.robotNotes);

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
        builder.setMessage("Do you really wish to send all robot data via bluetooth?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CsvWriter writer = new CsvWriter(getActivity(), "robot_data", HEADER,
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
            values[counter] = processStyles(mPrefs.getInt(STYLE_KEY, 0));
            counter++;
            values[counter] = processDrives(mPrefs.getInt(DRIVE_KEY, 0));
            counter++;
            values[counter] = processWheels(mPrefs.getInt(WHEELS_KEY, 0));
            counter++;
            values[counter] = String.valueOf(mPrefs.getFloat(RATE_KEY, 0f));
            counter++;
            values[counter] = processNotes(mPrefs.getString(NOTE_KEY, ""));
            counter++;
        }

        return values;
    }

    private void loadValues() {
        int style = mPrefs.getInt(STYLE_KEY, 0);
        int drive = mPrefs.getInt(DRIVE_KEY, 0);
        int wheel = mPrefs.getInt(WHEELS_KEY, 0);
        float rate = mPrefs.getFloat(RATE_KEY, 0f);
        String notes = mPrefs.getString(NOTE_KEY, "");

        mRobotStyle.setSelection(style);
        mDriveTrain.setSelection(drive);
        mWheels.setSelection(wheel);
        mRate.setRating(rate);
        mNotes.setText(notes);
    }

    private void saveValues() {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(STYLE_KEY, mRobotStyle.getSelectedItemPosition());
        editor.putInt(DRIVE_KEY, mDriveTrain.getSelectedItemPosition());
        editor.putInt(WHEELS_KEY, mWheels.getSelectedItemPosition());
        editor.putFloat(RATE_KEY, mRate.getRating());
        editor.putString(NOTE_KEY, mNotes.getText().toString());
        editor.commit();
    }

    private void viewsEnabled(boolean isEditing) {
        mRobotStyle.setEnabled(isEditing);
        mDriveTrain.setEnabled(isEditing);
        mWheels.setEnabled(isEditing);
        mRate.setEnabled(isEditing);
        mNotes.setEnabled(isEditing);
    }

    private void initSpinner() {
        ArrayAdapter<String> styles = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, ROBOT_STYLE);
        ArrayAdapter<String> drives = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, DRIVE_TRAIN);
        ArrayAdapter<String> wheels = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, WHEELS);

        mRobotStyle.setAdapter(styles);
        mDriveTrain.setAdapter(drives);
        mWheels.setAdapter(wheels);
    }

    private void updatePrefs() {
        mPrefs = getActivity().getSharedPreferences(PREFS_KEY + mPosition, Context.MODE_PRIVATE);
    }

    private String processStyles(int index) {
        return ROBOT_STYLE[index];
    }

    private String processDrives(int index) {
        return DRIVE_TRAIN[index];
    }

    private String processWheels(int index) {
        return WHEELS[index];
    }

    private String processNotes(String text) {
        return text.replace("\n", "   ");
    }

}
