package com.stimednp.aplikasimoviecataloguesub4.myfragment;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
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
                recyclerViewTvShow.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_transition_animation));
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
}
