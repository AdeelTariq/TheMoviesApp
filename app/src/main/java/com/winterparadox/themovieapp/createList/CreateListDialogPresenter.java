package com.winterparadox.themovieapp.createList;

import com.winterparadox.themovieapp.arch.BasePresenter;

public abstract class CreateListDialogPresenter extends BasePresenter<CreateListDialogView> {

    public abstract void createList (String name, long movieId);
}
