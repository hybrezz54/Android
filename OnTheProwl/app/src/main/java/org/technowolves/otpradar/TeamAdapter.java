package org.technowolves.otpradar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.technowolves.otpradar.model.Team;

import java.util.ArrayList;

public class TeamAdapter extends ArrayAdapter<Team> {

    private int selectedIndex;
    private int selectedColor = Color.parseColor("#1b1b1b");

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

        /*if (selectedIndex != -1 && position == selectedIndex)
            parent.setBackgroundColor(selectedColor);
        else
            parent.setBackgroundColor(Color.WHITE);*/

        txtNumber.setText(team.getNumber());
        txtName.setText(team.getName());

        return convertView;
    }

    public void setSelectedIndex(int index) {
        selectedIndex = index;
        notifyDataSetChanged();
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

}
