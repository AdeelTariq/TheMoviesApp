package com.winterparadox.themovieapp.charts;

import com.winterparadox.themovieapp.arch.BaseView;
import com.winterparadox.themovieapp.common.beans.Chart;

import java.util.List;

public interface ChartsView extends BaseView {

    void showCharts (List<Chart> charts, int firstVisibleItem);

    List<String> getDefaultCharts ();
}
