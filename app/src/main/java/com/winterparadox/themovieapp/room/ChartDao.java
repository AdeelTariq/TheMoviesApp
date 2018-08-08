package com.winterparadox.themovieapp.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;

import com.winterparadox.themovieapp.common.beans.Chart;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
public interface ChartDao {

    @Query("SELECT * FROM Chart")
    Flowable<List<Chart>> getCharts ();

    @Insert(onConflict = IGNORE)
    Long insert (Chart chart);


    @Insert(onConflict = REPLACE)
    Long update (Chart chart);

    @Query("SELECT * FROM Chart WHERE id = :id")
    Single<Chart> getChart (int id);
}
