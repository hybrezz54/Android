package org.technowolves.otpradar.activity;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import org.technowolves.otpradar.R;
import org.technowolves.otpradar.fragment.intro.MainFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener{

    private static final String DRAWER_POS_KEY = "DRAWER_POS_KEY";

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavView;
    private int mPosition;

    private FragmentManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        Intent intent = new Intent(this, InitialActivity.class);
        startActivity(intent);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        }

        /*mActionBar = getSupportActionBar();
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        mActionBar.setDisplayHomeAsUpEnabled(true);*/

        mNavView = (NavigationView) findViewById(R.id.navigation_view);

        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt(DRAWER_POS_KEY, 0);
        } else {
            mPosition = 0;
            initMainFragment(1);
        }

        if(mNavView != null)
            setUpNavigationDrawerContent();

    }

    private void setUpNavigationDrawerContent() {
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        menuItem.setChecked(true);
                        mPosition = 0;
                        mToolbar.setTitle(getString(R.string.drawer_item_one));
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        initMainFragment(1);
                        return true;
                    case R.id.navigation_item_2:
                        menuItem.setChecked(true);
                        mPosition = 1;
                        mToolbar.setTitle(getString(R.string.drawer_item_two));
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.navigation_item_3:
                        menuItem.setChecked(true);
                        mPosition = 2;
                        mToolbar.setTitle(R.string.drawer_item_three);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
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
    public void onFragmentInteraction() {

    }

    private void initMainFragment(int position) {
        mManager = getSupportFragmentManager();
        mManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(position))
                .commit();
    }

}
