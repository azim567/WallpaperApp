package com.thecodingshef.wallpaperapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.IOException;

public class FullImageActivity extends AppCompatActivity {

    ImageView fullImage;
    ProgressBar progressBar;
    Button save,setBack;
    String image_url, dirPath, fileName;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fullImage=findViewById(R.id.full_image);
        progressBar=findViewById(R.id.progressBar);
        save=findViewById(R.id.save);
        setBack=findViewById(R.id.setBack);

        image_url=getIntent().getStringExtra("imageUrl");

        loadImage(image_url);


        // Initialization Of DownLoad Button
        AndroidNetworking.initialize(getApplicationContext());

        //Folder Creating Into Phone Storage
        dirPath = Environment.getExternalStorageDirectory() + "/Image";

        fileName = "image.jpeg";

        //file Creating With Folder & Fle Name
        file = new File(dirPath, fileName);



        setBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHomeScreen();
            }
        });
        
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FullImageActivity.this, "save hello", Toast.LENGTH_SHORT).show();
                saveImage(image_url);
            }
        });



    }

    private void loadImage(String image_url) {

        Glide.with(this)
                .load(image_url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(fullImage);

    }

    private void saveImage(String image_url) {

        AndroidNetworking.download(this.image_url, dirPath, fileName)
                .build()
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                        Toast.makeText(FullImageActivity.this, "DownLoad Complete", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void setHomeScreen() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        Bitmap bitmap  = ((BitmapDrawable)fullImage.getDrawable()).getBitmap();
        try {
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(getApplicationContext(), "Wallpaper Set", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}