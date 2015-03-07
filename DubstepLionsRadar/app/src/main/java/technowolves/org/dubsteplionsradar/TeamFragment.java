package technowolves.org.dubsteplionsradar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TeamFragment extends Fragment {

    private static boolean isEditing;

    public static TeamFragment newInstance(boolean editing) {
        TeamFragment fragment = new TeamFragment();
        /*Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);*/
        isEditing = editing;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_team_data, container, false);
        if (isEditing)
            disableEditText();

        return rootView;
    }

    private void disableEditText() {

    }

}
