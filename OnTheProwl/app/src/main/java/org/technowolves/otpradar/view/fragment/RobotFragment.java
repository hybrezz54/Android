package org.technowolves.otpradar.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.technowolves.otpradar.ActivityInteractionListener;
import org.technowolves.otpradar.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RobotFragInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RobotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RobotFragment extends Fragment implements ActivityInteractionListener {

    private RobotFragInteractionListener mListener;

    public RobotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RobotFragment.
     */
    public static RobotFragment newInstance() {
        RobotFragment fragment = new RobotFragment();
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
        return inflater.inflate(R.layout.fragment_robot, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RobotFragInteractionListener) {
            mListener = (RobotFragInteractionListener) context;
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
    public interface RobotFragInteractionListener {
    }
}
