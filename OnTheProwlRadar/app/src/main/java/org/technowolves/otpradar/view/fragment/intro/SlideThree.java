package org.technowolves.otpradar.view.fragment.intro;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.technowolves.otpradar.R;
import org.technowolves.otpradar.view.activity.InitialActivity;
import org.technowolves.otpradar.view.activity.SettingsActivity;

public class SlideThree extends Fragment {

    //private OnFragmentReturn mListener;
    private EditText mNumber;
    private EditText mName;
    private Spinner mSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.slide_three, container, false);

        mNumber = (EditText) rootView.findViewById(R.id.tmNumber);
        mName = (EditText) rootView.findViewById(R.id.tmName);
        mSpinner = (Spinner) rootView.findViewById(R.id.spn_season);

        ArrayAdapter<String> season = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
                SettingsActivity.SEASONS);
        mSpinner.setAdapter(season);

        return rootView;
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            Activity activity = (Activity) context;
            mListener = (OnFragmentReturn) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement onFragmentReturn");
        }
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*mListener.storeSeasonPref(mNumber.getText().toString(), mName.getText().toString(),
                mSpinner.getSelectedItemPosition());*/
    }

    /*public interface OnFragmentReturn {
        void storeSeasonPref(String number, String name, int spnIndex);
    }*/

}
