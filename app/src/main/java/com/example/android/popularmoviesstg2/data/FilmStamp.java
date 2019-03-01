package com.example.android.popularmoviesstg2.data;


import android.net.Uri;
import android.provider.BaseColumns;

public class FilmStamp {

    static final String AUTHORITY = "com.example.android.popularmoviesstg2";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    static final String PATH_FILMS = "films";

    public static final class FilmEntry implements BaseColumns{

        public static final Uri FINAL_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FILMS).build();

    static final String TABLE_NAME = "films";
    public static final String COLUMN_IDFILM = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_SYNOPSIS = "synopsis";
    public static final String COLUMN_VOTING = "voting";
    public static final String COLUMN_RELEASE = "release";
    }
}
