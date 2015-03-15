package technowolves.org.otpradar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class PrimaryListFragment extends ListFragment {

    private static final String VALUE_KEY = "TEAM_LIST";
    private static final String PREFS_KEY = "technowolves.org.otpradar.PREFERENCE_FILE_KEY";
    private static final String SIZE_KEY = "ARRAY_SIZE";
    private static final String NUMBER_KEY = "TEAM_NUMBER";
    private static final String NAME_KEY = "TEAM_NAME";

    private static int mSection;
    private ArrayList<Team> mValues;
    private FragmentManager mManager;

    private InputFragment mInputFrag = null;
    private RobotFragment mRobotFrag = null;

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
        }

        setLongClick();
        load();
        TeamAdapter adapter = new TeamAdapter(getActivity(), mValues);
        setListAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
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
                        .addToBackStack(null)
                        .replace(R.id.container, TeamFragment.newInstance(position, false))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                break;

            case 3:
                if (mInputFrag == null)
                    mInputFrag = InputFragment.newInstance(position, false);
                else {
                    mInputFrag.updatePosition(position);
                    mInputFrag.updateEditing(false);
                }

                mManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, mInputFrag)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                break;

            case 4:
                if (mRobotFrag == null)
                    mRobotFrag = RobotFragment.newInstance(position, false);
                else {
                    mRobotFrag.updatePosition(position);
                    mRobotFrag.updateEditing(false);
                }

                mManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, mRobotFrag)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
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
                        .addToBackStack(null)
                        .replace(R.id.container, TeamFragment.newInstance(mValues.size(), true))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                break;
            case R.id.action_share:
                switch (mSection) {
                    case 2:
                        warnDialog();
                        break;
                    case 3:
                        mInputFrag.export(getTeamValues());
                        break;
                    case 4:
                        mRobotFrag.export(getTeamValues());
                        break;
                }
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

    private void setLongClick() {

        getListView().setLongClickable(true);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.getMenuInflater().inflate(R.menu.list_item, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        switch (id) {
                            case R.id.edit_item:

                                switch (mSection) {
                                    case 2:
                                        getFragmentManager().beginTransaction()
                                                .addToBackStack(null)
                                                .replace(R.id.container, TeamFragment.newInstance(position, true))
                                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                                .commit();
                                        break;
                                    case 3:
                                        if (mInputFrag == null)
                                            mInputFrag = InputFragment.newInstance(position, true);
                                        else {
                                            mInputFrag.updatePosition(position);
                                            mInputFrag.updateEditing(true);
                                        }
                                        getFragmentManager().beginTransaction()
                                                .addToBackStack(null)
                                                .replace(R.id.container, mInputFrag)
                                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                                .commit();
                                        break;
                                    case 4:
                                        if (mRobotFrag == null)
                                            mRobotFrag = RobotFragment.newInstance(position, true);
                                        else {
                                            mRobotFrag.updatePosition(position);
                                            mRobotFrag.updateEditing(true);
                                        }
                                        getFragmentManager().beginTransaction()
                                                .addToBackStack(null)
                                                .replace(R.id.container, mRobotFrag)
                                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                                .commit();
                                        break;
                                }
                                break;

                            case R.id.remove_all:
                                removeAll();
                                break;
                            case R.id.remove_item:
                                remove(position);
                                mInputFrag.remove(position);
                                mRobotFrag.remove(position);
                                break;
                        }

                        return true;
                    }
                });

                popup.show();
                return true;
            }
        });

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

    private void remove(final int index) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
        builder.setTitle("Delete Team Data");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage("Do you really wish to delete this team's data?\n *WARNING: Doesn't work right!");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mValues.remove(index);

                SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove(NUMBER_KEY + index);
                editor.remove(NAME_KEY + index);
                editor.commit();

                prefs = getActivity().getSharedPreferences(PREFS_KEY + index, Context.MODE_PRIVATE);
                editor = prefs.edit();
                editor.clear();
                editor.commit();

                ((TeamAdapter)getListAdapter()).notifyDataSetChanged();
                save();
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

    private void removeAll() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
        builder.setTitle("Delete Team Data");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage("Do you really wish to delete all team data?!");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences prefs;
                SharedPreferences.Editor editor;

                for (int i = 0; i < mValues.size(); i++) {
                    prefs = getActivity().getSharedPreferences(PREFS_KEY + i, Context.MODE_PRIVATE);
                    editor = prefs.edit();
                    editor.clear();
                    editor.commit();
                    mInputFrag.remove(i);
                    mRobotFrag.remove(i);
                }

                prefs = getActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
                editor = prefs.edit();
                editor.clear();
                editor.commit();

                mValues.clear();
                ((TeamAdapter)getListAdapter()).notifyDataSetChanged();
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

    private void warnDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
        builder.setTitle("Share via Bluetooth");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage("Do you really wish to send all team data via bluetooth?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CsvWriter writer = new CsvWriter(getActivity(), "team_data", TeamFragment.HEADER,
                        getStringsFromFields(TeamFragment.HEADER.length));
                writer.writeFile();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/*");
                intent.setPackage("com.android.bluetooth");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(writer.getFile()));
                startActivity(intent);
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

    private String[] getStringsFromFields(final int fields) {
        String[] strings = new String[fields*mValues.size()];
        int counter = 0;

        for (int i = 0; i < mValues.size(); i++) {
            SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_KEY + i, Context.MODE_PRIVATE);
            strings[counter] = prefs.getString(TeamFragment.NUMBER_KEY, "");
            counter++;
            strings[counter] = prefs.getString(TeamFragment.NAME_KEY, "");
            counter++;
            strings[counter] = prefs.getString(TeamFragment.SITE_KEY, "");
            counter++;
            strings[counter] = prefs.getString(TeamFragment.LOCATION_KEY, "");
            counter++;
            strings[counter] = prefs.getString(TeamFragment.TOTAL_KEY, "");
            counter++;
            strings[counter] = processSimple(prefs.getInt(TeamFragment.OTHER_KEY, 0));
            counter++;
            strings[counter] = processAward(prefs.getInt(TeamFragment.AWARD1_KEY, 0));
            counter++;
            strings[counter] = processYear(prefs.getInt(TeamFragment.YEAR1_KEY, 0));
            counter++;
            strings[counter] = processAward(prefs.getInt(TeamFragment.AWARD2_KEY, 0));
            counter++;
            strings[counter] = processYear(prefs.getInt(TeamFragment.YEAR2_KEY, 0));
            counter++;
            strings[counter] = processAward(prefs.getInt(TeamFragment.AWARD3_KEY, 0));
            counter++;
            strings[counter] = processYear(prefs.getInt(TeamFragment.YEAR3_KEY, 0));
            counter++;
            strings[counter] = processNotes(prefs.getString(TeamFragment.NOTES_KEY, ""));
            counter++;
        }

        return strings;
    }

    private String[] getTeamValues() {
        String[] values = new String[mValues.size()*2];
        int counter = 0;

        for(int i = 0; i < mValues.size(); i++) {
            values[counter] = mValues.get(i).number;
            counter++;
            values[counter] = mValues.get(i).team;
            counter++;
        }

        return values;
    }

    private String processSimple(int index) {
        return TeamFragment.SIMPLE[index];
    }

    private String processAward(int index) {
        return TeamFragment.AWARDS[index];
    }

    private String processYear(int index) {
        return TeamFragment.YEARS[index];
    }

    private String processNotes(String text) {
        return text.replace("\n", "   ");
    }

}
