package org.technowolves.otpradar.view.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ListView;

import org.technowolves.otpradar.R;
import org.technowolves.otpradar.presenter.TeamCursorAdapter;
import org.technowolves.otpradar.presenter.TeamListItem;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends ListFragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_TITLE = "section_title";

    private static String mTitle;
    private boolean isToolbarShown;
    private boolean isHiddenByTouch;

    private Toolbar mToolbar;
    private FloatingActionButton mFab;

    private OnFragmentInteractionListener mListener;

    private TeamCursorAdapter mAdapter;

    private Activity mActivity;

    public MainFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainFragment newInstance(String sectionTitle) {
        MainFragment fragment = new MainFragment();

        Bundle args = new Bundle();
        args.putString(ARG_SECTION_TITLE, sectionTitle);
        fragment.setArguments(args);

        return fragment;
    }

    /*public void setDatabaseHandler(DbHelper handler) {
        mDatabaseHandler = handler;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_SECTION_TITLE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mAdapter = new TeamCursorAdapter(mActivity, mListener.getCursorFromHandler());
        setListAdapter(mAdapter);

        setupToolbar(rootView);

        mFab = (FloatingActionButton) rootView.findViewById(R.id.action_edit);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mToolbar.getVisibility() == View.GONE) {
                    revealToolbar();
                }
            }
        });

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();

        getListView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isToolbarShown) {
                    isHiddenByTouch = true;
                    hideToolbar();
                }
                return false;
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            mActivity = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mListener.onListItemClick(position, isToolbarShown, isHiddenByTouch);
        isHiddenByTouch = false;
    }

    private void setupToolbar(View view) {

        mToolbar = (Toolbar) view.findViewById(R.id.editToolbar);
        mToolbar.setContentInsetsAbsolute(0, 0);
        mToolbar.inflateMenu(R.menu.edit);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        deleteAllTeams();
                        return true;
                    case R.id.action_add:
                        addTeam();
                        return true;
                }
                return false;
            }
        });

    }

    private void revealToolbar() {
        isToolbarShown = true;

        mFab.startAnimation(AnimationUtils.loadAnimation(mActivity,
                R.anim.popout_bottom));
        mFab.setVisibility(View.INVISIBLE);

        // get the center for the clipping circle
        int cx = (mFab.getLeft() + mFab.getRight()) / 2;
        int cy = (mFab.getTop() + mFab.getBottom()) / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(mToolbar.getWidth(), mToolbar.getHeight());

        // create the animator for this view (the start radius is zero)
        SupportAnimator anim = ViewAnimationUtils.createCircularReveal(mToolbar, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        mToolbar.setVisibility(View.VISIBLE);
        anim.start();

    }

    private void hideToolbar() {
        // get the center for the clipping circle
        int cx = (mFab.getLeft() + mFab.getRight()) / 2;
        int cy = (mFab.getTop() + mFab.getBottom()) / 2;

        // get the initial radius for the clipping circle
        int initialRadius = mFab.getWidth();

        // create the animator for this view (the final radius is zero)
        SupportAnimator anim = ViewAnimationUtils.createCircularReveal(mToolbar, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
                mToolbar.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat() {
            }

            @Override
            public void onAnimationCancel() {
            }
        });

        isToolbarShown = false;

        // start the animation
        anim.start();

        mFab.startAnimation(AnimationUtils.loadAnimation(mActivity,
                R.anim.popin_bottom));
        mFab.setVisibility(View.VISIBLE);

    }

    private void addTeam() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(R.layout.add_team_dlg);
        builder.setTitle(R.string.edit_add);
        builder.setMessage("Please enter the team information.");

        builder.setPositiveButton("OK", null);

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                hideToolbar();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText numberInput = (EditText) dialog.findViewById(R.id.numberInput);
        final EditText nameInput = (EditText) dialog.findViewById(R.id.nameInput);
        final EditText siteInput = (EditText) dialog.findViewById(R.id.siteInput);

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberInput.getText().toString().equals("")) {
                    Snackbar.make(v.getRootView(), "Please enter the team number.", Snackbar.LENGTH_LONG)
                            .show();
                } else if (nameInput.getText().toString().equals("")) {
                    Snackbar.make(v.getRootView(), "Please enter the team name.", Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    TeamListItem team = new TeamListItem(numberInput.getText().toString(),
                            nameInput.getText().toString(), siteInput.getText().toString());
                    if (mListener.checkTeamNumberExists(team.getNumber())) {
                        new AlertDialog.Builder(mActivity)
                                .setTitle("Warning!")
                                .setIcon(android.R.drawable.stat_sys_warning)
                                .setMessage("This team's info is already entered.")
                                .show();
                    } else {
                        mListener.addTeamListItem(team);
                        mAdapter.changeCursor(mListener.getCursorFromHandler());
                        mAdapter.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                }
            }
        });

    }

    private void deleteAllTeams() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(R.string.edit_delete_all);
        builder.setMessage("Are you sure you would like to delete all team items?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.deleteAllDatabaseItems();
                mAdapter.changeCursor(mListener.getCursorFromHandler());
                mAdapter.notifyDataSetInvalidated();
                hideToolbar();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                hideToolbar();
            }
        });

        builder.show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onListItemClick(int position, boolean toolbarShown, boolean hiddenByTouch);

        Cursor getCursorFromHandler();

        void deleteAllDatabaseItems();

        void addTeamListItem(TeamListItem team);

        boolean checkTeamNumberExists(String number);
    }

}
