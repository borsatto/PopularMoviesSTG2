package com.example.android.popularmoviesstg2.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmoviesstg2.R;
import com.example.android.popularmoviesstg2.models.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private Review[] filmReview;
    private TextView tvAuthorAdapter;
    private TextView tvReviewAdapter;

    public ReviewAdapter(Review[] reviews){
        this.filmReview = reviews;
    }

    @Override
    public int getItemCount(){
        return filmReview.length;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int positions) {
        String author = filmReview[positions].getFilmAuthor();
        String review = filmReview[positions].getFilmContent();
        tvAuthorAdapter.setText(author);
        tvReviewAdapter.setText(review);
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.list_item_review, viewGroup, false);
        return new ReviewHolder(view);
    }

    class ReviewHolder extends RecyclerView.ViewHolder{

        ReviewHolder(View view){
        super(view);
        tvAuthorAdapter = itemView.findViewById(R.id.tv_author);
        tvReviewAdapter = itemView.findViewById(R.id.tv_review_adapter);
        }
    }

}
