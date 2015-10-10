package org.technowolves.otpradar.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.technowolves.otpradar.R;
import org.technowolves.otpradar.presenter.RobotInfoItem;

public class RobotFragment extends Fragment {

    private static final String ARG_TEAM_NUMBER = "team_number";
    private static final String ARG_SEASON = "frc_season";
    private static final String ARG_EDIT_MODE = "edit_mode";

    private static String mNumber;
    private static int mSeason;
    private static boolean mEditMode;

    private OnFragmentInteractionListener mListener;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static RobotFragment newInstance(String number, int season, boolean editMode) {
        RobotFragment fragment = new RobotFragment();

        Bundle args = new Bundle();
        args.putString(ARG_TEAM_NUMBER, number);
        args.putInt(ARG_SEASON, season);
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
            mEditMode = getArguments().getBoolean(ARG_EDIT_MODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_robot_info, container, false);
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

    public interface OnFragmentInteractionListener {
        void saveRobotInfoValues(RobotInfoItem robot, boolean update);
    }

}
