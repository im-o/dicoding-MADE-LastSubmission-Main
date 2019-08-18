package com.stimednp.aplikasimoviecataloguesub4.roomdb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.stimednp.aplikasimoviecataloguesub4.roomtvshow.TvShow;
import com.stimednp.aplikasimoviecataloguesub4.roomtvshow.TvShowDao;

import java.util.List;

/**
 * Created by rivaldy on 8/4/2019.
 */

public class TvShowViewModel extends AndroidViewModel {
    private LiveData<List<TvShow>> tvShowLiveData;

    public TvShowViewModel(@NonNull Application application) {
        super(application);
        TvShowRoomDatabase db = TvShowRoomDatabase.getDatabase(application);
        TvShowDao tvShowDao = db.tvShowDao();
        tvShowLiveData = tvShowDao.getAllTvShowVm();
    }

    public LiveData<List<TvShow>> getTvShowList() {
        return tvShowLiveData;
    }
}
