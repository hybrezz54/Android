package org.technowolves.otpradar.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.technowolves.otpradar.ActivityInteractionListener;
import org.technowolves.otpradar.FragmentInteractionListener;
import org.technowolves.otpradar.R;
import org.technowolves.otpradar.view.fragment.MainFragment;
import org.technowolves.otpradar.view.fragment.RobotFragment;
import org.technowolves.otpradar.view.fragment.TeamFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentInteractionListener,
        MainFragment.MainFragInteractionListener, TeamFragment.TeamFragInteractionListener,
        RobotFragment.RobotFragInteractionListener {

    public static final String PREFS_OPEN_APP = "APP_OPEN_TIME";

    private ActivityInteractionListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean(PREFS_OPEN_APP, false)) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
            prefs.edit().putBoolean(PREFS_OPEN_APP, true).apply();
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);

                Fragment fragment = ((ViewPagerAdapter) viewPager.getAdapter())
                        .getItem(tab.getPosition());
                if (fragment instanceof ActivityInteractionListener)
                    mListener = (ActivityInteractionListener) fragment;
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);

                Fragment fragment = ((ViewPagerAdapter) viewPager.getAdapter())
                        .getItem(tab.getPosition());
                if (fragment instanceof ActivityInteractionListener)
                    mListener = (ActivityInteractionListener) fragment;
            }
        });

        tabLayout.getTabAt(0).select();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onButtonPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_help:
                Intent helpIntent = new Intent(this, IntroActivity.class);
                startActivity(helpIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateSnackbar(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .show();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(MainFragment.newInstance(), getString(R.string.tab_one));
        adapter.addFragment(TeamFragment.newInstance(), getString(R.string.tab_two));
        adapter.addFragment(RobotFragment.newInstance(), getString(R.string.tab_three));
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

    }

}
