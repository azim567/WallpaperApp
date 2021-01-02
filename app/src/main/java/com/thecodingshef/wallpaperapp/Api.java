package com.thecodingshef.wallpaperapp;

import retrofit2.Call;

import retrofit2.http.GET;

import retrofit2.http.Header;
import retrofit2.http.Query;


public interface Api {


    @GET("curated/?page=1&per_page=80")
    Call<WallpaperResponse> getWallpaper(

            @Header("Authorization") String credentials

    );

    @GET("search?")
    Call<WallpaperResponse> getSearch(

            @Header("Authorization") String credentials,
            @Query("query") String queryText
    );

}