package com.stimednp.aplikasimoviecataloguesub4.myfragment;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.stimednp.aplikasimoviecataloguesub4.R;
import com.stimednp.aplikasimoviecataloguesub4.roomdb.MoviesViewModel;
import com.stimednp.aplikasimoviecataloguesub4.roommovies.Movies;
import com.stimednp.aplikasimoviecataloguesub4.roommovies.MoviesAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavMoviesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = NavMoviesFragment.class.getSimpleName();
    private RecyclerView recyclerViewMovie;
    private MoviesAdapter moviesAdapter;
    private SwipeRefreshLayout refreshLayoutMovie;
    private ProgressBar progressBarMovie;
    private MoviesViewModel moviesViewModel;
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
//        setHasOptionsMenu(true);// calll fung menu
        textViewEmpty = view.findViewById(R.id.tv_movies_empty);
        progressBarMovie = view.findViewById(R.id.progressbar_tab_movies_room);
        refreshLayoutMovie = view.findViewById(R.id.swipe_scroll_movie_room);
        recyclerViewMovie = view.findViewById(R.id.rv_tab_movies_room);
        recyclerViewMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayoutMovie.setOnRefreshListener(this);

        callDataViewModel();

    }

    private void callDataViewModel() {
        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        moviesViewModel.getMoviesList().observe(this, new Observer<List<Movies>>() {
            @Override
            public void onChanged(List<Movies> movies) {
                moviesAdapter = new MoviesAdapter(getContext(), (ArrayList<Movies>) movies);
                moviesAdapter.setMoviesList((ArrayList<Movies>) movies);
                recyclerViewMovie.setAdapter(moviesAdapter);
                if (movies.size() < 1) {
                    textViewEmpty.setVisibility(View.VISIBLE);
                } else {
                    textViewEmpty.setVisibility(View.GONE);
                }
                progressBarMovie.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (refreshLayoutMovie.isRefreshing()) {
                    callDataViewModel();
                    refreshLayoutMovie.setRefreshing(false);
                }
            }
        }, 1000);
    }
}
