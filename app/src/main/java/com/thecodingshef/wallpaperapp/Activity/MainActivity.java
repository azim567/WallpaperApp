package com.thecodingshef.wallpaperapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.thecodingshef.wallpaperapp.R;
import com.thecodingshef.wallpaperapp.RecyclerViewClickInterface;
import com.thecodingshef.wallpaperapp.RetrofitClient;
import com.thecodingshef.wallpaperapp.Model.Wallpaper;
import com.thecodingshef.wallpaperapp.WallpaperAdapter;
import com.thecodingshef.wallpaperapp.Model.WallpaperResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickInterface {

    private RecyclerView mImagesRecyclerView;
    private final String API_KEY = "563492ad6f91700001000001408647ec35334e55b9aae2cbadca91fd";
    private int mPageCount = 1;
    private static final int PER_PAGE = 80;
    private List<Wallpaper> mImagesDataList = new ArrayList<>();
    private NestedScrollView mNestedScrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
        mNestedScrollview = findViewById(R.id.nestedScrollview);
        setupPagination(true);
    }

    private void initRecyclerView() {
        mImagesRecyclerView = findViewById(R.id.recycler);
        mImagesRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mImagesRecyclerView.setLayoutManager(gridLayoutManager);
        getData(mPageCount);
    }

    private void setupPagination(boolean isPaginationAllowed) {
        if (isPaginationAllowed) {
            mNestedScrollview.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    getData(++mPageCount);
                    Toast.makeText(getApplicationContext(), mPageCount + "", Toast.LENGTH_SHORT).show();
                }
            });
        } else
            mNestedScrollview.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            });
    }

    private void getSearchedData(String query) {
        Call<WallpaperResponse> wallpaperResponseCall = RetrofitClient
                .getInstance()
                .getApi()
                .getSearch(API_KEY, query);
        wallpaperResponseCall.enqueue(new Callback<WallpaperResponse>() {
            @Override
            public void onResponse(@NotNull Call<WallpaperResponse> call, @NotNull Response<WallpaperResponse> response) {
                if (response.isSuccessful()) {
                    setupPagination(false);
                    if (!mImagesDataList.isEmpty()) mImagesDataList.clear();
                    mImagesDataList = null != response.body() ? response.body().getPhotosList() : new ArrayList<>();
                    WallpaperAdapter wallpaperAdapter = new WallpaperAdapter(getApplicationContext(), mImagesDataList, MainActivity.this);
                    mImagesRecyclerView.setAdapter(wallpaperAdapter);
                } else Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NotNull Call<WallpaperResponse> call, @NotNull Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData(int pageCount) {
        Call<WallpaperResponse> wallpaperResponseCall = RetrofitClient
                .getInstance()
                .getApi()
                .getWallpaper(API_KEY, pageCount, PER_PAGE);
        wallpaperResponseCall.enqueue(new Callback<WallpaperResponse>() {
            @Override
            public void onResponse(@NotNull Call<WallpaperResponse> call, @NotNull Response<WallpaperResponse> response) {
                WallpaperResponse wallpaperResponse = response.body();
                if (response.isSuccessful() && null != wallpaperResponse) {
                    mImagesDataList.addAll(wallpaperResponse.getPhotosList());
                    WallpaperAdapter wallpaperAdapter = new WallpaperAdapter(getApplication(), mImagesDataList, MainActivity.this);
                    mImagesRecyclerView.setAdapter(wallpaperAdapter);
                    wallpaperAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<WallpaperResponse> call, @NotNull Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_item, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        if (searchItem != null) {
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (query.isEmpty()) getData(1);
                    else getSearchedData(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (newText.isEmpty()) getData(1);
                    return true;
                }
            });
            searchView.setOnCloseListener(() ->{
                getData(1);
            return true;});
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(), FullImageActivity.class);
        intent.putExtra("imageUrl", mImagesDataList.get(position).getSrc().getLarge());
        startActivity(intent);
    }
}