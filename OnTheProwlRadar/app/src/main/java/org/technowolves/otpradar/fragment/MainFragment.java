package org.technowolves.otpradar.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;

import org.technowolves.otpradar.R;
import org.technowolves.otpradar.framework.DatabaseHandler;
import org.technowolves.otpradar.framework.DatabaseTeamItem;
import org.technowolves.otpradar.framework.TeamCursorAdapter;

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
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static int mSection;
    private boolean isToolbarShown;

    private Toolbar mToolbar;
    private FloatingActionButton mFab;

    private OnFragmentInteractionListener mListener;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainFragment newInstance(int sectionNumber) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mSection = getArguments().getInt(ARG_SECTION_NUMBER);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        DatabaseHandler handler = new DatabaseHandler(getActivity());
        handler.addTeamItem(new DatabaseTeamItem("5518", "Techno Wolves", "http://technowolves.org"));
        TeamCursorAdapter adapter = new TeamCursorAdapter(getActivity(), handler.getCursor());
        setListAdapter(adapter);

        mToolbar = (Toolbar) rootView.findViewById(R.id.editToolbar);
        mToolbar.inflateMenu(R.menu.edit);

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

        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState != SCROLL_STATE_IDLE && isToolbarShown) {
                    hideToolbar();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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

    private void revealToolbar() {
        isToolbarShown = true;
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

        mFab.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                R.anim.popin_bottom));
        mFab.setVisibility(View.VISIBLE);
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
        public void onFragmentInteraction();
    }

}
