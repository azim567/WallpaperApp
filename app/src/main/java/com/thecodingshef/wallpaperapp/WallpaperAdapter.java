package com.thecodingshef.wallpaperapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.thecodingshef.wallpaperapp.Model.Wallpaper;

import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.viewHolder> {
    private final Context mContext;
    private final List<Wallpaper> mImageList;
    private final RecyclerViewClickInterface mRecyclerViewClickInterface;

    public WallpaperAdapter(Context context, List<Wallpaper> imageList, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.mContext = context;
        this.mImageList = imageList;
        this.mRecyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(mContext).inflate(R.layout.row_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Glide.with(mContext)
                .load(mImageList.get(position).getSrc().getMedium())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.mProgressbar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.mProgressbar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private final ImageView mImageView;
        private final ProgressBar mProgressbar;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.wallpaper);
            mProgressbar = itemView.findViewById(R.id.progressBar);
            mImageView.setOnClickListener(view -> { if (null != mRecyclerViewClickInterface) mRecyclerViewClickInterface.onItemClick(getAdapterPosition());});
        }
    }
}
