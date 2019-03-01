package com.example.android.popularmoviesstg2.adapters;


import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesstg2.MainActivity;
import com.example.android.popularmoviesstg2.R;
import com.example.android.popularmoviesstg2.interfaces.FilmClickListener;
import com.example.android.popularmoviesstg2.models.Film;
import com.squareup.picasso.Picasso;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmHolder>{

    private static final String URL_IMG_PATH = "http://image.tmdb.org/t/p/w342";
    private Context filmContext;
    private Film[] aFilm;
    private FilmClickListener filmClickListener;

    public FilmAdapter(Film[] films, Context context, FilmClickListener clickListener){
        aFilm = films;
        filmContext = context;
        filmClickListener = clickListener;
    }

    class FilmHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final ImageView imageViewHolder;

        FilmHolder (View view){
            super(view);

            imageViewHolder = view.findViewById(R.id.iv_list_item_poster);
            imageViewHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            filmClickListener.onClickFilm(clickPosition);
        }
    }

    @NonNull
    @Override
    public FilmHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.list_item_film_poster, viewGroup, false);
        return new FilmHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmHolder holder, int position) {

        Picasso.get()
                .load(URL_IMG_PATH.concat(MainActivity.aFilm[position].getFilmPoster()))
                .fit()
                .into(holder.imageViewHolder);
    }

    @Override
    public int getItemCount() {
        return aFilm.length;
    }
}
