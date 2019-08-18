package com.stimednp.aplikasimoviecataloguesub4.myapi;

import com.stimednp.aplikasimoviecataloguesub4.mymodel.MoviesResponse;
import com.stimednp.aplikasimoviecataloguesub4.mymodel.TvShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by rivaldy on 8/3/2019.
 */

public interface APIMovieTv {
    @GET("discover/movie")
    Call<MoviesResponse> getMovieList(
            @Query("api_key") String apiKey,
            @Query("language") String language);

    @GET("discover/tv")
    Call<TvShowResponse> getTvShowList(
            @Query("api_key") String apiKey,
            @Query("language") String language);

    @GET("search/movie")
    Call<MoviesResponse> getMovieSearch(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String searchQuery);

    @GET("search/tv")
    Call<TvShowResponse> getTvShowSearch(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String searchQuery);
}
