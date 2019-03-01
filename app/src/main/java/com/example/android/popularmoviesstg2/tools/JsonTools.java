package com.example.android.popularmoviesstg2.tools;

import android.app.Application;

import com.example.android.popularmoviesstg2.models.Film;
import com.example.android.popularmoviesstg2.models.Review;
import com.example.android.popularmoviesstg2.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonTools extends Application {

    private static final String FILM_RESULTS = "results";
    private static final String FILM_ID = "id";
    private static final String FILM_TITLE = "title";
    private static final String FILM_POSTER = "poster_path";
    private static final String FILM_SYNOPSIS = "overview";
    private static final String FILM_VOTING = "vote_average";
    private static final String FILM_DATE = "release_date";
    private static final String FILM_AUTHOR = "author";
    private static final String CONTENT = "content";
    private static final String TRAILER_NAME = "name";
    private static final String TRAILER_SOURCE = "source";
    private static final String TRAILER_RESULTS = "youtube";

    static Film[] parseJsonFilm (String jsonFilmData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonFilmData);
        JSONArray jsonArray = jsonObject.getJSONArray(FILM_RESULTS);
        Film[] result = new Film[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++){
            Film film = new Film();
            film.setFilmId(jsonArray.getJSONObject(i).optString(FILM_ID));
            film.setFilmTitle(jsonArray.getJSONObject(i).optString(FILM_TITLE));
            film.setFilmPoster(jsonArray.getJSONObject(i).optString(FILM_POSTER));
            film.setFilmSynopsis(jsonArray.getJSONObject(i).optString(FILM_SYNOPSIS));
            film.setFilmVoting(jsonArray.getJSONObject(i).optString(FILM_VOTING));
            film.setFilmReleaseDate(jsonArray.getJSONObject(i).optString(FILM_DATE));
            result[i] = film;
        }
        return result;
    }

    public static Review[] parseJsonReview (String jsonReviewData) throws JSONException{
        JSONObject jsonObject = new JSONObject(jsonReviewData);
        JSONArray jsonArray = jsonObject.getJSONArray(FILM_RESULTS);
        Review[] result = new Review[jsonArray.length()];
        for (int i = 0; i <jsonArray.length(); i++){
            Review review = new Review();
            review.setFilmAuthor(jsonArray.getJSONObject(i).optString(FILM_AUTHOR));
            review.setFilmContent(jsonArray.getJSONObject(i).optString(CONTENT));
            result[i] = review;
        }
        return result;
    }

    public static Trailer[] parseJsonTrailer (String jsonTrailerData) throws JSONException{
        JSONObject jsonObject = new JSONObject(jsonTrailerData);
        JSONArray jsonArray = jsonObject.getJSONArray(TRAILER_RESULTS);
        Trailer[] result = new Trailer[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++){
            Trailer trailer = new Trailer();
            trailer.setFilmKey(jsonArray.getJSONObject(i).optString(TRAILER_SOURCE));
            trailer.setFilmName((jsonArray.getJSONObject(i).optString(TRAILER_NAME)));
            result[i] = trailer;
        }
        return result;
    }
}
