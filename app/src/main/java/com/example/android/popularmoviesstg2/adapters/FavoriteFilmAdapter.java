package com.example.android.popularmoviesstg2.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesstg2.DetailsActivity;
import com.example.android.popularmoviesstg2.R;
import com.example.android.popularmoviesstg2.interfaces.FilmClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteFilmAdapter extends RecyclerView.Adapter<FavoriteFilmAdapter.FilmHolder> {

    private Context context;
    private FilmClickListener filmClickListener;

    public FavoriteFilmAdapter(ArrayList<String[]> arrayList, Context context1, FilmClickListener filmClickListener1){
        DetailsActivity.arrayListFilm = arrayList;
        context = context1;
        filmClickListener = filmClickListener1;
    }

    @Override
    public int getItemCount() {
        return DetailsActivity.favFilm.length;
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
                .load(DetailsActivity.favFilm[position][1])
                .into(holder.imageViewHolder);
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
}
