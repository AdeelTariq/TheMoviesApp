package com.winterparadox.themovieapp.userLists;

import com.winterparadox.themovieapp.arch.BaseView;
import com.winterparadox.themovieapp.common.beans.UserList;

import java.util.List;

public interface UserListsView extends BaseView {

    void showCharts (List<UserList> userLists, int firstVisibleItem);

    List<String> getDefaultLists ();
}
