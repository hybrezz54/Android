package technowolves.org.otpradar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TeamAdapter extends ArrayAdapter<Team> {

    public TeamAdapter(Context context, ArrayList<Team> teams) {
        super(context, 0, teams);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Team team = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_team, parent, false);

        TextView txtNumber = (TextView) convertView.findViewById(R.id.txtNumber);
        TextView txtTeam = (TextView) convertView.findViewById(R.id.txtTeam);

        txtNumber.setText(team.number);
        txtTeam.setText(team.team);

        return convertView;

    }
}
