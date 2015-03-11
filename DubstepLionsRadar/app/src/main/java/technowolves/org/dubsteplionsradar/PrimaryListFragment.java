package technowolves.org.dubsteplionsradar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.ListView;

import java.util.ArrayList;

public class PrimaryListFragment extends ListFragment {

    private static final String VALUE_KEY = "TEAM_LIST";
    private static final String PREFS_KEY = "technowolves.org.dubsteplionsradar.PREFERENCE_FILE_KEY";
    private static final String SIZE_KEY = "ARRAY_SIZE";
    private static final String NUMBER_KEY = "TEAM_NUMBER";
    private static final String NAME_KEY = "TEAM_NAME";

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
            //mValues.add(new Team("5518", "Techno Wolves"));
        }

        load();
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
        //mManager.saveFragmentInstanceState(this);
        save();
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

        switch (id) {
            case R.id.action_add:
                mManager.beginTransaction()
                        .addToBackStack("PrimaryListFragment")
                        .replace(R.id.container, TeamFragment.newInstance(mValues.size(), true))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                break;
            case R.id.action_share:
                warnDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void add(Team team) {
        mValues.add(team);
        ((TeamAdapter)getListAdapter()).notifyDataSetChanged();
        //mManager.saveFragmentInstanceState(this);
        save();
    }

    private void load() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        int size = prefs.getInt(SIZE_KEY, 0);

        for (int i = 0; i < size; i++) {
            mValues.add(new Team(prefs.getString(NUMBER_KEY + i, ""),
                    prefs.getString(NAME_KEY + i, "")));
        }
    }

    private void save() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(SIZE_KEY, mValues.size());
        for (int i = 0; i < mValues.size(); i++) {
            editor.putString(NUMBER_KEY + i, mValues.get(i).number);
            editor.putString(NAME_KEY + i, mValues.get(i).team);
        }
        editor.commit();
    }

    private void warnDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Share via Bluetooth");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage("Do you wish to turn on bluetooth to send files?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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

}
