package com.example.android.popularmoviesstg2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesstg2.adapters.FavoriteFilmAdapter;
import com.example.android.popularmoviesstg2.adapters.FilmAdapter;
import com.example.android.popularmoviesstg2.data.FilmStamp;
import com.example.android.popularmoviesstg2.interfaces.AsynkTaskListeningFilm;
import com.example.android.popularmoviesstg2.interfaces.FilmClickListener;
import com.example.android.popularmoviesstg2.models.Film;
import com.example.android.popularmoviesstg2.tools.FetchDataTask;
import com.example.android.popularmoviesstg2.tools.UrlTools;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FilmClickListener {

    private static final String QUERY = "query";
    private static final String NAMESORT = "namesort";
    private static final String FAVORITES = "favorites";
    public static String queryFilm = "popular";
    private String nameSort = "Popular movies";
    public static Film[] aFilm = null;
    public static boolean markedAsFav;
    public static ArrayList<String> dataDetails = new ArrayList<>();
    private FavoriteFilmAdapter favoriteFilmAdapter;
    public static RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    public static ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    public static TextView tv_error;
    @SuppressLint("StaticFieldLeak")
    public static TextView tv_no_data;
    @SuppressLint("StaticFieldLeak")
    public static Button button_retry;
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv_main);
        button_retry = findViewById(R.id.button_retry);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        progressBar = findViewById(R.id.pb_main);
        tv_error = findViewById(R.id.tv_error);
        tv_no_data = findViewById(R.id.tv_no_data);
        tv_no_data.setVisibility(View.INVISIBLE);
        context = getApplicationContext();


        setTitle(nameSort);
        if (!isConnected()) {
            errorNetworkApi();
            return;
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(FAVORITES)) {
                nameSort = savedInstanceState.getString(FAVORITES);
                queryFilm = "favorites";
                setTitle(nameSort);
                loadFavData();
                return;
            }
            if (savedInstanceState.containsKey(QUERY) || savedInstanceState.containsKey(NAMESORT)) {
                queryFilm = savedInstanceState.getString(QUERY);
                nameSort = savedInstanceState.getString(NAMESORT);
                setTitle(nameSort);
                new FetchDataTask(this, new FilmFetchTaskCompleteListener()).execute(queryFilm);
                return;
            }
        }
        if (nameSort.equals("Favorites")) return;
        new FetchDataTask(this, new FilmFetchTaskCompleteListener()).execute(queryFilm);
    }

    public void loadFavorites() {
        dataDetails.clear();
        Cursor cursor = getContentResolver().query(FilmStamp.FilmEntry.FINAL_CONTENT_URI, null, null, null, FilmStamp.FilmEntry._ID);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                dataDetails.add(cursor.getString(cursor.getColumnIndex(FilmStamp.FilmEntry.COLUMN_IMAGE)));
            }
        }
        assert cursor != null;
        cursor.close();
        loadData();
        recyclerView.setVisibility(View.VISIBLE);
        hidePBTV();
        favoriteFilmAdapter = new FavoriteFilmAdapter(DetailsActivity.arrayListFilm, this, MainActivity.this);
        favoriteFilmAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(favoriteFilmAdapter);
        if (nameSort.equals("Favorites")) {
            if (dataDetails.size() == 0) {
                tv_no_data.setVisibility(View.VISIBLE);
                tv_no_data.setTextColor(ContextCompat.getColor(this, R.color.secondaryTextColor));
            } else {
                tv_no_data.setVisibility(View.INVISIBLE);
            }
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private void loadData() {
        DetailsActivity.arrayListFilm.clear();
        DetailsActivity.favFilm = null;
        Cursor cursor = getContentResolver().query(FilmStamp.FilmEntry.FINAL_CONTENT_URI, null, null, null, FilmStamp.FilmEntry._ID);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                DetailsActivity.arrayListFilm.add(new String[]{
                        cursor.getString(cursor.getColumnIndex(FilmStamp.FilmEntry.COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(FilmStamp.FilmEntry.COLUMN_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(FilmStamp.FilmEntry.COLUMN_RELEASE)),
                        cursor.getString(cursor.getColumnIndex(FilmStamp.FilmEntry.COLUMN_VOTING)),
                        cursor.getString(cursor.getColumnIndex(FilmStamp.FilmEntry.COLUMN_SYNOPSIS)),
                        cursor.getString(cursor.getColumnIndex(FilmStamp.FilmEntry.COLUMN_IDFILM))
                });
            }
            DetailsActivity.favFilm = DetailsActivity.arrayListFilm.toArray(new String[DetailsActivity.arrayListFilm.size()][5]);
            cursor.close();
        }
    }

    private void hidePBTV() {
        progressBar.setVisibility(View.INVISIBLE);
        tv_error.setVisibility(View.INVISIBLE);
    }

    public static void errorNetworkApi() {
        progressBar.setVisibility(View.INVISIBLE);
        tv_error.setVisibility(View.VISIBLE);
        tv_error.setTextColor(ContextCompat.getColor(context, R.color.secondaryTextColor));
        button_retry.setVisibility(View.VISIBLE);
    }

    public void loadFavData() {
        loadFavorites();
        favoriteFilmAdapter = new FavoriteFilmAdapter(DetailsActivity.arrayListFilm, this, MainActivity.this);
        favoriteFilmAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(favoriteFilmAdapter);
    }

    public static void preExecute() {
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClickFilm(int position) {
        if (!isConnected()) {
            recyclerView.setVisibility(View.INVISIBLE);
            errorNetworkApi();
            return;
        }
        if (nameSort.equals("Favorites")) {
            markedAsFav = true;
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("fromFavorites", markedAsFav);
            intent.putExtra("sendPosition", position);
            startActivity(intent);
            markedAsFav = false;
            return;
        }
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("SendData", aFilm[position]);
        startActivity(intent);

    }

    public void clickRetry(View view) {
        if (!isConnected()) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
            view.startAnimation(animation);
            return;
        }
        queryFilm = "popular";
        button_retry.setVisibility(View.INVISIBLE);
        tv_error.setVisibility(View.INVISIBLE);
        new FetchDataTask(this, new FilmFetchTaskCompleteListener()).execute(queryFilm);
    }

    private class FilmFetchTaskCompleteListener implements AsynkTaskListeningFilm {
        @Override
        public void onTaskComplete(Film[] result) {
            if (result != null) {
                recyclerView.setVisibility(View.VISIBLE);
                hidePBTV();
                aFilm = result;
                FilmAdapter filmAdapter = new FilmAdapter(aFilm, MainActivity.this, MainActivity.this);
                recyclerView.setAdapter(filmAdapter);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!isConnected()) return false;
        if (UrlTools.API_KEY.equals("")) return false;
        int id = item.getItemId();
        switch (id) {
            case R.id.popularity:
                markedAsFav = false;
                tv_no_data.setVisibility(View.INVISIBLE);
                queryFilm = "popular";
                new FetchDataTask(this, new FilmFetchTaskCompleteListener()).execute(queryFilm);
                nameSort = "Popular Movies";
                setTitle(nameSort);
                break;
            case R.id.top_voted:
                markedAsFav = false;
                tv_no_data.setVisibility(View.INVISIBLE);
                queryFilm = "top_rated";
                new FetchDataTask(this, new FilmFetchTaskCompleteListener()).execute(queryFilm);
                nameSort = "Most Voted Movies";
                setTitle(nameSort);
                break;
            case R.id.favorites:
                markedAsFav = true;
                nameSort = "Favorites";
                queryFilm = "favorites";
                loadFavorites();
                setTitle(nameSort);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (!isConnected()) {
            recyclerView.setVisibility(View.INVISIBLE);
            errorNetworkApi();
            return;
        }
        tv_no_data.setVisibility(View.INVISIBLE);
        if (DetailsActivity.fromFav || markedAsFav) {
            markedAsFav = true;
            nameSort = "Favorites";
            loadFavData();
            setTitle(nameSort);
        } else if (queryFilm.equals("top_voted")) {
            markedAsFav = false;
            nameSort = "Top Voted Movies";
            setTitle(nameSort);
        }
    }
}
