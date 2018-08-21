package com.winterparadox.themovieapp.personDetails;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.GlideApp;
import com.winterparadox.themovieapp.common.UiUtils;
import com.winterparadox.themovieapp.common.beans.Image;
import com.winterparadox.themovieapp.common.views.TransitionNames;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.IMAGE;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.LARGE_PROFILE;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageItemHolder> {


    private static final float HEIGHT = 200;
    private List<Image> items;
    private ClickListener listener;

    public ImagesAdapter (ClickListener listener) {
        this.listener = listener;
    }

    public void setItems (List<Image> items) {
        this.items = items;
        notifyDataSetChanged ();
    }

    @NonNull
    @Override
    public ImageItemHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int type) {
        LayoutInflater inflater = LayoutInflater.from (viewGroup.getContext ());
        View view1 = inflater.inflate (R.layout.item_image, viewGroup, false);
        return new ImageItemHolder (view1);
    }

    @Override
    public void onBindViewHolder (@NonNull ImageItemHolder itemHolder, int i) {

        Image image = items.get (i);

        itemHolder.image.getLayoutParams ().width = (int) UiUtils.dpToPx (HEIGHT * image
                .aspectRatio);

        GlideApp.with (itemHolder.itemView)
                .load (Uri.parse (IMAGE + LARGE_PROFILE + image.filePath))
                .placeholder (R.drawable.ic_fallback_image)
                .into (itemHolder.image);

        itemHolder.image.setTransitionName (TransitionNames.PERSON_IMAGE + image.id);

        itemHolder.itemView.setOnClickListener (
                v -> listener.onImageClick (image, itemHolder.image));

    }

    @Override
    public int getItemCount () {
        return items == null ? 0 : items.size ();
    }

    static class ImageItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image) ImageView image;

        ImageItemHolder (@NonNull View itemView) {
            super (itemView);
            ButterKnife.bind (this, itemView);
        }
    }

    public interface ClickListener {
        void onImageClick (Image image, View element);
    }
}
