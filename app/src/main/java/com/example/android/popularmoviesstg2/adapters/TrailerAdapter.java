package com.example.android.popularmoviesstg2.adapters;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.popularmoviesstg2.R;
import com.example.android.popularmoviesstg2.models.Trailer;
import com.squareup.picasso.Picasso;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {

    private Trailer[] trailers;
    private TextView tv_trailer_adapter;
    private ImageView iv_trailer_img;
    private Context context;

    public TrailerAdapter(Trailer[] trailers1, Context context1){
        this.trailers = trailers1;
        this.context = context1;
    }

    @Override
    public int getItemCount(){
        return trailers.length;
    }

    @NonNull
    @Override
    public TrailerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_trailer, viewGroup, false);
        return new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerHolder holder, @SuppressLint("RecycleView") final int position) {
        String name = trailers[position].getFilmName();
        final String key = trailers[position].getFilmKey();
        tv_trailer_adapter.append(name);
        final String URL_BASE_YOUTUBE_VIDEO = "https://www.youtube.com/watch?v=";
        String URL_BASE_YOUTUBE_IMG = "http://img.youtube.com/vi/";
        final String URL_BASE_VND = "vnd.youtube";
        final String urlVideo = String.valueOf(URL_BASE_YOUTUBE_VIDEO.concat(key));
        Picasso.get()
                .load(URL_BASE_YOUTUBE_IMG.concat(key).concat("/0.jpg"))
                .into(iv_trailer_img);
        iv_trailer_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
                v.startAnimation(shake);
                CountDownTimer countDown = new CountDownTimer(80, 15) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_BASE_VND.concat(key)));
                        Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(urlVideo));
                        try{
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException anfe){
                            context.startActivity(intent1);
                        }
                    }
                };
                countDown.start();
            }
        });
    }

    class TrailerHolder extends RecyclerView.ViewHolder{

        TrailerHolder(View view){
            super(view);
            tv_trailer_adapter = itemView.findViewById(R.id.tv_trailer_adapter);
            iv_trailer_img = itemView.findViewById(R.id.iv_trailer_img);
        }

    }
}
