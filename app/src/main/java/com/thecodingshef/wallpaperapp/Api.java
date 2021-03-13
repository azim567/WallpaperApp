package com.thecodingshef.wallpaperapp;

import com.thecodingshef.wallpaperapp.Model.WallpaperResponse;

import retrofit2.Call;

import retrofit2.http.GET;

import retrofit2.http.Header;
import retrofit2.http.Query;


public interface Api {

    @GET("curated")
    Call<WallpaperResponse> getWallpaper(
            @Header("Authorization") String credentials,
            @Query("page") int pageCount,
            @Query("per_page") int perPage
    );

    @GET("search")
    Call<WallpaperResponse> getSearch(
            @Header("Authorization") String credentials,
            @Query("query") String queryText
    );

}