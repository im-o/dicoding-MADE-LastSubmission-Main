package com.stimednp.aplikasimoviecataloguesub4.mymodel;

import java.util.List;

/**
 * Created by rivaldy on 8/3/2019.
 */

public class MoviesResponse {
    private List<MovieItems> results;

    public MoviesResponse(List<MovieItems> results) {
        this.results = results;
    }

    List<MovieItems> getResults() {
        return results;
    }

}
