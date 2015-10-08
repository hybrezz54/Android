package org.technowolves.otpradar.view.fragment.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.technowolves.otpradar.R;

public class NewSlide extends Fragment {

    private static final String ARG_LAYOUT_RES_ID = "layoutResId";

    public static NewSlide newInstance(int layoutResId) {
        NewSlide slide = new NewSlide();

        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        slide.setArguments(args);

        return slide;
    }

    private int layoutResId;

    public NewSlide() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID))
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layoutResId, container, false);
    }

}
