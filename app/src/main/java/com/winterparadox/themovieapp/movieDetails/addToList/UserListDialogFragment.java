package com.winterparadox.themovieapp.movieDetails.addToList;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.winterparadox.themovieapp.App;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.UserList;

import java.util.ArrayList;

import javax.inject.Inject;

public class UserListDialogFragment extends AppCompatDialogFragment implements
        UserListDialogView, DialogInterface.OnMultiChoiceClickListener {

    private static final String LISTS = "lists";
    private static final String MOVIE = "movie";
    private boolean[] checked;
    private ArrayList<UserList> lists;
    private CharSequence[] labels;

    @Inject UserListDialogPresenter presenter;
    private int movieId;

    public static UserListDialogFragment instance (int movieId, ArrayList<UserList> lists) {
        UserListDialogFragment userListDialogFragment = new UserListDialogFragment ();
        Bundle args = new Bundle ();
        args.putSerializable (LISTS, lists);
        args.putInt (MOVIE, movieId);
        userListDialogFragment.setArguments (args);
        return userListDialogFragment;
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        ((App) getActivity ().getApplication ()).getAppComponent ().inject (this);

        lists = (ArrayList<UserList>) getArguments ()
                .getSerializable (LISTS);
        movieId = getArguments ().getInt (MOVIE);

        labels = new CharSequence[lists.size ()];
        checked = new boolean[lists.size ()];

        for ( int i = 0, listsSize = lists.size (); i < listsSize; i++ ) {
            UserList list = lists.get (i);
            labels[i] = list.name;
            checked[i] = list.isAdded;
        }

        presenter.attachView (this, (Navigator) getActivity ());
    }

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder (getActivity ());
        builder.setTitle (R.string.add_to_list);
        builder.setMultiChoiceItems (labels, checked, this);

        builder.setPositiveButton (R.string.ok, null);
        builder.setNeutralButton (R.string.create_new_list, null);

        // Create the AlertDialog object and return it
        return builder.create ();
    }

    @Override
    public void onClick (DialogInterface dialogInterface, int i, boolean isChecked) {
        if ( isChecked ) {
            presenter.addToList (movieId, lists.get (i));
        } else {
            presenter.removeFromList (movieId, lists.get (i));
        }
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
