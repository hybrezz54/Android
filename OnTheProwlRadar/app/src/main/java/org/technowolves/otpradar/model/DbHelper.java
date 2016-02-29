package org.technowolves.otpradar.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.technowolves.otpradar.presenter.RobotInfoItem;
import org.technowolves.otpradar.presenter.TeamInfoItem;
import org.technowolves.otpradar.presenter.TeamListItem;
import org.technowolves.otpradar.view.activity.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static final String CREATE_LIST_TABLE = "CREATE TABLE IF NOT EXISTS " + DbContract.TEAM_LIST_TABLE +
            "(" + DbContract.COLUMN_ID + " INTEGER PRIMARY KEY, " + DbContract.COLUMN_NUMBER + " TEXT NOT NULL UNIQUE, "
            + DbContract.COLUMN_NAME + " TEXT NOT NULL, " + DbContract.COLUMN_SITE + " TEXT NOT NULL)";

    private static final String CREATE_INFO_TABLE = "CREATE TABLE IF NOT EXISTS " + DbContract.TEAM_INFO_TABLE +
            "(" + DbContract.COLUMN_ID + " INTEGER PRIMARY KEY, " + DbContract.COLUMN_LOCATION + " TEXT NOT NULL, "
            + DbContract.COLUMN_TOTALYRS + " TEXT NOT NULL, " + DbContract.COLUMN_PARTICIPATE + " INTEGER NOT NULL, " +
            DbContract.COLUMN_AWARD1 + " INTEGER NOT NULL, " + DbContract.COLUMN_YEAR1 + " INTEGER NOT NULL, " +
            DbContract.COLUMN_AWARD2 + " INTEGER NOT NULL, " + DbContract.COLUMN_YEAR2 + " INTEGER NOT NULL, " +
            DbContract.COLUMN_AWARD3 + " INTEGER NOT NULL, " + DbContract.COLUMN_YEAR3 + " INTEGER NOT NULL, " +
            DbContract.COLUMN_NOTES + " TEXT, " + DbContract.COLUMN_COACHMENTOR + " INTEGER NOT NULL, " + DbContract.COLUMN_DRIVER +
            " TEXT NOT NULL, " + DbContract.COLUMN_HP + " TEXT NOT NULL)";

    private static final String CREATE_ROBOT_TABLE = "CREATE TABLE IF NOT EXISTS " + DbContract.ROBOT_TABLE +
            "(" + DbContract.COLUMN_ID + " INTEGER NOT NULL, " + DbContract.COLUMN_SEASON + " TEXT NOT NULL, "
            + DbContract.COLUMN_ROBOT + " TEXT NOT NULL, " + DbContract.COLUMN_STYLE + " INTEGER NOT NULL, " +
            DbContract.COLUMN_HEIGHT + " TEXT NOT NULL, " + DbContract.COLUMN_WEIGHT + " TEXT NOT NULL, " +
            DbContract.COLUMN_DRIVE + " INTEGER NOT NULL, " + DbContract.COLUMN_WHEEL + " INTEGER NOT NULL, " +
            DbContract.COLUMN_STRAFE + " INTEGER NOT NULL, " + DbContract.COLUMN_MOVE1 + " INTEGER NOT NULL, " +
            DbContract.COLUMN_MOVE2 + " INTEGER NOT NULL, " + DbContract.COLUMN_ACQ1 + " INTEGER NOT NULL, " +
            DbContract.COLUMN_ACQ2 + " INTEGER NOT NULL, " + DbContract.COLUMN_POS + " INTEGER NOT NULL, " +
            DbContract.COLUMN_TOTE + " TEXT NOT NULL)";

    public DbHelper(Context context) {
        super(context, DbContract.DATABASE_NAME, null, DbContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LIST_TABLE);
        db.execSQL(CREATE_INFO_TABLE);
        db.execSQL(CREATE_ROBOT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.TEAM_LIST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.TEAM_INFO_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.ROBOT_TABLE);
        onCreate(db);
    }

    public void addTeamItem(TeamListItem team) {
        // Open db connection
        SQLiteDatabase db = this.getWritableDatabase();

        // Store values of team item
        ContentValues values = new ContentValues();
        values.put(DbContract.COLUMN_NUMBER, team.getNumber());
        values.put(DbContract.COLUMN_NAME, team.getName());
        values.put(DbContract.COLUMN_SITE, team.getWebsite());

        // Insert row into table
        db.insert(DbContract.TEAM_LIST_TABLE, null, values);
        db.close(); // Close db connection
    }

    public void addTeamItem(TeamInfoItem team) {
        // Open db connection
        SQLiteDatabase db = this.getWritableDatabase();

        // Store values of team item
        ContentValues values = new ContentValues();
        values.put(DbContract.COLUMN_LOCATION, team.getLocation());
        values.put(DbContract.COLUMN_TOTALYRS, team.getTotalYears());
        values.put(DbContract.COLUMN_PARTICIPATE, team.getParticipate());
        values.put(DbContract.COLUMN_AWARD1, team.getAward1());
        values.put(DbContract.COLUMN_YEAR1, team.getYear1());
        values.put(DbContract.COLUMN_AWARD2, team.getAward2());
        values.put(DbContract.COLUMN_YEAR2, team.getYear2());
        values.put(DbContract.COLUMN_AWARD3, team.getAward3());
        values.put(DbContract.COLUMN_YEAR3, team.getYear3());
        values.put(DbContract.COLUMN_NOTES, team.getNotes());
        values.put(DbContract.COLUMN_COACHMENTOR, team.getCoachMentor());
        values.put(DbContract.COLUMN_DRIVER, String.valueOf(team.getDriver()));
        values.put(DbContract.COLUMN_HP, String.valueOf(team.getHp()));

        // Insert row into table
        db.insert(DbContract.TEAM_INFO_TABLE, null, values);
        db.close(); // Close db connection
    }

    public void addRobotItem(RobotInfoItem robot) {
        // Open db connection
        SQLiteDatabase db = this.getWritableDatabase();

        // Store values of team item
        ContentValues values = new ContentValues();
        values.put(DbContract.COLUMN_ID, robot.getId());
        values.put(DbContract.COLUMN_SEASON, robot.getSeason());
        values.put(DbContract.COLUMN_ROBOT, robot.getName());
        values.put(DbContract.COLUMN_STYLE, robot.getStyle());
        values.put(DbContract.COLUMN_HEIGHT, robot.getHeight());
        values.put(DbContract.COLUMN_WEIGHT, robot.getWeight());
        values.put(DbContract.COLUMN_DRIVE, robot.getDriveTrain());
        values.put(DbContract.COLUMN_WHEEL, robot.getWheelType());
        values.put(DbContract.COLUMN_STRAFE, robot.getStrafe());
        values.put(DbContract.COLUMN_MOVE1, robot.getMoveTote());
        values.put(DbContract.COLUMN_MOVE2, robot.getMoveCont());
        values.put(DbContract.COLUMN_ACQ1, robot.getAcqTote());
        values.put(DbContract.COLUMN_ACQ2, robot.getAcqCont());
        values.put(DbContract.COLUMN_POS, robot.getStartPos());
        values.put(DbContract.COLUMN_TOTE, robot.getMaxTote());

        // Insert row into table
        db.insert(DbContract.ROBOT_TABLE, null, values);
        db.close(); // Close db connection
    }

    public int updateTeamItem(TeamListItem team) {
        // Open db connection
        SQLiteDatabase db = this.getWritableDatabase();

        // Store values of team item
        ContentValues values = new ContentValues();
        values.put(DbContract.COLUMN_NUMBER, team.getNumber());
        values.put(DbContract.COLUMN_NAME, team.getName());
        values.put(DbContract.COLUMN_SITE, team.getWebsite());

        // Updating row
        int result = db.update(DbContract.TEAM_LIST_TABLE, values, DbContract.COLUMN_ID + " = ?",
                new String[] { String.valueOf(team.getId()) });
        db.close(); // Close db connection
        return result; // return the number of rows affected
    }

    public int updateTeamItem(TeamInfoItem team) {
        // Open db connection
        SQLiteDatabase db = this.getWritableDatabase();

        // Store values of team item
        ContentValues values = new ContentValues();
        values.put(DbContract.COLUMN_LOCATION, team.getLocation());
        values.put(DbContract.COLUMN_TOTALYRS, team.getTotalYears());
        values.put(DbContract.COLUMN_PARTICIPATE, team.getParticipate());
        values.put(DbContract.COLUMN_AWARD1, team.getAward1());
        values.put(DbContract.COLUMN_YEAR1, team.getYear1());
        values.put(DbContract.COLUMN_AWARD2, team.getAward2());
        values.put(DbContract.COLUMN_YEAR2, team.getYear2());
        values.put(DbContract.COLUMN_AWARD3, team.getAward3());
        values.put(DbContract.COLUMN_YEAR3, team.getYear3());
        values.put(DbContract.COLUMN_NOTES, team.getNotes());
        values.put(DbContract.COLUMN_COACHMENTOR, team.getCoachMentor());
        values.put(DbContract.COLUMN_DRIVER, String.valueOf(team.getDriver()));
        values.put(DbContract.COLUMN_HP, String.valueOf(team.getHp()));

        // Updating row
        int result = db.update(DbContract.TEAM_INFO_TABLE, values, DbContract.COLUMN_ID + " = ?",
                new String[] { String.valueOf(team.getId()) });
        db.close(); // Close db connection
        return result; // return the number of rows affected
    }

    public int updateRobotItem(RobotInfoItem robot) {
        // Open db connection
        SQLiteDatabase db = this.getWritableDatabase();

        // Store values of team item
        ContentValues values = new ContentValues();
        values.put(DbContract.COLUMN_ID, robot.getId());
        values.put(DbContract.COLUMN_SEASON, robot.getSeason());
        values.put(DbContract.COLUMN_ROBOT, robot.getName());
        values.put(DbContract.COLUMN_STYLE, robot.getStyle());
        values.put(DbContract.COLUMN_HEIGHT, robot.getHeight());
        values.put(DbContract.COLUMN_WEIGHT, robot.getWeight());
        values.put(DbContract.COLUMN_DRIVE, robot.getDriveTrain());
        values.put(DbContract.COLUMN_WHEEL, robot.getWheelType());
        values.put(DbContract.COLUMN_STRAFE, robot.getStrafe());
        values.put(DbContract.COLUMN_MOVE1, robot.getMoveTote());
        values.put(DbContract.COLUMN_MOVE2, robot.getMoveCont());
        values.put(DbContract.COLUMN_ACQ1, robot.getAcqTote());
        values.put(DbContract.COLUMN_ACQ2, robot.getAcqCont());
        values.put(DbContract.COLUMN_POS, robot.getStartPos());
        values.put(DbContract.COLUMN_TOTE, robot.getMaxTote());

        // Updating row
        int result = db.update(DbContract.ROBOT_TABLE, values, DbContract.COLUMN_ID + " = ?",
                new String[] { String.valueOf(robot.getId()) });
        db.close(); // Close db connection
        return result; // return the number of rows affected
    }

    public void deleteTeamItem(TeamListItem team) {
        // Open db connection
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete the record with the specified id
        db.delete(DbContract.TEAM_LIST_TABLE, DbContract.COLUMN_ID + " = ?",
                new String[] { String.valueOf(team.getId()) });
        db.delete(DbContract.TEAM_INFO_TABLE, DbContract.COLUMN_ID + " = ?",
                new String[] { String.valueOf(team.getId()) });
        db.close(); // Close db connection
    }

    public void deleteAllTeams() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DbContract.TEAM_LIST_TABLE, null, null);
        db.delete(DbContract.TEAM_INFO_TABLE, null, null);
    }

    public Cursor getCursor() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + DbContract.TEAM_LIST_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public TeamListItem getTeamListItem(int id) {
        // Open db
        SQLiteDatabase db = this.getReadableDatabase();

        // Construct and execute query
        Cursor cursor = db.query(DbContract.TEAM_LIST_TABLE,
                new String[]{DbContract.COLUMN_ID, DbContract.COLUMN_NUMBER, DbContract.COLUMN_NAME, DbContract.COLUMN_SITE},
                        DbContract.COLUMN_ID + " = ?", new String[]{String.valueOf(id + 1)}, null, null,
                        DbContract.COLUMN_ID + " ASC", null);

        if (cursor != null && cursor.moveToFirst()) {
            // Load item values
            TeamListItem team = new TeamListItem(cursor.getString(1),
                    cursor.getString(2), cursor.getString(3));
            team.setId(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_ID)));
            cursor.close(); // Close cursor
            return team;
        }

        return null;
    }

    public TeamInfoItem getTeamInfoItem(int id) {
        // Open db
        SQLiteDatabase db = this.getReadableDatabase();

        // Construct and execute query
        Cursor cursor = db.query(DbContract.TEAM_INFO_TABLE,
                new String[] { DbContract.COLUMN_ID, DbContract.COLUMN_LOCATION, DbContract.COLUMN_TOTALYRS, DbContract.COLUMN_PARTICIPATE,
                        DbContract.COLUMN_AWARD1, DbContract.COLUMN_YEAR1, DbContract.COLUMN_AWARD2, DbContract.COLUMN_YEAR2,
                        DbContract.COLUMN_AWARD3, DbContract.COLUMN_YEAR3, DbContract.COLUMN_NOTES, DbContract.COLUMN_COACHMENTOR,
                        DbContract.COLUMN_DRIVER, DbContract.COLUMN_HP}, /** add columns here **/
                        DbContract.COLUMN_ID + " = ?", new String[] { String.valueOf(id + 1) }, null, null,
                        DbContract.COLUMN_ID + " ASC", null);

        if (cursor != null && cursor.moveToFirst()) {
            // Load item values
            TeamInfoItem team = new TeamInfoItem();
            team.setId(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_ID)));
            team.setLocation(cursor.getString(cursor.getColumnIndex(DbContract.COLUMN_LOCATION)));
            team.setTotalYears(cursor.getString(cursor.getColumnIndex(DbContract.COLUMN_TOTALYRS)));
            team.setParticipate(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_PARTICIPATE)));
            team.setAward1(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_AWARD1)));
            team.setYear1(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_YEAR1)));
            team.setAward2(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_AWARD2)));
            team.setYear2(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_YEAR2)));
            team.setAward3(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_AWARD3)));
            team.setYear3(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_YEAR3)));
            team.setNotes(cursor.getString(cursor.getColumnIndex(DbContract.COLUMN_NOTES)));
            team.setCoachMentor(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_COACHMENTOR)));
            team.setDriver(Float.valueOf(cursor.getString(cursor.getColumnIndex(DbContract.COLUMN_DRIVER))));
            team.setHp(Float.valueOf(cursor.getString(cursor.getColumnIndex(DbContract.COLUMN_HP))));
            cursor.close(); // Close cursor
            return team;
        }

        return null;
    }

    public RobotInfoItem getRobotItem(int pos, int season) {
        // Open db
        SQLiteDatabase db = this.getReadableDatabase();

        // Construct and execute query
        Cursor cursor = db.query(DbContract.ROBOT_TABLE,
                new String[]{DbContract.COLUMN_ID, DbContract.COLUMN_SEASON, DbContract.COLUMN_ROBOT,
                        DbContract.COLUMN_STYLE, DbContract.COLUMN_HEIGHT, DbContract.COLUMN_WEIGHT,
                        DbContract.COLUMN_DRIVE, DbContract.COLUMN_WHEEL, DbContract.COLUMN_STRAFE,
                        DbContract.COLUMN_MOVE1, DbContract.COLUMN_MOVE2, DbContract.COLUMN_ACQ1,
                        DbContract.COLUMN_ACQ2, DbContract.COLUMN_POS, DbContract.COLUMN_TOTE},
                        DbContract.COLUMN_ID + " = ? AND " + DbContract.COLUMN_SEASON + " = ?",
                        new String[]{String.valueOf(pos + 1), SettingsActivity.SEASONS[season]}, null,
                        null, DbContract.COLUMN_ID + " ASC", null);

        if (cursor != null && cursor.moveToFirst()) {
            // Load item values
            RobotInfoItem robot = new RobotInfoItem();
            robot.setId(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_ID)));
            robot.setSeason(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_SEASON)));
            robot.setName(cursor.getString(cursor.getColumnIndex(DbContract.COLUMN_ROBOT)));
            robot.setStyle(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_STYLE)));
            robot.setHeight(cursor.getString(cursor.getColumnIndex(DbContract.COLUMN_HEIGHT)));
            robot.setWeight(cursor.getString(cursor.getColumnIndex(DbContract.COLUMN_WEIGHT)));
            robot.setDriveTrain(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_DRIVE)));
            robot.setWheelType(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_WHEEL)));
            robot.setStrafe(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_STRAFE)));
            robot.setMoveTote(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_MOVE1)));
            robot.setMoveCont(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_MOVE2)));
            robot.setAcqTote(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_ACQ1)));
            robot.setAcqTote(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_ACQ1)));
            robot.setAcqCont(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_ACQ2)));
            robot.setAcqCont(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_ACQ2)));
            robot.setMaxTote(cursor.getString(cursor.getColumnIndex(DbContract.COLUMN_TOTE)));
            cursor.close(); // Close cursor
            return robot;
        }

        return null;
    }

    public List<TeamListItem> getAllTeamItems() {
        List<TeamListItem> teams = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + DbContract.TEAM_LIST_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TeamListItem item = new TeamListItem(cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));
                item.setId(cursor.getInt(cursor.getColumnIndex(DbContract.COLUMN_ID)));
                teams.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close(); // Close the cursor

        return teams;
    }

    public int getTeamCount() {
        String selectQuery = "SELECT  * FROM " + DbContract.TEAM_LIST_TABLE;
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
