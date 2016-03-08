package org.technowolves.otpradar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.technowolves.otpradar.model.Team;

import java.util.ArrayList;

public class TeamAdapter extends ArrayAdapter<Team> {

    public TeamAdapter(Context context, ArrayList<Team> teams) {
        super(context, 0, teams);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Team team = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.team_item, parent, false);

        TextView txtNumber = (TextView) convertView.findViewById(R.id.number);
        TextView txtName = (TextView) convertView.findViewById(R.id.name);

        txtNumber.setText(team.getNumber());
        txtName.setText(team.getName());

        return convertView;
    }
}
