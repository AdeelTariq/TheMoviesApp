package com.winterparadox.themovieapp.charts;

import com.winterparadox.themovieapp.arch.BasePresenter;
import com.winterparadox.themovieapp.common.beans.Chart;

public abstract class ChartsPresenter extends BasePresenter<ChartsView> {

    abstract void fetchChartData ();

    public abstract void onChartClicked (Chart chart);

    public abstract void saveState (int firstVisibleItem);
}
