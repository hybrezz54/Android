package technowolves.org.otpradar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SecondaryListFragment extends ListFragment {

    private static final String VALUE_KEY = "MATCH_LIST";

    private static int mPosition;

    private ArrayList<Team> mValues;

    public static SecondaryListFragment newInstance(int position) {
        SecondaryListFragment fragment = new SecondaryListFragment();
        mPosition = position;
        /*Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null)
            mValues = savedInstanceState.getParcelableArrayList(VALUE_KEY);
        else
            mValues = new ArrayList<Team>();

        TeamAdapter adapter = new TeamAdapter(getActivity(), mValues);
        setListAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    public ArrayAdapter<Team> getAdapter() {
        return (ArrayAdapter<Team>)getListAdapter();
    }

}
