package com.stimednp.aplikasimoviecataloguesub4.mymodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.stimednp.aplikasimoviecataloguesub4.myapi.APIClientMovieTv;
import com.stimednp.aplikasimoviecataloguesub4.myapi.APIMovieTv;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.stimednp.aplikasimoviecataloguesub4.addingmethod.Constant.API_KEY;

/**
 * Created by rivaldy on 8/4/2019.
 */

public class MainViewModel extends ViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();
    private static final String LANGUAGE = "en-US";

    private MutableLiveData<ArrayList<MovieItems>> listMovies = new MutableLiveData<>();
    private MutableLiveData<ArrayList<TvShowItems>> listTvShow = new MutableLiveData<>();

    public void setListMovies() {
        APIMovieTv apiMovieTv = APIClientMovieTv.getClient().create(APIMovieTv.class);
        Call<MoviesResponse> call = apiMovieTv.getMovieList(API_KEY, LANGUAGE);
        final ArrayList<MovieItems> movieItems = new ArrayList<>();
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                List<MovieItems> movieItemList = null;
                if (response.body() != null) {
                    movieItemList = response.body().getResults();
                }
                try {
                    if (movieItemList != null) {
                        movieItems.addAll(movieItemList);
                    }
                    listMovies.postValue(movieItems);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "ERROR : " + t.getMessage());
            }
        });
    }

    public void setListTvShow() {
        APIMovieTv apiMovieTv = APIClientMovieTv.getClient().create(APIMovieTv.class);
        Call<TvShowResponse> call = apiMovieTv.getTvShowList(API_KEY, LANGUAGE);
        final ArrayList<TvShowItems> tvShowItems = new ArrayList<>();
        call.enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowResponse> call, @NonNull Response<TvShowResponse> response) {
                List<TvShowItems> movieItemList = null;
                if (response.body() != null) {
                    movieItemList = response.body().getResults();
                }
                try {
                    if (movieItemList != null) {
                        tvShowItems.addAll(movieItemList);
                    }
                    listTvShow.postValue(tvShowItems);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShowResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "ERROR : " + t.getMessage());
            }
        });
    }

    public void setSeacrhMovies(String strQuery) {
        APIMovieTv apiMovieTv = APIClientMovieTv.getClient().create(APIMovieTv.class);
        Call<MoviesResponse> call = apiMovieTv.getMovieSearch(API_KEY, LANGUAGE, strQuery);
        final ArrayList<MovieItems> movieItems = new ArrayList<>();
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                List<MovieItems> movieItemList = null;
                if (response.body() != null) {
                    movieItemList = response.body().getResults();
                }
                try {
                    if (movieItemList != null) {
                        movieItems.addAll(movieItemList);
                    }
                    listMovies.postValue(movieItems);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "ERROR : " + t.getMessage());
            }
        });
    }

    public void setSearchTvShow(String strQuery) {
        APIMovieTv apiMovieTv = APIClientMovieTv.getClient().create(APIMovieTv.class);
        Call<TvShowResponse> call = apiMovieTv.getTvShowSearch(API_KEY, LANGUAGE, strQuery);
        final ArrayList<TvShowItems> tvShowItems = new ArrayList<>();
        call.enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowResponse> call, @NonNull Response<TvShowResponse> response) {
                List<TvShowItems> movieItemList = null;
                if (response.body() != null) {
                    movieItemList = response.body().getResults();
                }
                try {
                    if (movieItemList != null) {
                        tvShowItems.addAll(movieItemList);
                    }
                    listTvShow.postValue(tvShowItems);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShowResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "ERROR : " + t.getMessage());
            }
        });
    }

    public MutableLiveData<ArrayList<MovieItems>> getListMovies() {
        return listMovies;
    }

    public MutableLiveData<ArrayList<TvShowItems>> getListTvShow() {
        return listTvShow;
    }
}
