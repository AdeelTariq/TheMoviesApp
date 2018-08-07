package com.winterparadox.themovieapp.common.views;

import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.winterparadox.themovieapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ErrorViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.error) public TextView error;
    @BindView(R.id.retry) public MaterialButton retry;

    public ErrorViewHolder (@NonNull View itemView, OnClickListener listener) {
        super (itemView);
        ButterKnife.bind (this, itemView);

        retry.setOnClickListener (v -> listener.retry ());
    }


    public interface OnClickListener {
        void retry ();
    }
}
