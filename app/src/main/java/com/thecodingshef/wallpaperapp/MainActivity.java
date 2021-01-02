package com.thecodingshef.wallpaperapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,RecyclerViewClickInterface {

    RecyclerView recyclerView;
    private String API_KEY="563492ad6f91700001000001408647ec35334e55b9aae2cbadca91fd";
    List<Wallpaper> dataList;
    boolean isScrolling=false;
    int totalItem, visibleItem, scrolledOutItem;
    SearchView searchView;
    CardView nature,wildlife,technology,education,love, popular;
    String searchText;
    RecyclerViewClickInterface recyclerViewClickInterface;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        searchView=findViewById(R.id.search);
        progressBar=findViewById(R.id.progressBar);
        searchText=searchView.getQuery().toString().toLowerCase();

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
               getData();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                getSearchedData(query);
                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {


                if(newText.isEmpty()){
                    getData();
                }
                else {
                    getSearchedData(newText);
                }


                return true;
            }
        });




        recyclerView=findViewById(R.id.recycler);
        nature=findViewById(R.id.nature);
        wildlife=findViewById(R.id.wildlife);
        education=findViewById(R.id.education);
        technology=findViewById(R.id.technologies);
        love=findViewById(R.id.love);
        popular=findViewById(R.id.popular);

        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,4);
        recyclerView.setLayoutManager(gridLayoutManager);


        popular.setOnClickListener(this);
        nature.setOnClickListener(this);
        wildlife.setOnClickListener(this);
        education.setOnClickListener(this);
        love.setOnClickListener(this);
        technology.setOnClickListener(this);

        recyclerViewClickInterface=(RecyclerViewClickInterface)MainActivity.this;

        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling=true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItem=gridLayoutManager.getItemCount();
                visibleItem=gridLayoutManager.getChildCount();
                scrolledOutItem=gridLayoutManager.findFirstVisibleItemPosition();


                if(isScrolling&& (visibleItem+scrolledOutItem==totalItem)){
                    getData();
                    isScrolling=false;
                }

            }
        });*/


        getData();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.popular:
                getData();
                break;
            case R.id.nature:
                getSearchedData("nature");
                break;
            case R.id.wildlife:
                getSearchedData("wildlife");
                break;
            case R.id.love:
                getSearchedData("love");
                break;
            case R.id.technologies:
                getSearchedData("technologies");
                break;
            case R.id.education:
                getSearchedData("education");
                break;

        }

    }

    private void getSearchedData(String query) {


        Call<WallpaperResponse> wallpaperResponseCall=RetrofitClient
                .getInstance()
                .getApi()
                .getSearch(API_KEY,query);

        wallpaperResponseCall.enqueue(new Callback<WallpaperResponse>() {
            @Override
            public void onResponse(Call<WallpaperResponse> call, Response<WallpaperResponse> response) {



                if(response.isSuccessful()){

                    dataList=response.body().getPhotosList();

                    WallpaperAdapter wallpaperAdapter=new WallpaperAdapter(getApplicationContext(),dataList,recyclerViewClickInterface);
                    recyclerView.setAdapter(wallpaperAdapter);
                    wallpaperAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<WallpaperResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getData() {

        Call<WallpaperResponse> wallpaperResponseCall=RetrofitClient
                .getInstance()
                .getApi()
                .getWallpaper(API_KEY);

        wallpaperResponseCall.enqueue(new Callback<WallpaperResponse>() {
            @Override
            public void onResponse(Call<WallpaperResponse> call, Response<WallpaperResponse> response) {

                WallpaperResponse wallpaperResponse=response.body();

                if(response.isSuccessful()){

                    dataList=response.body().getPhotosList();

                    WallpaperAdapter wallpaperAdapter=new WallpaperAdapter(getApplication(),dataList,recyclerViewClickInterface);
                    recyclerView.setAdapter(wallpaperAdapter);
                    wallpaperAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<WallpaperResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onItemClick(int position) {

       /* AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(this);
        AlertDialog builder=alertDialogBuilder.create();
                View dialogView= getLayoutInflater().inflate(R.layout.image_dialog,null);
                ImageView wallpaperImage, close;
                Button downloadNow,setHomeScreen;
                wallpaperImage=dialogView.findViewById(R.id.full_image);
                close=dialogView.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        builder.dismiss();

                    }
                });
                String originalUrl=dataList.get(position).getSrc().getLarge();
                Glide.with(getApplicationContext())
                        .load(originalUrl)
                        .into(wallpaperImage);
                builder.setView(dialogView);
                builder.setCancelable(true);
                builder.show();
                dialogView.cancelLongPress();
                downloadNow=dialogView.findViewById(R.id.download_now);
                setHomeScreen=dialogView.findViewById(R.id.set_home_screen);
                downloadNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(originalUrl);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        downloadManager.enqueue(request);
                        Toast.makeText(getApplicationContext(), "Downloading Start", Toast.LENGTH_SHORT).show();

                    }
                });

                setHomeScreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                        Bitmap bitmap  = ((BitmapDrawable)wallpaperImage.getDrawable()).getBitmap();
                        try {
                            wallpaperManager.setBitmap(bitmap);
                            Toast.makeText(getApplicationContext(), "Wallpaper Set", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
*/
        Intent intent = new Intent(getBaseContext(), FullImageActivity.class);
        intent.putExtra("imageUrl", dataList.get(position).getSrc().getLarge());
        startActivity(intent);

    }
}