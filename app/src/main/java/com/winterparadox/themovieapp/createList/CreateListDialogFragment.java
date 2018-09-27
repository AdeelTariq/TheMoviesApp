package com.winterparadox.themovieapp.createList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.winterparadox.themovieapp.App;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.arch.Navigator;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CreateListDialogFragment extends AppCompatDialogFragment implements
        CreateListDialogView {

    private static final String MOVIE = "movieId";
    @Inject CreateListDialogPresenter presenter;
    private long movieId;

    public static CreateListDialogFragment instance (long movieId) {
        CreateListDialogFragment userListDialogFragment = new CreateListDialogFragment ();
        Bundle args = new Bundle ();
        args.putLong (MOVIE, movieId);
        userListDialogFragment.setArguments (args);
        return userListDialogFragment;
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        movieId = getArguments ().getLong (MOVIE);

        ((App) getActivity ().getApplication ()).getAppComponent ().inject (this);

        presenter.attachView (this, (Navigator) getActivity ());
    }

    @Override
    public void onDestroy () {
        super.onDestroy ();
        presenter.detachView ();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder (getActivity (),
                R.style.Theme_Movies_Dialog);
        builder.setTitle (R.string.create_new_list);

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from (builder.getContext ())
                .inflate (R.layout.layout_name_input, null, false);

        TextInputEditText etName = view.findViewById (R.id.etName);

        builder.setView (view);

        builder.setPositiveButton (R.string.create,
                (d, v) -> presenter.createList (etName.getText ().toString (), movieId));
        builder.setNegativeButton (R.string.cancel, null);

        // Create the AlertDialog object and return it
        AlertDialog alertDialog = builder.create ();
        alertDialog.getWindow ().setBackgroundDrawable (
                getResources ().getDrawable (R.drawable.dialog_background));
        return alertDialog;
    }

    @Override
    public void showProgress () {

    }

    @Override
    public void hideProgress () {

    }

    @Override
    public void showMessage (String message) {

    }

    @Override
    public void showError (String message) {

    }
}
