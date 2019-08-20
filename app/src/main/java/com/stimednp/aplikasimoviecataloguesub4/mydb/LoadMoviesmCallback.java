package com.stimednp.aplikasimoviecataloguesub4.mydb;

import com.stimednp.aplikasimoviecataloguesub4.mydbentity.Moviesm;

import java.util.ArrayList;

/**
 * Created by rivaldy on 8/19/2019.
 */

public interface LoadMoviesmCallback {
    void preExecute();
    void postExecute(ArrayList<Moviesm> moviesmList);
}
