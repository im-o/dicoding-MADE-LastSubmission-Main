package com.stimednp.aplikasimoviecataloguesub4.mymodel;

import java.util.List;

/**
 * Created by rivaldy on 8/3/2019.
 */

public class MoviesResponse {
    private int page;
    private List<MovieItems> results;
    private int total_results;
    private int total_pages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    List<MovieItems> getResults() {
        return results;
    }

    public void setResults(List<MovieItems> results) {
        this.results = results;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }
}
