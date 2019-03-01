package com.example.android.popularmoviesstg2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FilmDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "database.db";
    private static final int DB_VERSION = 1;

    FilmDbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + FilmStamp.FilmEntry.TABLE_NAME + " (" +
                FilmStamp.FilmEntry._ID + " INTEGER PRIMARY KEY, " +
                FilmStamp.FilmEntry.COLUMN_IDFILM + " TEXT NOT NULL, " +
                FilmStamp.FilmEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FilmStamp.FilmEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
                FilmStamp.FilmEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                FilmStamp.FilmEntry.COLUMN_VOTING + " TEXT NOT NULL, " +
                FilmStamp.FilmEntry.COLUMN_RELEASE + " TEXT NOT NULL);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FilmStamp.FilmEntry.TABLE_NAME);
        onCreate(db);
    }
}
