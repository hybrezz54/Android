package org.technowolves.otpradar.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.technowolves.otpradar.R;

import java.util.ArrayList;

public class TeamListAdapter extends ArrayAdapter<TeamListItem> {

    public TeamListAdapter(Context context, ArrayList<TeamListItem> teams) {
        super(context, 0, teams);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TeamListItem team = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_team, parent, false);

        TextView txtNumber = (TextView) convertView.findViewById(R.id.txtTeamNumber);
        TextView txtName = (TextView) convertView.findViewById(R.id.txtTeamName);
        TextView txtSite = (TextView) convertView.findViewById(R.id.txtWebsite);

        txtNumber.setText(team.getNumber());
        txtName.setText(team.getName());
        txtSite.setText(team.getWebsite());

        return convertView;

    }
}
