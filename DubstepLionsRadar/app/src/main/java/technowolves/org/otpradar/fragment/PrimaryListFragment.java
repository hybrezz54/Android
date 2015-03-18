package technowolves.org.otpradar.fragment;

import android.app.Activity;
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

import technowolves.org.otpradar.R;
import technowolves.org.otpradar.util.CsvWriter;
import technowolves.org.otpradar.framework.Team;
import technowolves.org.otpradar.framework.TeamAdapter;

public class PrimaryListFragment extends ListFragment {

    private static final String VALUE_KEY = "TEAM_LIST";
    private static final String PREFS_KEY = "technowolves.org.otpradar.PREFERENCE_FILE_KEY";
    private static final String SIZE_KEY = "ARRAY_SIZE";
    private static final String NUMBER_KEY = "TEAM_NUMBER";
    private static final String NAME_KEY = "TEAM_NAME";

    private static int mSection;
    private ArrayList<Team> mValues;
    private FragmentManager mManager;
    private Activity mActivity;

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
        TeamAdapter adapter = new TeamAdapter(mActivity, mValues);
        setListAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
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
                        .replace(R.id.container, TeamFragment.newInstance(position, false, true))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                break;

            case 3:
                mManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, InputFragment.newInstance(position, false))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                break;

            case 4:
                mManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, RobotFragment.newInstance(position, false))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mActivity.getMenuInflater().inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        mManager = getFragmentManager();

        switch (id) {
            case R.id.action_add:
                if (mSection == 2) {
                    mManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.container, TeamFragment.newInstance(mValues.size(), true, false))
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                }
                break;
            case R.id.action_share:
                switch (mSection) {
                    case 2:
                        exportTeamData();
                        break;
                    case 3:
                        InputFragment.newInstance(0, false).export(getTeamValues(), mActivity);
                        break;
                    case 4:
                        RobotFragment.newInstance(0, false).export(getTeamValues(), mActivity);
                        break;
                }
                break;
            case R.id.action_share_all:
                exportAll();
                break;
            case R.id.action_frcscout_export:
                exportFrcScout();
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

                PopupMenu popup = new PopupMenu(mActivity, view);
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
                                                .replace(R.id.container, TeamFragment.newInstance(position, true, true))
                                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                                .commit();
                                        break;
                                    case 3:
                                        getFragmentManager().beginTransaction()
                                                .addToBackStack(null)
                                                .replace(R.id.container, InputFragment.newInstance(position, true))
                                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                                .commit();
                                        break;
                                    case 4:
                                        getFragmentManager().beginTransaction()
                                                .addToBackStack(null)
                                                .replace(R.id.container, RobotFragment.newInstance(position, true))
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
                                InputFragment.newInstance(0, false).remove(position, mActivity);
                                RobotFragment.newInstance(0, false).remove(position, mActivity);
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

    public String getTeamNumber(int index) {
        return mValues.get(index).number;
    }

    private void load() {
        SharedPreferences prefs = mActivity.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        int size = prefs.getInt(SIZE_KEY, 0);

        for (int i = 0; i < size; i++) {
            mValues.add(new Team(prefs.getString(NUMBER_KEY + i, ""),
                    prefs.getString(NAME_KEY + i, "")));
        }
    }

    private void save() {
        SharedPreferences prefs = mActivity.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(SIZE_KEY, mValues.size());
        for (int i = 0; i < mValues.size(); i++) {
            editor.putString(NUMBER_KEY + i, mValues.get(i).number);
            editor.putString(NAME_KEY + i, mValues.get(i).team);
        }
        editor.commit();
    }

    private void remove(final int index) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, AlertDialog.THEME_HOLO_DARK);
        builder.setTitle("Delete Team Data");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage("Do you really wish to delete this team's data?\n *WARNING: Doesn't work right!");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mValues.remove(index);

                SharedPreferences prefs = mActivity.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove(NUMBER_KEY + index);
                editor.remove(NAME_KEY + index);
                editor.commit();

                prefs = mActivity.getSharedPreferences(PREFS_KEY + index, Context.MODE_PRIVATE);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, AlertDialog.THEME_HOLO_DARK);
        builder.setTitle("Delete Team Data");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage("Do you really wish to delete all team data?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences prefs;
                SharedPreferences.Editor editor;
                InputFragment inputFrag = InputFragment.newInstance(0, false);
                RobotFragment robotFrag = RobotFragment.newInstance(0, false);

                for (int i = 0; i < mValues.size(); i++) {
                    prefs = mActivity.getSharedPreferences(PREFS_KEY + i, Context.MODE_PRIVATE);
                    editor = prefs.edit();
                    editor.clear();
                    editor.commit();
                    inputFrag.remove(i, mActivity);
                    robotFrag.remove(i, mActivity);
                }

                prefs = mActivity.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
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

    private void exportTeamData() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, AlertDialog.THEME_HOLO_DARK);
        builder.setTitle("Share via Bluetooth");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage("Do you really wish to send all team data via bluetooth?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CsvWriter writer = new CsvWriter(mActivity, "team_data", TeamFragment.HEADER,
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

    private void exportAll() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, AlertDialog.THEME_HOLO_DARK);
        builder.setTitle("Share All via Bluetooth");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage("Do you really wish to send ALL collected data via bluetooth?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CsvWriter writer = new CsvWriter(mActivity, "team_data", TeamFragment.HEADER,
                        getStringsFromFields(TeamFragment.HEADER.length));
                writer.writeFile();


                ArrayList<Uri> uris = new ArrayList<Uri>();
                uris.add(Uri.fromFile(writer.getFile()));
                uris.add(InputFragment.newInstance(0, false).getFileAfterWrite(getTeamValues(), mActivity));

                RobotFragment robotFrag = RobotFragment.newInstance(0, false);
                uris.add(robotFrag.getFileAfterWrite(getTeamValues(), mActivity));

                for (int i = 0; i < mValues.size(); i++) {
                    uris.add(robotFrag.getImageFile(i));
                }

                Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                intent.setType("text/*");
                intent.setPackage("com.android.bluetooth");
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
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

    private void exportFrcScout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, AlertDialog.THEME_HOLO_DARK);
        builder.setTitle("Share All via Bluetooth");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage("Do you really wish to send ALL collected data via bluetooth for FRCScout DB export?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String[] HEADER = new String[] {"Team Number", "Driver #1", "Driver #2", "Coach", "Is drive coach mentor?", "Robot Height (in)", "Robot Weight (lbs)",
                        "Can move totes?", "Can move containers?", "Can acquire containers?", "Preferred starting location", "Tote Stack Capacity", "Human can load totes?",
                        "Human can load litter?", "Human can throw litter?", "Robot has turret?", "Robot has strafing?", "Robot speed (ft/s)", "Robot Strengths",
                        "Robot Weaknesses"};

                CsvWriter writer = new CsvWriter(mActivity, "frcscout_db", HEADER,
                        getFrcScoutExportArray(HEADER.length));
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
            SharedPreferences prefs = mActivity.getSharedPreferences(PREFS_KEY + i, Context.MODE_PRIVATE);
            strings[counter] = prefs.getString(TeamFragment.NUMBER_KEY, "");
            counter++;
            strings[counter] = prefs.getString(TeamFragment.NAME_KEY, "");
            counter++;
            strings[counter] = prefs.getString(TeamFragment.SITE_KEY, "");
            counter++;
            strings[counter] = processLocation(prefs.getString(TeamFragment.LOCATION_KEY, ""));
            counter++;
            strings[counter] = prefs.getString(TeamFragment.TOTAL_KEY, "");
            counter++;
            strings[counter] = processComp(prefs.getInt(TeamFragment.OTHER_KEY, 0));
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
            strings[counter] = prefs.getString(TeamFragment.DRIVER1_KEY, "");
            counter++;
            strings[counter] = prefs.getString(TeamFragment.DRIVER2_KEY, "");
            counter++;
            strings[counter] = prefs.getString(TeamFragment.COACH_KEY, "");
            counter++;
            strings[counter] = processSimple(prefs.getInt(TeamFragment.CM_KEY, 0));
            counter++;
            strings[counter] = String.valueOf(prefs.getFloat(TeamFragment.DRIVER_KEY, 0f));
            counter++;
            strings[counter] = String.valueOf(prefs.getFloat(TeamFragment.HP_KEY, 0f));
            counter++;
            strings[counter] = processSimple(prefs.getInt(TeamFragment.HPTOTE_KEY, 0));
            counter++;
            strings[counter] = processSimple(prefs.getInt(TeamFragment.HPLITTER_KEY, 0));
            counter++;
            strings[counter] = processSimple(prefs.getInt(TeamFragment.HPTHROW_KEY, 0));
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

    private String[] getFrcScoutExportArray(final int fields) {
        String[] values = new String[fields*mValues.size()];
        RobotFragment robotFrag = RobotFragment.newInstance(0, false);
        int counter = 0;

        for (int i = 0; i < mValues.size(); i++) {
            SharedPreferences prefs = mActivity.getSharedPreferences(PREFS_KEY + i, Context.MODE_PRIVATE);
            String[] robot_values = robotFrag.getFrcScoutExportArray(i, mActivity);

            values[counter] = mValues.get(i).number;
            counter++;
            values[counter] = prefs.getString(TeamFragment.DRIVER1_KEY, "");
            counter++;
            values[counter] = prefs.getString(TeamFragment.DRIVER2_KEY, "");
            counter++;
            values[counter] = prefs.getString(TeamFragment.COACH_KEY, "");
            counter++;
            values[counter] = processSimple(prefs.getInt(TeamFragment.CM_KEY, 0));
            counter++;
            for (int j = 0; j < (robot_values.length - 5); j++) {
                values[counter] = robot_values[j];
                counter++;
            }
            values[counter] = processSimple(prefs.getInt(TeamFragment.HPTOTE_KEY, 0));
            counter++;
            values[counter] = processSimple(prefs.getInt(TeamFragment.HPLITTER_KEY, 0));
            counter++;
            values[counter] = processSimple(prefs.getInt(TeamFragment.HPTHROW_KEY, 0));
            counter++;
            for (int j = 5; j > 0; j--) {
                values[counter] = robot_values[robot_values.length - j];
                counter++;
            }
        }

        return values;
    }

    private String processSimple(int index) {
        return TeamFragment.SIMPLE[index];
    }

    private String processComp(int index) {
        return TeamFragment.COMPETITION[index];
    }

    private String processAward(int index) {
        return TeamFragment.AWARDS[index];
    }

    private String processYear(int index) {
        return TeamFragment.YEARS[index];
    }

    private String processNotes(String text) {
        return text.replace("\n", "   ").replace(",", "");
    }

    private String processLocation(String text) {
        return text.replace(",", "");
    }

}
