package com.winterparadox.themovieapp.charts;

import com.winterparadox.themovieapp.common.base.BaseView;
import com.winterparadox.themovieapp.common.beans.Chart;

import java.util.List;

public interface ChartsView extends BaseView {

    void showCharts (List<Chart> charts);
}
