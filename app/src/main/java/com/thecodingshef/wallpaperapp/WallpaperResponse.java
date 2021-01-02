package com.thecodingshef.wallpaperapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WallpaperResponse {

    @SerializedName("photos")
    private List<Wallpaper> photosList;

    public WallpaperResponse(List<Wallpaper> photosList) {
        this.photosList = photosList;
    }

    public List<Wallpaper> getPhotosList() {
        return photosList;
    }

    public void setPhotosList(List<Wallpaper> photosList) {
        this.photosList = photosList;
    }
}
