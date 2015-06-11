package org.technowolves.otpradar.framework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "otpradar";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "team_list";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NUMBER = "team_number";
    public static final String COLUMN_NAME = "team_name";
    public static final String COLUMN_SITE = "team_site";

    private static final String DATABASE_QUERY = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE +
            "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_NUMBER + " TEXT NOT NULL, "
            + COLUMN_NAME + " TEXT NOT NULL, " + COLUMN_SITE + " TEXT NOT NULL)";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    public void addTeamItem(DatabaseTeamItem team) {
        // Open db connection
        SQLiteDatabase db = this.getWritableDatabase();

        // Store values of team item
        ContentValues values = new ContentValues();
        values.put(COLUMN_NUMBER, team.getNumber());
        values.put(COLUMN_NAME, team.getName());
        values.put(COLUMN_SITE, team.getWebsite());

        // Insert row into table
        db.insert(DATABASE_TABLE, null, values);
        db.close(); // Close db connection
    }

    public int updateTeamItem(DatabaseTeamItem team) {
        // Open db connection
        SQLiteDatabase db = this.getWritableDatabase();

        // Store values of team item
        ContentValues values = new ContentValues();
        values.put(COLUMN_NUMBER, team.getNumber());
        values.put(COLUMN_NAME, team.getName());
        values.put(COLUMN_SITE, team.getWebsite());

        // Updating row
        int result = db.update(DATABASE_TABLE, values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(team.getId()) });
        db.close(); // Close db connection
        return result; // return the number of rows affected
    }

    public void deleteTeamItem(DatabaseTeamItem team) {
        // Open db connection
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete the record with the specified id
        db.delete(DATABASE_TABLE, COLUMN_ID + " = ?",
                new String[] { String.valueOf(team.getId()) });
        db.close(); // Close db connection
    }

    public Cursor getCursor() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + DatabaseHandler.DATABASE_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public DatabaseTeamItem getTeamItem(int id) {
        // Open db
        SQLiteDatabase db = this.getReadableDatabase();

        // Construct and execute query
        Cursor cursor = db.query(DATABASE_TABLE,
                new String[] { COLUMN_ID, COLUMN_NUMBER, COLUMN_NAME, COLUMN_SITE },
                COLUMN_ID + "= ?", new String[] { String.valueOf(id) },
                null, null, "id ASC", "100");

        if (cursor != null)
            cursor.moveToFirst();

        // Load item values into model
        DatabaseTeamItem team = new DatabaseTeamItem(cursor.getString(1),
                cursor.getString(2), cursor.getString(3));
        team.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));

        cursor.close(); // Close cursor

        return team;
    }

    public List<DatabaseTeamItem> getAllTeamItems() {
        List<DatabaseTeamItem> teams = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DatabaseTeamItem item = new DatabaseTeamItem(cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));
                item.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                teams.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close(); // Close the cursor

        return teams;
    }

    public int getTeamCount() {
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.close(); // Close the cursor
        return cursor.getCount();
    }

}
