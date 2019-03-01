package com.example.android.popularmoviesstg2.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.View;

import com.example.android.popularmoviesstg2.MainActivity;
import com.example.android.popularmoviesstg2.R;
import com.example.android.popularmoviesstg2.interfaces.AsynkTaskListeningFilm;
import com.example.android.popularmoviesstg2.models.Film;

import java.net.URL;

import androidx.core.content.ContextCompat;

import static com.example.android.popularmoviesstg2.MainActivity.button_retry;
import static com.example.android.popularmoviesstg2.MainActivity.tv_error;

public class FetchDataTask extends AsyncTask<String, Void, Film[]> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private AsynkTaskListeningFilm listeningFilm;


    public FetchDataTask (Context context1, AsynkTaskListeningFilm asynkTaskListeningFilm){
        this.context = context1;
        this.listeningFilm = asynkTaskListeningFilm;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MainActivity.preExecute();
    }

    @Override
    protected Film[] doInBackground(String... strings) {
        Film[] aFilms = null;
        if(!isConnected()){
            MainActivity.errorNetworkApi();
            return null;
        }
        if (UrlTools.API_KEY.equals("")){
            MainActivity.errorNetworkApi();
            tv_error.setText(R.string.error_api_key);
            tv_error.setTextColor(ContextCompat.getColor(context, R.color.secondaryTextColor));
            button_retry.setVisibility(View.INVISIBLE);
            return null;
        }
        URL filmURL = UrlTools.buildUrl(strings[0]);

        String filmResponse;
        try {
            filmResponse = UrlTools.getResponseFromHttp(filmURL);
            aFilms = JsonTools.parseJsonFilm(filmResponse);
        } catch (Exception e){
            e.printStackTrace();
        }
        return aFilms;
    }

    @Override
    protected void onPostExecute(Film[] films) {
        super.onPostExecute(films);
        listeningFilm.onTaskComplete(films);
    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager !=null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isConnectedOrConnecting();
    }
}
