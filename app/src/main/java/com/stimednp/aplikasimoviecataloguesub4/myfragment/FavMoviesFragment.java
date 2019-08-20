package com.stimednp.aplikasimoviecataloguesub4.myfragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.stimednp.aplikasimoviecataloguesub4.R;
import com.stimednp.aplikasimoviecataloguesub4.myactivity.DetailsMovieActivity;
import com.stimednp.aplikasimoviecataloguesub4.mydb.LoadMoviesmCallback;
import com.stimednp.aplikasimoviecataloguesub4.mydb.MovieHelper;
import com.stimednp.aplikasimoviecataloguesub4.mydbadapter.MoviesmAdapter;
import com.stimednp.aplikasimoviecataloguesub4.mydbentity.Moviesm;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavMoviesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoviesmCallback {
    private MoviesmAdapter moviesmAdapter;
    private RecyclerView rvMoviesm;
    private static final String EXTRA_STATE = "EXTRA_STATE";

    private MovieHelper movieHelper;
    ArrayList<Moviesm> moviesmList;


    private static final String TAG = NavMoviesFragment.class.getSimpleName();
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
        rvMoviesm = view.findViewById(R.id.rv_tab_movies_room);
        textViewEmpty = view.findViewById(R.id.tv_movies_empty);
        progressBarMovie = view.findViewById(R.id.progressbar_tab_movies_room);
        refreshLayoutMovie = view.findViewById(R.id.swipe_scroll_movie_room);

        rvMoviesm.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMoviesm.setHasFixedSize(true);

        movieHelper = new MovieHelper(this.getActivity());
        movieHelper.open();

        refreshLayoutMovie.setOnRefreshListener(this);

        if (savedInstanceState == null) {
            new LoadMoviesmAsync(movieHelper, this).execute();
        } else {
            ArrayList<Moviesm> moviesmList = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (moviesmList != null) {
                moviesmAdapter.setListMoviesm(moviesmList);
            }
        }

        callDataViewModel();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, moviesmAdapter.getmoviesmList());
        Toast.makeText(getActivity(), "SAVE" + outState, Toast.LENGTH_SHORT).show();
    }

    private void callDataViewModel() {
        moviesmList = movieHelper.getAllMovies();
        moviesmAdapter = new MoviesmAdapter(this.getActivity(), moviesmList);
//        moviesmAdapter.setListMoviesm(moviesmList);
        rvMoviesm.setAdapter(moviesmAdapter);
        if (moviesmList.size() < 1) {
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
        }
        progressBarMovie.setVisibility(View.GONE);


//        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
//        moviesViewModel.getMoviesList().observe(this, new Observer<List<Movies>>() {
//            @Override
//            public void onChanged(List<Movies> movies) {
//                moviesAdapter = new MoviesAdapter(getContext(), (ArrayList<Movies>) movies);
//                moviesAdapter.setMoviesList((ArrayList<Movies>) movies);
//                recyclerViewMovie.setAdapter(moviesAdapter);
//                if (movies.size() < 1) {
//                    textViewEmpty.setVisibility(View.VISIBLE);
//                } else {
//                    textViewEmpty.setVisibility(View.GONE);
//                }
//                progressBarMovie.setVisibility(View.GONE);
//            }
//        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (refreshLayoutMovie.isRefreshing()) {
//                    callDataViewModel();
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
    public void postExecute(ArrayList<Moviesm> moviesmList) {
        refreshLayoutMovie.setRefreshing(false);
        moviesmAdapter.setListMoviesm(moviesmList);
    }

    private static class LoadMoviesmAsync extends AsyncTask<Void, Void, ArrayList<Moviesm>> {
        private final WeakReference<MovieHelper> weakMovieHelper;
        private final WeakReference<LoadMoviesmCallback> weakCallback;

        LoadMoviesmAsync(MovieHelper movieHelper, LoadMoviesmCallback callback) {
            weakMovieHelper = new WeakReference<>(movieHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Moviesm> doInBackground(Void... voids) {
            return weakMovieHelper.get().getAllMovies();
        }

        @Override
        protected void onPostExecute(ArrayList<Moviesm> moviesms) {
            super.onPostExecute(moviesms);
            weakCallback.get().postExecute(moviesms);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getActivity(), "WORK FAV", Toast.LENGTH_SHORT).show();
//        if (data != null) {
//            if (requestCode == DetailsMovieActivity.REQUEST_ADD) {
//                if (resultCode == DetailsMovieActivity.RESULT_ADD) {
//                    Moviesm moviesm = data.getParcelableExtra(DetailsMovieActivity.EXTRA_MOVIE);
//                    moviesmAdapter.addItem(moviesm);
//                    rvMoviesm.smoothScrollToPosition(moviesmAdapter.getItemCount() - 1);
//                    Toast.makeText(getContext(), "Satu item berhasil ditambahkan", Toast.LENGTH_SHORT).show();
////                    showSnackbarMessage("Satu item berhasil ditambahkan");
//                }
//            }
//
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: HANCUUURRRR");
        movieHelper.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadMoviesmAsync(movieHelper, this).execute();
        rvMoviesm.setAdapter(moviesmAdapter);
        Log.d(TAG, "onResume: "+movieHelper);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }
}
