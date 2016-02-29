package org.technowolves.otpradar.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.technowolves.otpradar.R;
import org.technowolves.otpradar.presenter.RobotInfoItem;
import org.technowolves.otpradar.presenter.TeamListItem;
import org.technowolves.otpradar.view.activity.SettingsActivity;

import java.util.Set;

public class RobotFragment extends Fragment {

    private static final String ARG_TEAM_NUMBER = "team_number";
    private static final String ARG_SEASON = "frc_season";
    private static final String ARG_POS_ID = "pos_id";
    private static final String ARG_EDIT_MODE = "edit_mode";

    public static final String[] STYLE_2015 = new String[] {"------", "TOTE STACKER/LIFTER", "CONTAINER CARRIER",
            "TOTE HAULER/PUSHER", "TOTE STACK + CONTAINER CARRY", "TOTE PUSH + CONTAINER CARRY",
            "STACK + PUSH + CARRY"};
    public static final String[] DRIVE_TRAIN = new String[] {"------", "TANK", "MECANUM", "SWERVE",
            "SLIDE", "HOLONOMIC", "OTHER"};
    public static final String[] WHEEL = new String[] {"------", "MECANUM", "OMNI", "TREAD", "OTHER"};
    public static final String[] START_POS_2015 = new String[] {"------", "AUTO ZONE", "MIDDLE STAGING ZONE",
            "SIDE STAGING ZONE (platform)" + "SIDE STAGING ZONE (no platform)"};
    public static final String[] SIMPLE = new String[] {"------", "NO", "YES"};

    private OnFragmentInteractionListener mListener;

    private static String mNumber;
    private static int mSeason;
    private static int mId;
    private static boolean mEditMode;

    private RobotInfoItem mRobot;

    EditText edtName, edtWeight, edtHeight, edtMaxTote;
    Spinner spnStyle, spnDrive, spnWheel, spnStrafe,
        spnMoveTote, spnMoveCont, spnAcqTote,
        spnAcqCont, spnStartPos;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static RobotFragment newInstance(RobotInfoItem robot, String number,
                                            int season, int id, boolean editMode) {
        RobotFragment fragment = new RobotFragment();
        fragment.setRobotInfoItem(robot);

        Bundle args = new Bundle();
        args.putString(ARG_TEAM_NUMBER, number);
        args.putInt(ARG_SEASON, season);
        args.putInt(ARG_POS_ID, id);
        args.putBoolean(ARG_EDIT_MODE, editMode);
        fragment.setArguments(args);
        return fragment;
    }

    public RobotFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mNumber = getArguments().getString(ARG_TEAM_NUMBER);
            mSeason = getArguments().getInt(ARG_SEASON);
            mId = getArguments().getInt(ARG_POS_ID);
            mEditMode = getArguments().getBoolean(ARG_EDIT_MODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_robot_info, container, false);

        edtName = (EditText) rootView.findViewById(R.id.robotName);
        spnStyle = (Spinner) rootView.findViewById(R.id.robotStyle);
        edtHeight = (EditText) rootView.findViewById(R.id.robotHeight);
        edtWeight = (EditText) rootView.findViewById(R.id.robotWeight);
        spnDrive = (Spinner) rootView.findViewById(R.id.driveTrain);
        spnWheel = (Spinner) rootView.findViewById(R.id.wheelType);
        spnStrafe = (Spinner) rootView.findViewById(R.id.spnStrafe);
        spnMoveTote = (Spinner) rootView.findViewById(R.id.moveTote);
        spnMoveCont = (Spinner) rootView.findViewById(R.id.moveContainer);
        spnAcqTote = (Spinner) rootView.findViewById(R.id.acqTote);
        spnAcqCont = (Spinner) rootView.findViewById(R.id.acqContainer);
        spnStartPos = (Spinner) rootView.findViewById(R.id.startingPos);
        edtMaxTote = (EditText) rootView.findViewById(R.id.maxTote);
        FloatingActionButton fabSave = (FloatingActionButton) rootView.findViewById(R.id.fabSave);

        ArrayAdapter<String> style = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, STYLE_2015);
        ArrayAdapter<String> drive = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, DRIVE_TRAIN);
        ArrayAdapter<String> wheel = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, WHEEL);
        ArrayAdapter<String> startPos = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, START_POS_2015);
        ArrayAdapter<String> simple = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, SIMPLE);

        spnStyle.setAdapter(style);
        spnDrive.setAdapter(drive);
        spnWheel.setAdapter(wheel);
        spnStrafe.setAdapter(simple);
        spnMoveTote.setAdapter(simple);
        spnMoveCont.setAdapter(simple);
        spnAcqTote.setAdapter(simple);
        spnAcqCont.setAdapter(simple);
        spnStartPos.setAdapter(startPos);

        if (!mEditMode) {
            edtName.setEnabled(false);
            spnStyle.setEnabled(false);
            edtHeight.setEnabled(false);
            edtWeight.setEnabled(false);
            spnDrive.setEnabled(false);
            spnWheel.setEnabled(false);
            spnStrafe.setEnabled(false);
            spnMoveTote.setEnabled(false);
            spnMoveCont.setEnabled(false);
            spnAcqTote.setEnabled(false);
            spnAcqCont.setEnabled(false);
            spnStartPos.setEnabled(false);
            edtMaxTote.setEnabled(false);
            fabSave.setVisibility(View.INVISIBLE);
        }

        if (mRobot != null) {
            mSeason = mRobot.getSeason();
            edtName.setText(mRobot.getName());
            spnStyle.setSelection(mRobot.getStyle());
            edtHeight.setText(mRobot.getHeight());
            edtWeight.setText(mRobot.getWeight());
            spnDrive.setSelection(mRobot.getDriveTrain());
            spnWheel.setSelection(mRobot.getWheelType());
            spnStrafe.setSelection(mRobot.getStrafe());
            spnMoveTote.setSelection(mRobot.getMoveTote());
            spnMoveCont.setSelection(mRobot.getMoveCont());
            spnAcqTote.setSelection(mRobot.getAcqTote());
            spnAcqCont.setSelection(mRobot.getAcqCont());
            spnStartPos.setSelection(mRobot.getStartPos());
            edtMaxTote.setText(mRobot.getMaxTote());
        }

        TextView txtSeason = (TextView) rootView.findViewById(R.id.txtSeasonInfo);
        txtSeason.setText(mNumber + " - " + SettingsActivity.SEASONS[mSeason]);

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean update = false;

                if (mRobot != null)
                    update = true;
                else
                    mRobot = new RobotInfoItem();

                Log.e("OtpRadar", "fabSave clicked " + String.valueOf(update));

                mRobot.setId(mId);
                mRobot.setSeason(mSeason);
                mRobot.setName(edtName.getText().toString());
                mRobot.setStyle(spnStyle.getSelectedItemPosition());
                mRobot.setHeight(edtHeight.getText().toString());
                mRobot.setWeight(edtWeight.getText().toString());
                mRobot.setDriveTrain(spnDrive.getSelectedItemPosition());
                mRobot.setWheelType(spnWheel.getSelectedItemPosition());
                mRobot.setStrafe(spnStrafe.getSelectedItemPosition());
                mRobot.setMoveTote(spnMoveTote.getSelectedItemPosition());
                mRobot.setMoveCont(spnMoveCont.getSelectedItemPosition());
                mRobot.setAcqTote(spnAcqTote.getSelectedItemPosition());
                mRobot.setAcqCont(spnAcqCont.getSelectedItemPosition());
                mRobot.setStartPos(spnStartPos.getSelectedItemPosition());
                mRobot.setMaxTote(edtMaxTote.getText().toString());

                mListener.saveRobotInfoValues(mRobot, update);

            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            Activity activity = (Activity) context;
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setRobotInfoItem(RobotInfoItem robot) {
        mRobot = robot;
    }

    public interface OnFragmentInteractionListener {
        void saveRobotInfoValues(RobotInfoItem robot, boolean update);
    }

}
