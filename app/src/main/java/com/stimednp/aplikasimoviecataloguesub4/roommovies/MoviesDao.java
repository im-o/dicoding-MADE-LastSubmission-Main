package com.stimednp.aplikasimoviecataloguesub4.roommovies;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Created by rivaldy on 8/4/2019.
 */
@Dao
public interface MoviesDao {
    @Insert
    void insert(Movies movies);

    @Query("SELECT * FROM "+Movies.TABLE_NAME+" ORDER BY "+Movies.COLUMN_TITLE)
    LiveData<List<Movies>> getAllMoviesVm();

    @Query("DELETE FROM movies WHERE title= :title_movie")
    void deleteByTitle(String title_movie);

    @Query("SELECT COUNT(*) FROM "+Movies.TABLE_NAME)
    int count();

    @Query("SELECT * FROM "+Movies.TABLE_NAME)
    Cursor selectAll();

    @Query("SELECT * FROM "+Movies.TABLE_NAME+" WHERE "+Movies.COLUMN_ID+" = :id")
    Cursor selectById(int id);
}
