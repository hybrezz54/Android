package technowolves.org.dubsteplionsradar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PrimaryListFragment extends ListFragment {

    private static final String VALUE_KEY = "TEAM_LIST";

    private static int mSection;
    private ArrayList<Team> mValues;

    private FragmentManager mManager;

    public static PrimaryListFragment newInstance(int sectionNumber) {
        PrimaryListFragment fragment = new PrimaryListFragment();
        mSection = sectionNumber;
        /*Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null)
            mValues = savedInstanceState.getParcelableArrayList(VALUE_KEY);
        else {
            mValues = new ArrayList<Team>();
            mValues.add(new Team("5518", "Techno Wolves"));
        }

        TeamAdapter adapter = new TeamAdapter(getActivity(), mValues);
        setListAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(VALUE_KEY, mValues);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        mManager = getFragmentManager();
        switch (mSection) {
            case 2:
                mManager.beginTransaction()
                        .addToBackStack("")
                        .replace(R.id.container, TeamFragment.newInstance(false))
                        .commit();
                break;
            case 3:
                mManager.beginTransaction()
                        .addToBackStack("")
                        .replace(R.id.container, TeamFragment.newInstance(false))
                        .commit();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if(mSection == 2) {
            getActivity().getMenuInflater().inflate(R.menu.main, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            mManager.beginTransaction()
                    .addToBackStack("")
                    .replace(R.id.container, TeamFragment.newInstance(true))
                    .commit();
        }

        return super.onOptionsItemSelected(item);
    }
}
