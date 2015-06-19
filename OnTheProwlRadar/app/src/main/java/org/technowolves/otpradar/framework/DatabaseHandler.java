package org.technowolves.otpradar.framework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "otpradar";
    private static final int DATABASE_VERSION = 1;

    public static final String COLUMN_ID = "_id";

    private static final String TEAM_LIST_TABLE = "team_list";
    public static final String COLUMN_NUMBER = "team_number";
    public static final String COLUMN_NAME = "team_name";
    public static final String COLUMN_SITE = "team_site";

    private static final String TEAM_INFO_TABLE = "team_info";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_TOTALYRS = "total_yrs";
    private static final String COLUMN_PARTICIPATE = "participate";
    private static final String COLUMN_AWARD1 = "award_one";
    private static final String COLUMN_YEAR1 = "year_one";
    private static final String COLUMN_AWARD2 = "award_two";
    private static final String COLUMN_YEAR2 = "year_two";
    private static final String COLUMN_AWARD3 = "award_three";
    private static final String COLUMN_YEAR3 = "year_three";
    private static final String COLUMN_NOTES = "notes";
    private static final String COLUMN_COACHMENTOR = "coach_mentor";
    private static final String COLUMN_DRIVER = "driver_rate";
    private static final String COLUMN_HP = "hp_rate";

    private static final String CREATE_LIST_TABLE = "CREATE TABLE IF NOT EXISTS " + TEAM_LIST_TABLE +
            "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_NUMBER + " TEXT NOT NULL UNIQUE, "
            + COLUMN_NAME + " TEXT NOT NULL, " + COLUMN_SITE + " TEXT NOT NULL)";

    private static final String CREATE_INFO_TABLE = "CREATE TABLE IF NOT EXISTS " + TEAM_INFO_TABLE +
            "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_LOCATION + " TEXT NOT NULL, "
            + COLUMN_TOTALYRS + " TEXT NOT NULL, " + COLUMN_PARTICIPATE + " INTEGER NOT NULL, " +
            COLUMN_AWARD1 + " INTEGER NOT NULL, " + COLUMN_YEAR1 + " INTEGER NOT NULL, " +
            COLUMN_AWARD2 + " INTEGER NOT NULL, " + COLUMN_YEAR2 + " INTEGER NOT NULL, " +
            COLUMN_AWARD3 + " INTEGER NOT NULL, " + COLUMN_YEAR3 + " INTEGER NOT NULL, " +
            COLUMN_NOTES + " TEXT, " + COLUMN_COACHMENTOR + " INTEGER NOT NULL, " + COLUMN_DRIVER +
            " REAL NOT NULL, " + COLUMN_HP + " REAL NOT NULL)";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LIST_TABLE);
        db.execSQL(CREATE_INFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TEAM_LIST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TEAM_INFO_TABLE);
        onCreate(db);
    }

    public void addTeamItem(TeamListItem team) {
        // Open db connection
        SQLiteDatabase db = this.getWritableDatabase();

        // Store values of team item
        ContentValues values = new ContentValues();
        values.put(COLUMN_NUMBER, team.getNumber());
        values.put(COLUMN_NAME, team.getName());
        values.put(COLUMN_SITE, team.getWebsite());

        // Insert row into table
        db.insert(TEAM_LIST_TABLE, null, values);
        db.close(); // Close db connection
    }

    public void addTeamItem(TeamInfoItem team) {
        // Open db connection
        SQLiteDatabase db = this.getWritableDatabase();

        // Store values of team item
        ContentValues values = new ContentValues();

        // Insert row into table
        db.insert(TEAM_INFO_TABLE, null, values);
        db.close(); // Close db connection
    }

    public int updateTeamItem(TeamListItem team) {
        // Open db connection
        SQLiteDatabase db = this.getWritableDatabase();

        // Store values of team item
        ContentValues values = new ContentValues();
        values.put(COLUMN_NUMBER, team.getNumber());
        values.put(COLUMN_NAME, team.getName());
        values.put(COLUMN_SITE, team.getWebsite());

        // Updating row
        int result = db.update(TEAM_LIST_TABLE, values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(team.getId()) });
        db.close(); // Close db connection
        return result; // return the number of rows affected
    }

    public void deleteTeamItem(TeamListItem team) {
        // Open db connection
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete the record with the specified id
        db.delete(TEAM_LIST_TABLE, COLUMN_ID + " = ?",
                new String[] { String.valueOf(team.getId()) });
        db.delete(TEAM_INFO_TABLE, COLUMN_ID + " = ?",
                new String[] { String.valueOf(team.getId()) });
        db.close(); // Close db connection
    }

    public void deleteAllTeams() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TEAM_LIST_TABLE, null, null);
        db.delete(TEAM_INFO_TABLE, null, null);
    }

    public Cursor getCursor() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TEAM_LIST_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public TeamListItem getTeamListItem(int id) {
        // Open db
        SQLiteDatabase db = this.getReadableDatabase();

        // Construct and execute query
        Cursor cursor = db.query(TEAM_LIST_TABLE,
                new String[] { COLUMN_ID, COLUMN_NUMBER, COLUMN_NAME, COLUMN_SITE },
                COLUMN_ID + " = ?", new String[] { String.valueOf(id + 1) }, null, null,
                COLUMN_ID + " ASC", null);

        if (cursor != null && cursor.moveToFirst()) {
            // Load item values into model
            TeamListItem team = new TeamListItem(cursor.getString(1),
                    cursor.getString(2), cursor.getString(3));
            team.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            cursor.close(); // Close cursor
            return team;
        }

        Log.e("OTPRadar", "Cursor null or moveToFirst is false");

        return null;
    }

    public TeamInfoItem getTeamInfoItem(int id) {
        // Open db
        SQLiteDatabase db = this.getReadableDatabase();

        // Construct and execute query
        Cursor cursor = db.query(TEAM_INFO_TABLE,
                new String[] { COLUMN_ID }, /** add columns here **/
                COLUMN_ID + " = ?", new String[] { String.valueOf(id) },
                null, null, "_id ASC", "100");

        if (cursor != null)
            cursor.moveToFirst();

        // Load item values into model
        TeamInfoItem team = new TeamInfoItem();
        team.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));

        cursor.close(); // Close cursor

        return team;
    }

    public List<TeamListItem> getAllTeamItems() {
        List<TeamListItem> teams = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TEAM_LIST_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TeamListItem item = new TeamListItem(cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));
                item.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                teams.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close(); // Close the cursor

        return teams;
    }

    public int getTeamCount() {
        String selectQuery = "SELECT  * FROM " + TEAM_LIST_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close(); // Close the cursor
        return count;
    }

    public boolean checkTeamNumber(String number) {
        return false;
    }

}
