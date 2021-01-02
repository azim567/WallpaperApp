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
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.viewHolder> {
    Context context;
    List<Wallpaper> imageList;
    RecyclerViewClickInterface recyclerViewClickInterface;

    public WallpaperAdapter(Context context, List<Wallpaper> imageList,RecyclerViewClickInterface  recyclerViewClickInterface) {
        this.context = context;
        this.imageList = imageList;
        this.recyclerViewClickInterface=recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view= LayoutInflater.from(context).inflate(R.layout.row_item,parent,false);
        viewHolder holder=new viewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {


        Glide.with(context)
                .load(imageList.get(position).getSrc().getMedium())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressbar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressbar.setVisibility(View.GONE);
                        return false;
                    }
                })

                .into(holder.imageView);





        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerViewClickInterface.onItemClick(position);

                /*AlertDialog builder=new AlertDialog.Builder(v.getRootView().getContext()).create();
                View dialogView= LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.image_dialog,null);
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
                String originalUrl=imageList.get(position).getSrc().getLarge();
                Glide.with(context)
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

                        DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(originalUrl);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        downloadManager.enqueue(request);
                        Toast.makeText(context, "Downloading Start", Toast.LENGTH_SHORT).show();

                    }
                });

                setHomeScreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
                        Bitmap bitmap  = ((BitmapDrawable)wallpaperImage.getDrawable()).getBitmap();
                        try {
                            wallpaperManager.setBitmap(bitmap);
                            Toast.makeText(context, "Wallpaper Set", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });*/
            }
        });


    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ProgressBar progressbar;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.wallpaper);
            progressbar=itemView.findViewById(R.id.progressBar);
        }
    }
}
