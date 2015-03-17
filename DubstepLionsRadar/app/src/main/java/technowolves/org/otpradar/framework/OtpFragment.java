package technowolves.org.otpradar.framework;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;

import technowolves.org.otpradar.R;

public class OtpFragment extends Fragment {

    private static final String ARG_POS_NUMBER = "ROBOT_FRAG_POS";
    private static final String ARG_EDITING = "ROBOT_FRAG_STATE";

    protected boolean isEditing;
    protected int mPosition;
    protected Activity mActivity;

    public static OtpFragment newInstance(int position, boolean editing) {
        OtpFragment fragment = new OtpFragment();
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

        mActivity = getActivity();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (isEditing)
            getActivity().getMenuInflater().inflate(R.menu.save, menu);
    }

    public void updatePosition(int position) {
        mPosition = position;
        updatePrefs();
        //loadValues();
    }

    public void updateEditing(boolean editing) {
        this.isEditing = editing;
    }

    protected void updatePrefs() {

    }

    protected String processMultiEdt(String text) {
        return text.replace("\n", "   ").replace(",", "");
    }

}
