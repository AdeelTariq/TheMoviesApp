package com.winterparadox.themovieapp.common.views;

import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.view.View;
import android.widget.TextView;

import com.winterparadox.themovieapp.R;
import com.woxthebox.draglistview.DragItemAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ErrorViewHolder extends DragItemAdapter.ViewHolder {

    @BindView(R.id.error) public TextView error;
    @BindView(R.id.retry) public MaterialButton retry;

    public ErrorViewHolder (@NonNull View itemView, OnClickListener listener) {
        this (itemView, listener, null);

    }

    public ErrorViewHolder (View view0, OnClickListener retryListener, String btnText) {
        super (view0, R.id.error, false);
        ButterKnife.bind (this, itemView);

        retry.setOnClickListener (v -> retryListener.onClick ());
        if ( btnText != null ) {
            retry.setText (btnText);
        }
    }


    public interface OnClickListener {
        void onClick ();
    }
}
