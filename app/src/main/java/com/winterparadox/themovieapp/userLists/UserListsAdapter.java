package com.winterparadox.themovieapp.userLists;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.GlideApp;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.views.GradientColorFilterTransformation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.IMAGE;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.MEDIUM_BACKDROP;

public class UserListsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final RequestOptions requestOptions;
    private List<Object> items;
    private ClickListener listener;

    UserListsAdapter (ClickListener listener) {
        this.listener = listener;

        requestOptions = new RequestOptions ()
                .transforms (new CenterCrop (), new GradientColorFilterTransformation ());

    }

    void setItems (List<UserList> userLists) {
        this.items = new ArrayList<> ();
        this.items.addAll (userLists);
        notifyDataSetChanged ();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int type) {
        LayoutInflater inflater = LayoutInflater.from (viewGroup.getContext ());

        View view1 = inflater.inflate (R.layout.item_chart, viewGroup, false);
        return new ChartItemHolder (view1);
    }

    @Override
    public void onBindViewHolder (@NonNull RecyclerView.ViewHolder itemHolder, int i) {
        UserList userList = (UserList) items.get (i);
        bindChart (((ChartItemHolder) itemHolder), userList);
    }

    private void bindChart (ChartItemHolder itemHolder, UserList userList) {
        GlideApp.with (itemHolder.itemView)
                .load (Uri.parse (IMAGE + MEDIUM_BACKDROP + userList.backDropPath))
                .apply (requestOptions)
                .into (itemHolder.thumbnail);

        itemHolder.title.setText (userList.name);

        itemHolder.itemView.setOnClickListener (v -> listener.onUserListClick (userList));
        itemHolder.itemView.setOnLongClickListener (v -> {
            listener.onUserListLongClick (userList, v);
            return true;
        });
    }

    @Override
    public int getItemCount () {
        return items == null ? 0 : items.size ();
    }

    static class ChartItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail) ImageView thumbnail;
        @BindView(R.id.name) TextView title;

        ChartItemHolder (@NonNull View itemView) {
            super (itemView);
            ButterKnife.bind (this, itemView);
        }
    }

    interface ClickListener {
        void onUserListClick (UserList list);

        void onUserListLongClick (UserList list, View view);
    }
}
