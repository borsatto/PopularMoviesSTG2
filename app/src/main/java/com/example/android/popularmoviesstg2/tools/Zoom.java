package com.example.android.popularmoviesstg2.tools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.popularmoviesstg2.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Zoom extends AppCompatActivity {

    PhotoView photoView;
    public String urlPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        photoView = findViewById(R.id.photo_view);
        Bundle bundle = getIntent().getExtras();
        urlPoster = bundle.getString("poster");
        String title = bundle.getString("title");
        setTitle(title);

        Picasso.get()
                .load(urlPoster)
                .into(photoView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.zoom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_save:
                shareImg(urlPoster, this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    static public void shareImg(String url, final Context context){

        Picasso.get()
                .load(url)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, context));
                        context.startActivity(Intent.createChooser(intent, "Share image"));
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
    }

    static public Uri getLocalBitmapUri(Bitmap bitmap, Context context){
        Uri bitmapUri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".jp");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.close();
            bitmapUri = Uri.fromFile(file);
        } catch (IOException e){
            e.printStackTrace();
        }
        return bitmapUri;
    }
}
