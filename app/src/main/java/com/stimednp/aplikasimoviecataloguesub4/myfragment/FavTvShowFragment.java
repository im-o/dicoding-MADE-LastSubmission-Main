package com.stimednp.aplikasimoviecataloguesub4.myfragment;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.stimednp.aplikasimoviecataloguesub4.R;
import com.stimednp.aplikasimoviecataloguesub4.roomdb.TvShowViewModel;
import com.stimednp.aplikasimoviecataloguesub4.roomtvshow.TvShow;
import com.stimednp.aplikasimoviecataloguesub4.roomtvshow.TvShowAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavTvShowFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerViewTvShow;
    private TvShowAdapter tvShowAdapter;
    private SwipeRefreshLayout refreshLayoutMovie;
    private ProgressBar progressBarMovie;
    private TextView textViewEmpty;

    public FavTvShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fav_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setHasOptionsMenu(true);// calll fung menu
        textViewEmpty = view.findViewById(R.id.tv_tvshow_empty);
        progressBarMovie = view.findViewById(R.id.progressbar_tab_tvshow_room);
        refreshLayoutMovie = view.findViewById(R.id.swipe_scroll_tvshow_room);
        recyclerViewTvShow = view.findViewById(R.id.rv_tab_tvshow_room);

        recyclerViewTvShow.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayoutMovie.setOnRefreshListener(this);

        refreshLayoutMovie.setOnRefreshListener(this);
        callDataViewModel();
    }

    private void callDataViewModel() {
        TvShowViewModel tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
        tvShowViewModel.getTvShowList().observe(this, new Observer<List<TvShow>>() {
            @Override
            public void onChanged(List<TvShow> tvShows) {
                tvShowAdapter = new TvShowAdapter(getContext(), (ArrayList<TvShow>) tvShows);
                tvShowAdapter.setTvshowList((ArrayList<TvShow>) tvShows);
                recyclerViewTvShow.setAdapter(tvShowAdapter);
                if (tvShows.size() < 1) {
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

    /**

     fdfdfdfdf
     aassas
     package com.stimednp.aplikasimoviecataloguesub4.myfragment;


     import android.os.AsyncTask;
     import android.os.Bundle;
     import android.os.Handler;
     import android.util.Log;
     import android.view.LayoutInflater;
     import android.view.Menu;
     import android.view.MenuInflater;
     import android.view.View;
     import android.view.ViewGroup;
     import android.widget.ProgressBar;
     import android.widget.RelativeLayout;
     import android.widget.TextView;
     import android.widget.Toast;

     import androidx.annotation.NonNull;
     import androidx.annotation.Nullable;
     import androidx.appcompat.widget.SearchView;
     import androidx.core.content.ContextCompat;
     import androidx.fragment.app.Fragment;
     import androidx.fragment.app.FragmentActivity;
     import androidx.lifecycle.Observer;
     import androidx.lifecycle.ViewModelProviders;
     import androidx.recyclerview.widget.LinearLayoutManager;
     import androidx.recyclerview.widget.RecyclerView;
     import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

     import com.google.android.material.snackbar.Snackbar;
     import com.stimednp.aplikasimoviecataloguesub4.R;
     import com.stimednp.aplikasimoviecataloguesub4.adapter.MovieItemsAdapter;
     import com.stimednp.aplikasimoviecataloguesub4.addingmethod.AllMyStrings;
     import com.stimednp.aplikasimoviecataloguesub4.addingmethod.CheckNetwork;
     import com.stimednp.aplikasimoviecataloguesub4.mydb.LoadMoviesmCallback;
     import com.stimednp.aplikasimoviecataloguesub4.mydb.MovieHelper;
     import com.stimednp.aplikasimoviecataloguesub4.mydbadapter.MoviesmAdapter;
     import com.stimednp.aplikasimoviecataloguesub4.mydbentity.Moviesm;
     import com.stimednp.aplikasimoviecataloguesub4.mymodel.MainViewModel;
     import com.stimednp.aplikasimoviecataloguesub4.mymodel.MovieItems;

     import java.lang.ref.WeakReference;
     import java.util.ArrayList;

    public class NavMoviesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoviesmCallback {
        private MoviesmAdapter moviesmAdapter;
        private MovieHelper movieHelper;
        private RecyclerView rvMoviesm;
        private static final String EXTRA_STATE = "EXTRA_STATE";

        private RecyclerView recyclerViewMovie;
        private MainViewModel mainViewModel;
        private MovieItemsAdapter movieItemsAdapter;
        private SwipeRefreshLayout refreshLayoutMovie;
        private RelativeLayout frameLayoutMovie;
        private String noInternet, tryAgain, reconnect, wrongNet, wrongError;
        private String textSearch;
        private TextView textViewEmpty;

        public NavMoviesFragment() {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_nav_movies, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            setHasOptionsMenu(true);// method for menu search

            movieHelper = MovieHelper.getInstance(getContext());
            movieHelper.open();
            moviesmAdapter = new MoviesmAdapter(this.getActivity());
            rvMoviesm = view.findViewById(R.id.rv_tab_movies);
            rvMoviesm.setAdapter(moviesmAdapter);

            if (savedInstanceState == null){
                new LoadMoviesmAsync(movieHelper, this).execute();
            } else {
                ArrayList<Moviesm> moviesmList = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
                if (moviesmList != null){
                    moviesmAdapter.setListMoviesm(moviesmList);
                }
            }

//        textViewEmpty = view.findViewById(R.id.tv_db_movies_empty);
//        refreshLayoutMovie = view.findViewById(R.id.swipe_scroll_movie);
//        frameLayoutMovie = view.findViewById(R.id.framel_movie);
//        recyclerViewMovie = view.findViewById(R.id.rv_tab_movies);
//
//        if (getActivity() != null) {
//            mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
//            mainViewModel.getListMovies().observe(getActivity(), getMovie);
//            movieItemsAdapter = new MovieItemsAdapter(getContext());
//        }
//        refreshLayoutMovie.setOnRefreshListener(this);

            //callmethod
            getAllMyString();
//        checkingNetwork();
            setHasOptionsMenu(true);
        }

        @Override
        public void onSaveInstanceState(@NonNull Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putParcelableArrayList(EXTRA_STATE, moviesmAdapter.getmoviesmList());
        }

        private void getAllMyString() {
            AllMyStrings myStr = new AllMyStrings();
            noInternet = myStr.getNoInet(getContext());
            tryAgain = myStr.getTryAgain(getContext());
            reconnect = myStr.getRecon(getContext());
            wrongNet = myStr.getWrongNet(getContext());
            wrongError = myStr.getWrongErr(getContext());
        }

//    private Observer<? super ArrayList<MovieItems>> getMovie = new Observer<ArrayList<MovieItems>>() {
//        @Override
//        public void onChanged(ArrayList<MovieItems> movieItems) {
//            if (movieItemsAdapter != null) {
//                movieItemsAdapter.setMoviesData(movieItems);
//                if (movieItems.size() < 1 ) {
//                    textViewEmpty.setVisibility(View.VISIBLE);
//                } else {
//                    textViewEmpty.setVisibility(View.INVISIBLE);
//                }
//                timeRecyclerLoadFalse();
//            }
//        }
//    };

        private void checkingNetwork() {
            refreshLayoutMovie.setRefreshing(true);
            if (getContext() != null) {
                if (CheckNetwork.isInternetAvailable(getContext())) {
                    int status = CheckNetwork.statusInternet;
                    if (status == 0) {//disconnect
                        timeRecyclerLoadFalse();
                        Snackbar snackbar = Snackbar.make(frameLayoutMovie, noInternet, Snackbar.LENGTH_SHORT).setAction(tryAgain, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkingNetwork();
                            }
                        });
                        snackbar.setActionTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                        snackbar.show();
                    } else if (status == 1) {//connected
                        showRecyclerList(textSearch);
                    } else if (status == 2) {//reconnection
                        Toast.makeText(getContext(), reconnect, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), wrongNet, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), wrongError, Toast.LENGTH_SHORT).show();
                }
            }
        }

        private void timeRecyclerLoadFalse() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (refreshLayoutMovie.isRefreshing()) {
                        refreshLayoutMovie.setRefreshing(false);
                    }
                }
            }, 1000);
        }

        @Override
        public void onRefresh() {
            checkingNetwork();
        }

        @Override
        public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            SearchView searchView = (SearchView) menu.findItem(R.id.itemm_search).getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    textSearch = query;
                    checkingNetwork();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    textSearch = newText;
                    checkingNetwork();
                    return true;
                }
            });
        }


        private void showRecyclerList(String query) {
            if (mainViewModel != null) {
                if (query != null) {
                    if (!query.equals("")) {
                        mainViewModel.setSeacrhMovies(query);
                    } else {
                        mainViewModel.setListMovies();
                    }
                } else {
                    mainViewModel.setListMovies();
                }
                movieItemsAdapter.notifyDataSetChanged();
                recyclerViewMovie.setHasFixedSize(true);
                recyclerViewMovie.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerViewMovie.setAdapter(movieItemsAdapter);
            }
        }

        @Override
        public void preExecute() {

        }

        @Override
        public void postExecute(ArrayList<Moviesm> moviesmList) {

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
                weakCallback.get().preExecute();
            }
        }
    }


     */
}
