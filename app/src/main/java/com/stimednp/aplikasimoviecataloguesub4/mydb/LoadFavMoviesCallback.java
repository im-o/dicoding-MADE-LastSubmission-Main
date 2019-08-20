package com.stimednp.aplikasimoviecataloguesub4.mydb;

import com.stimednp.aplikasimoviecataloguesub4.mydbentity.FavMoviesModel;

import java.util.ArrayList;

/**
 * Created by rivaldy on 8/19/2019.
 */

public interface LoadFavMoviesCallback {
    void preExecute();
    void postExecute(ArrayList<FavMoviesModel> favMoviesModelList);
}
