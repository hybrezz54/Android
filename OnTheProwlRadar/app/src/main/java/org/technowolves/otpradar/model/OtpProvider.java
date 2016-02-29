package org.technowolves.otpradar.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class OtpProvider extends ContentProvider {

    private static final String AUTHORITY = "org.technowolves.otpradar";
    private static final String URL = "content://" + AUTHORITY + "/teams";
    private static final Uri CONTENT_URI = Uri.parse(URL);

    private DbHelper mDbHelper;

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public boolean onCreate() {
        Context c = getContext();
        mDbHelper = new DbHelper(c);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

}
