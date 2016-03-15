package org.technowolves.otpradar;

import android.view.View;

import org.technowolves.otpradar.model.Team;

public interface FragmentInteractionListener {

    void onCreateSnackbar(View view, String msg);
    Team getSelectedTeam();

}
