package technowolves.org.dubsteplionsradar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PrimaryListFragment extends ListFragment {

    private static final String VALUE_KEY = "TEAM_LIST";

    private static int mSection;
    private ArrayList<Team> mValues;
    private FragmentManager mManager;
    private TeamAdapter mAdapter;

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

        mAdapter = new TeamAdapter(getActivity(), mValues);
        setListAdapter(mAdapter);
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
                        .replace(R.id.container, TeamFragment.newInstance(position, false))
                        .commit();
                Log.v("PrimaryListFragment", Integer.toString(position));
                break;
            case 3:
                mManager.beginTransaction()
                        .addToBackStack("")
                        .replace(R.id.container, MatchFragment.newInstance(false))
                        .commit();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if(mSection == 2)
            getActivity().getMenuInflater().inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        mManager = getFragmentManager();

        if (id == R.id.action_add) {
            mManager.beginTransaction()
                    .addToBackStack("PrimaryListFragment")
                    .replace(R.id.container, TeamFragment.newInstance(mValues.size(), true))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            Log.v("PrimaryListFragment", Integer.toString(mValues.size()));
        }

        return super.onOptionsItemSelected(item);
    }

    public void add(Team team) {
        //mValues.add(team);
        //mAdapter.add(team);
        ((TeamAdapter) getListAdapter()).add(team);
        Log.v("PrimaryListFragment", Integer.toString(mValues.size()));
        Log.v("PrimaryListFragment", Integer.toString(getListAdapter().getCount()));
        Log.v("PrimaryListFragment", team.number + team.team);
        Log.v("PrimaryListFragment", mValues.get(0).number+mValues.get(1).team);
    }

}
