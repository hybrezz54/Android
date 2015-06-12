package org.technowolves.otpradar.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
    //private ArrayList<TeamListItem> mValues;

    private Toolbar mToolbar;

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

        mToolbar = new Toolbar(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        DatabaseHandler handler = new DatabaseHandler(getActivity());
        handler.addTeamItem(new DatabaseTeamItem("5518", "Techno Wolves", "http://technowolves.org"));
        TeamCursorAdapter adapter = new TeamCursorAdapter(getActivity(), handler.getCursor());
        setListAdapter(adapter);

        final Toolbar editToolbar = (Toolbar) rootView.findViewById(R.id.editToolbar);

        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.action_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (editToolbar.getVisibility() == View.GONE) {
                /*editToolbar.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                        R.anim.popin_bottom));
                editToolbar.setVisibility(View.VISIBLE);*/
                revealToolbar(editToolbar, fab);
            } else {
                editToolbar.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                        R.anim.popout_bottom));
                editToolbar.setVisibility(View.GONE);
            }
            }
        });

        return rootView;

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

    private SupportAnimator revealToolbar(Toolbar toolbar, View view) {
        // previously invisible view

        // get the center for the clipping circle
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(toolbar.getWidth(), toolbar.getHeight());

        // create the animator for this view (the start radius is zero)
        SupportAnimator anim = ViewAnimationUtils.createCircularReveal(toolbar, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        toolbar.setVisibility(View.VISIBLE);
        anim.start();

        return anim;
    }

    private void hideToolbar(SupportAnimator anim, Toolbar toolbar) {
//        // previously invisible view
//        final Toolbar editToolbar = (Toolbar) view.findViewById(R.id.editToolbar);
//
//        // get the center for the clipping circle
//        int cx = (editToolbar.getLeft() + editToolbar.getRight()) / 2;
//        int cy = (editToolbar.getTop() + editToolbar.getBottom()) / 2;
//
//        // get the final radius for the clipping circle
//        int initialRadius = editToolbar.getWidth();
//
//        // create the animator for this view (the start radius is zero)
//        SupportAnimator anim = ViewAnimationUtils.createCircularReveal(editToolbar, cx, cy, initialRadius, 0);
//
//        // make the view invisible when the animation is done
//        anim.addListener(new SupportAnimator.AnimatorListener() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                editToolbar.setVisibility(View.INVISIBLE);
//            }
//        });

        // reverse animation
        anim = anim.reverse();

        toolbar.setVisibility(View.INVISIBLE);

        // start the animation
        anim.start();
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
