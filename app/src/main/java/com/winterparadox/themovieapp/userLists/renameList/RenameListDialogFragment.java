package com.winterparadox.themovieapp.userLists.renameList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.winterparadox.themovieapp.App;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.UserList;

import javax.inject.Inject;

public class RenameListDialogFragment extends AppCompatDialogFragment implements
        RenameListDialogView {

    private static final String LIST = "list";
    @Inject RenameListDialogPresenter presenter;

    private UserList userList;

    public static RenameListDialogFragment instance (UserList userList) {
        RenameListDialogFragment userListDialogFragment = new RenameListDialogFragment ();
        Bundle args = new Bundle ();
        args.putSerializable (LIST, userList);
        userListDialogFragment.setArguments (args);
        return userListDialogFragment;
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        userList = (UserList) getArguments ().getSerializable (LIST);

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
        builder.setTitle (R.string.rename);

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from (builder.getContext ())
                .inflate (R.layout.layout_name_input, null, false);

        TextInputEditText etName = view.findViewById (R.id.etName);
        etName.setText (userList.name);
        etName.setSelection (0, userList.name.length ());

        builder.setView (view);

        builder.setPositiveButton (R.string.done,
                (d, v) -> presenter.renameList (etName.getText ().toString (), userList));
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
