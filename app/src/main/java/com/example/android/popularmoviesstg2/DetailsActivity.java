package com.example.android.popularmoviesstg2;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.android.popularmoviesstg2.adapters.ReviewAdapter;
import com.example.android.popularmoviesstg2.adapters.TrailerAdapter;
import com.example.android.popularmoviesstg2.tools.JsonTools;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstg2.data.FilmStamp;
import com.example.android.popularmoviesstg2.models.Film;
import com.example.android.popularmoviesstg2.models.Review;
import com.example.android.popularmoviesstg2.models.Trailer;
import com.example.android.popularmoviesstg2.tools.UrlTools;
import com.example.android.popularmoviesstg2.tools.Zoom;
import com.squareup.picasso.Picasso;
import com.example.android.popularmoviesstg2.databinding.ActivityDetailsBinding;

import java.net.URL;
import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.popularmoviesstg2.MainActivity.button_retry;
import static com.example.android.popularmoviesstg2.MainActivity.context;
import static com.example.android.popularmoviesstg2.MainActivity.tv_error;


public class DetailsActivity extends AppCompatActivity {

    ActivityDetailsBinding binding;
    @BindView(R.id.rv_reviews)
    RecyclerView recyclerViewReview;
    @BindView(R.id.rv_trailers)
    RecyclerView recyclerViewTrailer;
    @BindView(R.id.fb_detail)
    FloatingActionButton floatingButton;

    private static final String TAG = DetailsActivity.class.getSimpleName();
    private static final String URL_PATH_IMG = "http://image.tmdb.org/t/p/w342";
    private static final String URL_PATH_LARGE_IMG = "http://image.tmdb.org/t/p/w780";
    private final String URL_YOUTUBE = "http://www.youtube.com/watch?v=";
    public static Film filmData;
    private Review[] reviews = null;
    private Trailer[] trailers = null;
    private String id;
    private String title;
    private String poster;
    private String synopsis;
    private String voting;
    private String release;
    private String releaseFinal;
    int position;
    int toastId;
    public static String[][] favFilm;
    public static ArrayList<String[]> arrayListFilm = new ArrayList<>();
    public static ArrayList<String> dataDetails = new ArrayList<>();
    public static boolean fromFav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        ButterKnife.bind(this);
        recyclerViewReview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewReview.setHasFixedSize(true);
        recyclerViewTrailer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewTrailer.setHasFixedSize(true);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        fromFav = bundle.getBoolean("fromFavorites");
        position = bundle.getInt("sendPosition");

        if (!fromFav){
            fillVarsFilms();
        } else {
            fillVarsFavFilms();
        }

        new FilmFetchTaskReview().execute("reviews");
        new FilmFetchTaskTrailer().execute("trailers");
    }

    public void toastMsg(){
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.toast, (ViewGroup)findViewById(R.id.toast_container));
        TextView textView = view.findViewById(R.id.tv_toast);
        switch (toastId){
            case 0:
                textView.setText(getResources().getString(R.string.toast_added));
                break;
            case 1:
                textView.setText(getResources().getString(R.string.toast_removed));
                break;
            case 2:
                textView.setText(getResources().getString(R.string.toast_already_in_favorites));
                break;
        }
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0,0);
        toast.setDuration(toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    public void putData(){
        binding.tvTitle.setText(title);
        binding.tvSynopsis.setText(synopsis);
        binding.tvVoting.setText(voting);
        binding.tvRelease.setText(releaseFinal);
        setTitle(title);
        dataDetails.add(getTitle().toString());
        dataDetails.add(poster);
        dataDetails.add(synopsis);
        dataDetails.add(voting);
        dataDetails.add(releaseFinal);
    }

    public void zoom(View view){
        String urlLargeImage;
        if (!fromFav){
            urlLargeImage = URL_PATH_LARGE_IMG.concat(poster);
        } else {
            urlLargeImage = favFilm[position][1];
        }
        Intent intent = new Intent(this, Zoom.class);
        intent.putExtra("poster", urlLargeImage);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    public void fillVarsFilms(){
        filmData = getIntent().getParcelableExtra("SendData");
        title = filmData.getFilmTitle();
        poster = filmData.getFilmPoster();
        synopsis = filmData.getFilmSynopsis();
        voting = filmData.getFilmVoting();
        release = filmData.getFilmReleaseDate();
        releaseFinal = release.substring(0,4);
        floatingButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite));
        Picasso.get()
                .load(URL_PATH_IMG.concat(poster))
                .into(binding.ivPosterDetail);
        putData();
    }

    public void fillVarsFavFilms(){
        floatingButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_trash));
        Picasso.get()
                .load(favFilm[position][1])
                .into(binding.ivPosterDetail);
        title = favFilm[position][0];
        synopsis = favFilm[position][4];
        voting = favFilm[position][3];
        release = favFilm[position][2];
        releaseFinal = release.substring(0, 4);
        id = favFilm[position][5];
        putData();
    }

    public boolean markedAsFavorite(){
        Cursor cursor = getContentResolver().query(FilmStamp.FilmEntry.FINAL_CONTENT_URI,
                null,
                null,
                null,
                FilmStamp.FilmEntry.COLUMN_IDFILM);
        if (cursor != null){
            while (cursor.moveToNext()) {
                String idFilms = cursor.getString(1);
                String idFilmSelected;
                if (!fromFav) {
                    idFilmSelected = String.valueOf(filmData.getFilmId());
                } else {
                    idFilmSelected = favFilm[position][5];
                }
                if (idFilms.equals(idFilmSelected)) {
                    return false;
                }
            }
        }
        assert cursor != null;
        cursor.close();
        return true;
    }

    public void saveFilmData(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FilmStamp.FilmEntry.COLUMN_IDFILM, filmData.getFilmId());
        contentValues.put(FilmStamp.FilmEntry.COLUMN_TITLE, filmData.getFilmTitle());
        contentValues.put(FilmStamp.FilmEntry.COLUMN_IMAGE, URL_PATH_LARGE_IMG.concat(filmData.getFilmPoster()));
        contentValues.put(FilmStamp.FilmEntry.COLUMN_SYNOPSIS, filmData.getFilmSynopsis());
        contentValues.put(FilmStamp.FilmEntry.COLUMN_VOTING, filmData.getFilmVoting());
        contentValues.put(FilmStamp.FilmEntry.COLUMN_RELEASE, filmData.getFilmReleaseDate());
        getContentResolver().insert(FilmStamp.FilmEntry.FINAL_CONTENT_URI, contentValues);
        toastId = 0;
        toastMsg();
    }

    public void deleteFilmData(){
        ContentResolver contentResolver = getContentResolver();
        Uri uri = FilmStamp.FilmEntry.FINAL_CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(id)).build();
        contentResolver.delete(uri, null,null);
        MainActivity.dataDetails.clear();
        toastId = 1;
        toastMsg();

    }

    public void saveDeletedData (View view){

        if (!markedAsFavorite() && !fromFav){
            toastId = 2;
            toastMsg();
            return;
        }
        if (!markedAsFavorite()){
            toastId = 1;
            deleteFilmData();
            if (fromFav)
				floatingButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite));
        } else {
            toastId = 0;
            saveFilmData();
            if (fromFav)
				floatingButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_trash));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                switch (MainActivity.queryFilm){
                    case "popular":
                        MainActivity.queryFilm = "popular";
                        break;
                    case "top_voted":
                        MainActivity.queryFilm = "top_voted";
                        break;
                }
                break;
            case R.id.menu_share:
                Log.e(TAG, trailers[position].getFilmKey());
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                if (!fromFav){
                    intent.putExtra(Intent.EXTRA_SUBJECT, filmData.getFilmTitle());
                    intent.putExtra(Intent.EXTRA_TEXT, filmData.getFilmTitle() + " " + URL_YOUTUBE.concat(trailers[position].getFilmKey()));
                } else {
                    intent.putExtra(Intent.EXTRA_SUBJECT, favFilm[position][0]);
                    intent.putExtra(Intent.EXTRA_TEXT, favFilm[position][0] + " " + URL_YOUTUBE.concat(trailers[position].getFilmKey()));
                }
                startActivity(Intent.createChooser(intent, getString(R.string.trailer_share)));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    private class FilmFetchTaskReview extends AsyncTask<String, Void, Review[]>{


            URL reviewURL;					  

        @Override
        protected Review[] doInBackground(String... strings) {


            if (UrlTools.API_KEY.equals("")) {
                MainActivity.errorNetworkApi();
                tv_error.setText(R.string.error_api_key);
                tv_error.setTextColor(ContextCompat.getColor(context, R.color.secondaryTextColor));
                button_retry.setVisibility(View.INVISIBLE);
                return null;
            }
            if (!fromFav) {
                reviewURL = UrlTools.buildUrlReview(String.valueOf(filmData.getFilmId()).concat("/"), strings[0]);
            } else {
                reviewURL = UrlTools.buildUrlReview(id.concat("/"), strings[0]);
            }
            String reviewResponse;
            try {
                reviewResponse = UrlTools.getResponseFromHttp(reviewURL);
                reviews = JsonTools.parseJsonReview(reviewResponse);
            } catch (Exception e) {
                Log.e(TAG, "Problems with the Review", e);
            }
            return reviews;
        }

        @Override
        protected void onPostExecute(Review[] reviews1) {
            if (reviews1 != null){
                reviews = reviews1;
                ReviewAdapter reviewAdapter = new ReviewAdapter(reviews);
                recyclerViewReview.setAdapter(reviewAdapter);
            } else{
                Log.e(TAG, "Problems with the adapter");
            }
            if (isCancelled()){
                new FilmFetchTaskReview().cancel(true);
            }
            for (Review review : reviews){
                dataDetails.add(review.getFilmAuthor());
                dataDetails.add(review.getFilmContent());
            }
            assert reviews1 !=null;
            if (reviews1.length == 0){
                recyclerViewReview.setVisibility(View.INVISIBLE);
                binding.tvAdapterNoData.setVisibility(View.VISIBLE);
                binding.tvAdapterNoData.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondaryTextColor));
            }
        }

        @Override
        protected void onCancelled(Review[] reviews) {
            super.onCancelled(reviews);
            new FilmFetchTaskReview().cancel(true);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class FilmFetchTaskTrailer extends AsyncTask<String, Void, Trailer[]>{

        URL trailerURL;

        @Override
        protected Trailer[] doInBackground(String... strings) {

            if(UrlTools.API_KEY.equals("")){
                MainActivity.errorNetworkApi();
                tv_error.setText(R.string.error_api_key);
                tv_error.setTextColor(ContextCompat.getColor(context, R.color.secondaryTextColor));
                button_retry.setVisibility(View.INVISIBLE);
                return null;
            }
            if(!fromFav){
                trailerURL = UrlTools.buildUrlTrailer(String.valueOf(filmData.getFilmId()).concat("/"),strings[0]);
            } else{
                trailerURL = UrlTools.buildUrlTrailer(id.concat("/"),strings [0]);
            }
            String trailerResponse;
            try {
                trailerResponse = UrlTools.getResponseFromHttp(trailerURL);
                trailers = JsonTools.parseJsonTrailer(trailerResponse);
            } catch (Exception e){
                Log.e(TAG, "Problems with the trailer", e);
            }
            return trailers;
        }

        @Override
        protected void onPostExecute(Trailer[] trailers1) {
            if (trailers1 != null){
                trailers = trailers1;
                TrailerAdapter trailerAdapter = new TrailerAdapter(trailers, DetailsActivity.this);
                recyclerViewTrailer.setAdapter(trailerAdapter);
            } else{
                Log.e(TAG, "Problems with the adapter");
            }
            if (isCancelled()){
                new FilmFetchTaskTrailer().cancel(true);
            }
            for (Trailer trailers2 : trailers){
                dataDetails.add(trailers2.getFilmName());
                dataDetails.add(trailers2.getFilmKey());
            }
            assert trailers1 !=null;
            if (trailers1.length==0){
                recyclerViewTrailer.setVisibility(View.INVISIBLE);
                binding.tvAdapterNoDataReview.setVisibility(View.VISIBLE);

            }
        }

        @Override
        protected void onCancelled(Trailer[] trailers) {
            super.onCancelled(trailers);
            new FilmFetchTaskTrailer().cancel(true);
        }
    }
}
