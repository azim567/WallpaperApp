package com.thecodingshef.wallpaperapp.Model;

import com.google.gson.annotations.SerializedName;
import com.thecodingshef.wallpaperapp.Model.Wallpaper;

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
