package com.winterparadox.themovieapp.common.room;

import com.winterparadox.themovieapp.common.beans.Chart;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import io.reactivex.Flowable;
import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

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
    Single<Chart> getChart (long id);
}
