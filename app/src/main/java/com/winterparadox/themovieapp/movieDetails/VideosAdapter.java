package com.winterparadox.themovieapp.movieDetails;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.GlideApp;
import com.winterparadox.themovieapp.common.beans.VideoItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoItemHolder> {


    private final RequestOptions requestOptionsBackDrop;
    private List<VideoItem> items;

    public VideosAdapter () {

        requestOptionsBackDrop = new RequestOptions ()
                .transforms (new CenterCrop ()
                        //                        , new GradientColorFilterTransformation ()
                );

    }

    public void setItems (List<VideoItem> items) {
        this.items = items;
        notifyDataSetChanged ();
    }

    @NonNull
    @Override
    public VideoItemHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int type) {
        LayoutInflater inflater = LayoutInflater.from (viewGroup.getContext ());
        View view1 = inflater.inflate (R.layout.item_video,
                viewGroup, false);
        return new VideoItemHolder (view1);
    }

    @Override
    public void onBindViewHolder (@NonNull VideoItemHolder itemHolder, int i) {

        VideoItem videoItem = items.get (i);

        GlideApp.with (itemHolder.itemView)
                .load (Uri.parse (String.format (VideoItem.YOUTUBE_THUMB, videoItem.key)))
                .placeholder (R.drawable.ic_fallback_poster)
                .apply (requestOptionsBackDrop)
                .into (itemHolder.thumbnail);

        itemHolder.title.setText (videoItem.name);

        itemHolder.itemView.setOnClickListener (v -> {
            Context context = v.getContext ();
            Intent intent = new Intent (Intent.ACTION_VIEW);
            intent.setData (Uri.parse (String.format (VideoItem.YOUTUBE_VIDEO, videoItem.key)));
            context.startActivity (intent);
        });

    }

    @Override
    public int getItemCount () {
        return items == null ? 0 : items.size ();
    }

    static class VideoItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail) ImageView thumbnail;
        @BindView(R.id.name) TextView title;

        VideoItemHolder (@NonNull View itemView) {
            super (itemView);
            ButterKnife.bind (this, itemView);
        }
    }
}
