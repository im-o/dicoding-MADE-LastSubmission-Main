package com.stimednp.aplikasimoviecataloguesub4.mymodel;

import java.util.List;

/**
 * Created by rivaldy on 8/3/2019.
 */

public class TvShowResponse {
    private List<TvShowItems> results;

    public TvShowResponse(List<TvShowItems> results) {
        this.results = results;
    }

    List<TvShowItems> getResults() {
        return results;
    }
}
