//package com.stimednp.aplikasimoviecataloguesub4.roomdb;
//
//import android.app.Application;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.LiveData;
//
//import com.stimednp.aplikasimoviecataloguesub4.roommovies.Movies;
//import com.stimednp.aplikasimoviecataloguesub4.roommovies.MoviesDao;
//
//import java.util.List;
//
///**
// * Created by rivaldy on 8/4/2019.
// */
//
//public class MoviesViewModel extends AndroidViewModel {
//    private LiveData<List<Movies>> moviesLiveData;
//
//    public MoviesViewModel(@NonNull Application application) {
//        super(application);
//        MoviesRoomDatabase db = MoviesRoomDatabase.getDatabase(application);
//        MoviesDao moviesDao = db.moviesDao();
//        moviesLiveData = moviesDao.getAllMoviesVm();
//    }
//
//    public LiveData<List<Movies>> getMoviesList() {
//        return moviesLiveData;
//    }
//}
