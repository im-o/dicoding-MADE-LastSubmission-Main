package com.stimednp.aplikasimoviecataloguesub4.myfragment;


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
import com.stimednp.aplikasimoviecataloguesub4.mymodel.MainViewModel;
import com.stimednp.aplikasimoviecataloguesub4.mymodel.MovieItems;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavMoviesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
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
        textViewEmpty = view.findViewById(R.id.tv_db_movies_empty);
        refreshLayoutMovie = view.findViewById(R.id.swipe_scroll_movie);
        frameLayoutMovie = view.findViewById(R.id.framel_movie);
        recyclerViewMovie = view.findViewById(R.id.rv_tab_movies);

        if (getActivity() != null) {
            mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
            mainViewModel.getListMovies().observe(getActivity(), getMovie);
            movieItemsAdapter = new MovieItemsAdapter(getContext());
        }
        refreshLayoutMovie.setOnRefreshListener(this);

        //callmethod
        getAllMyString();
        checkingNetwork();
        setHasOptionsMenu(true);
    }

    private void getAllMyString() {
        AllMyStrings myStr = new AllMyStrings();
        noInternet = myStr.getNoInet(getContext());
        tryAgain = myStr.getTryAgain(getContext());
        reconnect = myStr.getRecon(getContext());
        wrongNet = myStr.getWrongNet(getContext());
        wrongError = myStr.getWrongErr(getContext());
    }

    private Observer<? super ArrayList<MovieItems>> getMovie = new Observer<ArrayList<MovieItems>>() {
        @Override
        public void onChanged(ArrayList<MovieItems> movieItems) {
            if (movieItemsAdapter != null) {
                movieItemsAdapter.setMoviesData(movieItems);
                if (movieItems.size() < 1 ) {
                    textViewEmpty.setVisibility(View.VISIBLE);
                } else {
                    textViewEmpty.setVisibility(View.INVISIBLE);
                }
                timeRecyclerLoadFalse();
            }
        }
    };

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
}
