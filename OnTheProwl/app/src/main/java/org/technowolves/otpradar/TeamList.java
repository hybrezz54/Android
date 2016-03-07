package org.technowolves.otpradar;

import java.util.ArrayList;
import java.util.List;

public class TeamList {

    private List<Team> mTeams;

    public TeamList() {
        mTeams = new ArrayList<Team>();
    }

    public TeamList(List<Team> teams) {
        mTeams = teams;
    }

    public List<Team> getTeams() {
        return mTeams;
    }

    public void setTeams(List<Team> teams) {
        mTeams = teams;
    }

}
