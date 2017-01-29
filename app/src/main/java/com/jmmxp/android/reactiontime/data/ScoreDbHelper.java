package com.jmmxp.android.reactiontime.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jmmxp.android.reactiontime.data.ScoreContract.ScoreEntry;

/**
 * Created by jmmxp on 28/01/17.
 */

public class ScoreDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "data.db";
    private static final int DATABASE_VERSION = 1;


    public ScoreDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_COMMAND = "CREATE TABLE " + ScoreEntry.TABLE_NAME + "(" +
                ScoreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ScoreEntry.COLUMN_TIME + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_COMMAND);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
