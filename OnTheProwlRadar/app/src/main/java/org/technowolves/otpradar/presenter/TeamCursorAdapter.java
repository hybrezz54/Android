package org.technowolves.otpradar.presenter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.technowolves.otpradar.R;
import org.technowolves.otpradar.model.DbContract;
import org.technowolves.otpradar.model.DbHelper;

public class TeamCursorAdapter extends CursorAdapter {

    public TeamCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtNumber = (TextView) view.findViewById(R.id.txtTeamNumber);
        TextView txtName = (TextView) view.findViewById(R.id.txtTeamName);
        TextView txtSite = (TextView) view.findViewById(R.id.txtWebsite);

        String number = cursor.getString(cursor.getColumnIndex(DbContract.COLUMN_NUMBER));
        String name = cursor.getString(cursor.getColumnIndex(DbContract.COLUMN_NAME));
        String website = cursor.getString(cursor.getColumnIndex(DbContract.COLUMN_SITE));

        txtNumber.setText(number);
        txtName.setText(name);

        if (txtSite != null)
            txtSite.setText(website);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_team, parent, false);
    }
}
