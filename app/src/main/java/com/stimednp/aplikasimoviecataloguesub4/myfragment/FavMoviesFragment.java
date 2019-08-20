package com.stimednp.aplikasimoviecataloguesub4.myfragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.stimednp.aplikasimoviecataloguesub4.R;
import com.stimednp.aplikasimoviecataloguesub4.myactivity.DetailsMovieActivity;
import com.stimednp.aplikasimoviecataloguesub4.mydb.FavMoviesHelper;
import com.stimednp.aplikasimoviecataloguesub4.mydb.LoadFavMoviesCallback;
import com.stimednp.aplikasimoviecataloguesub4.mydbadapter.FavMoviesAdapter;
import com.stimednp.aplikasimoviecataloguesub4.mydbentity.FavMoviesModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavMoviesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LoadFavMoviesCallback, FavMoviesAdapter.OnItemClickCallback {
    public static final String TAG = FavMoviesFragment.class.getSimpleName();
    private FavMoviesAdapter favMoviesAdapter;
    private RecyclerView recyclervFavMovies;
    private static final String EXTRA_STATE = "EXTRA_STATE";

    private FavMoviesHelper favMoviesHelper;
    private ArrayList<FavMoviesModel> favMoviesModelList;

    private SwipeRefreshLayout refreshLayoutMovie;
    private ProgressBar progressBarMovie;
    private TextView textViewEmpty;

    public FavMoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fav_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclervFavMovies = view.findViewById(R.id.rv_tab_movies_room);
        textViewEmpty = view.findViewById(R.id.tv_movies_empty);
        progressBarMovie = view.findViewById(R.id.progressbar_tab_movies_room);
        refreshLayoutMovie = view.findViewById(R.id.swipe_scroll_movie_room);
        recyclervFavMovies.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclervFavMovies.setHasFixedSize(true);
        favMoviesHelper = new FavMoviesHelper(this.getActivity());

        favMoviesHelper.open();
        favMoviesModelList = favMoviesHelper.getAllMovies();
        favMoviesAdapter = new FavMoviesAdapter(this.getActivity());
        recyclervFavMovies.setAdapter(favMoviesAdapter);

        if (savedInstanceState == null) {
            new LoadMoviesmAsync(favMoviesHelper, this).execute();
        } else {
            ArrayList<FavMoviesModel> favMoviesModelList = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (favMoviesModelList != null) {
                favMoviesAdapter.setListMoviesm(favMoviesModelList);
            }
        }

        favMoviesAdapter.setOnItemClickCallback(this);
        refreshLayoutMovie.setOnRefreshListener(this);
        //check
        checkingMovieList();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, favMoviesAdapter.getmoviesmList());
    }

    private void checkingMovieList() {
        recyclervFavMovies.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_transition_animation));
        if (favMoviesModelList.size() < 1) {
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
        }
        progressBarMovie.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (refreshLayoutMovie.isRefreshing()) {
                    checkingMovieList();
                    refreshLayoutMovie.setRefreshing(false);
                }
            }
        }, 1000);
    }

    @Override
    public void preExecute() {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshLayoutMovie.setRefreshing(true);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<FavMoviesModel> favMoviesModelList) {
        refreshLayoutMovie.setRefreshing(false);
        favMoviesAdapter.setListMoviesm(favMoviesModelList);
    }

    @Override
    public void onItemClicked(FavMoviesModel favMoviesModel) {
        Intent intent = new Intent(getActivity(), DetailsMovieActivity.class);
        intent.putExtra(DetailsMovieActivity.EXTRA_WHERE_FROM, TAG);
        intent.putExtra(DetailsMovieActivity.EXTRA_MOVIE, favMoviesModel);
        startActivityForResult(intent, DetailsMovieActivity.REQUEST_ADD);
    }

    private static class LoadMoviesmAsync extends AsyncTask<Void, Void, ArrayList<FavMoviesModel>> {
        private final WeakReference<FavMoviesHelper> weakMovieHelper;
        private final WeakReference<LoadFavMoviesCallback> weakCallback;

        LoadMoviesmAsync(FavMoviesHelper favMoviesHelper, LoadFavMoviesCallback callback) {
            weakMovieHelper = new WeakReference<>(favMoviesHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<FavMoviesModel> doInBackground(Void... voids) {
            return weakMovieHelper.get().getAllMovies();
        }

        @Override
        protected void onPostExecute(ArrayList<FavMoviesModel> favMoviesModels) {
            super.onPostExecute(favMoviesModels);
            weakCallback.get().postExecute(favMoviesModels);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == DetailsMovieActivity.REQUEST_ADD) {
                if (resultCode == DetailsMovieActivity.RESULT_ADD) {
                    FavMoviesModel favMoviesModel = data.getParcelableExtra(DetailsMovieActivity.EXTRA_MOVIE);
                    favMoviesAdapter.addItem(favMoviesModel);
                    recyclervFavMovies.smoothScrollToPosition(favMoviesAdapter.getItemCount() - 1);
                    recyclervFavMovies.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_transition_animation));
                }
                if (resultCode == DetailsMovieActivity.RESULT_DELETE) {
                    favMoviesAdapter.removeItem(0);
                    recyclervFavMovies.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_transition_animation));
                    if (favMoviesAdapter.getItemCount() == 0) {
                        textViewEmpty.setVisibility(View.VISIBLE);
                    }
                }
                new LoadMoviesmAsync(favMoviesHelper, this).execute();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        favMoviesHelper.close();
    }
}
