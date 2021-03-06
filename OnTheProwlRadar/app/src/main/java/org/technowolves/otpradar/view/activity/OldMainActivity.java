package org.technowolves.otpradar.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import org.technowolves.otpradar.R;
import org.technowolves.otpradar.presenter.RobotInfoItem;
import org.technowolves.otpradar.view.fragment.OldMainFragment;
import org.technowolves.otpradar.view.fragment.RobotFragment;
import org.technowolves.otpradar.view.fragment.TeamInfoFragment;
import org.technowolves.otpradar.model.DbHelper;
import org.technowolves.otpradar.presenter.TeamInfoItem;
import org.technowolves.otpradar.presenter.TeamListItem;

public class OldMainActivity extends AppCompatActivity implements OldMainFragment.OnFragmentInteractionListener,
        TeamInfoFragment.OnFragmentInteractionListener, RobotFragment.OnFragmentInteractionListener{

    //public static final String PREFS_KEY = "org.technowolves.otpradar.MAIN_PREFS_KEY";
    public static final String PREFS_OPEN_APP = "APP_OPEN_TIME";
    private static final String DRAWER_POS_KEY = "DRAWER_POS_KEY";

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private NavigationView mNavView;
    private FragmentManager mFragManager;
    private DbHelper mDbHelper;

    private int mPosition;
    public int mSeason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_old);

        mFragManager = getSupportFragmentManager();
        mDbHelper = new DbHelper(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavView = (NavigationView) findViewById(R.id.navigation_view);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState != null)
            mPosition = savedInstanceState.getInt(DRAWER_POS_KEY, 0);
        else
            initMainFragment(0, getString(R.string.drawer_item_one));

        if(mNavView != null)
            setUpNavigationDrawerContent();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //SharedPreferences prefs = this.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSeason = Integer.parseInt(prefs.getString(SettingsActivity.PREF_SEASON, "0"));

        if (!prefs.getBoolean(PREFS_OPEN_APP, false)) {
            Intent intent = new Intent(this, InitialActivity.class);
            startActivity(intent);
            prefs.edit()
                    .putBoolean(PREFS_OPEN_APP, true)
                    .apply();
        }
    }

    private void setUpNavigationDrawerContent() {

        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        menuItem.setChecked(true);
                        initMainFragment(0, getString(R.string.drawer_item_one));
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.navigation_item_2:
                        menuItem.setChecked(true);
                        initMainFragment(1, getString(R.string.drawer_item_two));
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.navigation_item_3:
                        menuItem.setChecked(true);
                        mPosition = 2;
                        mToolbar.setTitle(R.string.drawer_item_three);
                        startActivity(new Intent(OldMainActivity.this, SettingsActivity.class));
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.navigation_item_4:
                        menuItem.setChecked(true);
                        mPosition = 3;
                        Intent i = new Intent(OldMainActivity.this,
                                InitialActivity.class);
                        startActivity(i);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                mToolbar.setTitle(R.string.app_name);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPosition = savedInstanceState.getInt(DRAWER_POS_KEY, 0);
        Menu menu = mNavView.getMenu();
        menu.getItem(mPosition).setChecked(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(DRAWER_POS_KEY, mPosition);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onListItemClick(int position, boolean toolbarShown, boolean hiddenByTouch) {

        TeamListItem teamListItem = mDbHelper.getTeamListItem(position);
        TeamInfoItem teamInfoItem = mDbHelper.getTeamInfoItem(position);
        RobotInfoItem robotItem = mDbHelper.getRobotItem(position, mSeason);

        Fragment fragment = null;
        boolean edit = false;

        if (toolbarShown || hiddenByTouch)
            edit = true;

        switch (mPosition) {
            case 0:
                fragment = TeamInfoFragment.newInstance(teamListItem, teamInfoItem, edit);
                break;
            case 1:
                fragment = RobotFragment.newInstance(robotItem, teamListItem.getNumber(),
                        mSeason, position, edit);
                break;
        }

        mFragManager.beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public Cursor getCursorFromHandler() {
        return mDbHelper.getCursor();
    }

    @Override
    public void deleteAllDatabaseItems() {
        mDbHelper.deleteAllTeams();
    }

    @Override
    public boolean checkTeamNumberExists(String number) {
        return mDbHelper.checkTeamNumber(number);
    }

    @Override
    public void addTeamListItem(TeamListItem team) {
        mDbHelper.addTeamItem(team);
    }

    @Override
    public void saveTeamInfoValues(TeamInfoItem team, boolean update) {

        if (update)
            mDbHelper.updateTeamItem(team);
        else
            mDbHelper.addTeamItem(team);

        mFragManager.popBackStack();

    }

    @Override
    public void saveRobotInfoValues(RobotInfoItem robot, boolean update) {

        if (update)
            mDbHelper.updateRobotItem(robot);
        else
            mDbHelper.addRobotItem(robot);

        mFragManager.popBackStack();

    }

    private void initMainFragment(int pos, String title) {
        mPosition = pos;
        mToolbar.setTitle(title);

        mFragManager.beginTransaction()
                .replace(R.id.container, OldMainFragment.newInstance(title))
                .commit();
    }

    private void sbComingSoon() {
        Snackbar.make(mNavView, "This feature will be released in an update.", Snackbar.LENGTH_LONG)
                .show();
    }

}
