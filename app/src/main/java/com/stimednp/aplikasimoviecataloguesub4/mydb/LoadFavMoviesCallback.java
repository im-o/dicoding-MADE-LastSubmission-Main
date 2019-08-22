package com.stimednp.aplikasimoviecataloguesub4.mydb;

import android.database.Cursor;

/**
 * Created by rivaldy on 8/19/2019.
 */

public interface LoadFavMoviesCallback {
    void preExecute();
    void postExecute(Cursor cursor);
//    void postExecute(ArrayList<MoviesModel> moviesModelList);
}
