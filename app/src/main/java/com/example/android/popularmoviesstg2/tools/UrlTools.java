package com.example.android.popularmoviesstg2.tools;

import android.net.Uri;
import android.util.Log;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class UrlTools {

    public static final String API_KEY = ""; // INSERT YOUR API KEY HERE //
    private static final String TAG = UrlTools.class.getSimpleName();
    private static final String QUERY_API = "api_key";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";


    static URL buildUrl (String filmUrl){
        Uri uri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(filmUrl)
                .appendQueryParameter(QUERY_API, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e){
            Log.e(TAG, "Failed to create URL", e);
        }
        return url;
    }

    public static URL buildUrlReview(String idFilm, String reviews){
        Uri uri = Uri.parse(BASE_URL.concat(idFilm).concat(reviews))
                .buildUpon()
                .appendQueryParameter(QUERY_API,API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        }
        catch (MalformedURLException e){
            Log.e(TAG, "Failed to create URL");
        }
        return url;
    }

    public static URL buildUrlTrailer(String idTrailer, String trailer){
        Uri uri = Uri.parse(BASE_URL.concat(idTrailer.concat(trailer)))
                .buildUpon()
                .appendQueryParameter(QUERY_API, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e){
            Log.e(TAG, "Failed to create URL", e);
        }
        return url;
    }

    public static String getResponseFromHttp (URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput){
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
