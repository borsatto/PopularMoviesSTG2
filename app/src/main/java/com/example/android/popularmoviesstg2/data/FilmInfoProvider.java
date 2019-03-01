package com.example.android.popularmoviesstg2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.popularmoviesstg2.R;

import static com.example.android.popularmoviesstg2.data.FilmStamp.FilmEntry.TABLE_NAME;

public class FilmInfoProvider extends ContentProvider {

    public static final int FILMS = 101;
    public static final int FILMS_WITH_ID = 102;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher1 = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher1.addURI(FilmStamp.AUTHORITY, FilmStamp.PATH_FILMS, FILMS);
        uriMatcher1.addURI(FilmStamp.AUTHORITY, FilmStamp.PATH_FILMS+"/#", FILMS_WITH_ID);
        return uriMatcher1;
    }

    private FilmDbHelper filmDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        filmDbHelper = new FilmDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = filmDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Uri rUri;
        switch (match){
            case FILMS:
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0){
                    rUri = ContentUris.withAppendedId(FilmStamp.FilmEntry.FINAL_CONTENT_URI, id);
                } else throw new SQLException(getContext().getString(R.string.failed_insert_row)+ uri);
                break;
                default:
                    throw new UnsupportedOperationException(getContext().getString(R.string.unknow_uri)+ uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rUri;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = filmDbHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);
        Cursor rCursor;
        switch (match){
            case FILMS:
                rCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
                default:
                    throw new UnsupportedOperationException(getContext().getString(R.string.unknow_uri) + uri);
        }
        rCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return rCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = filmDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int filmDeleted;
        switch (match){
            case FILMS_WITH_ID:
                String id = uri.getPathSegments().get(1);
                filmDeleted = db.delete(TABLE_NAME, FilmStamp.FilmEntry.COLUMN_IDFILM +"=?", new String[]{id});
                break;
                default:
                    throw new UnsupportedOperationException(getContext().getString(R.string.unknow_uri) + uri);
        }
        if (filmDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return filmDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
