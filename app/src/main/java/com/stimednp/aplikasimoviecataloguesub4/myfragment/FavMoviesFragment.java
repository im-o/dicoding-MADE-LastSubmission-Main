package com.stimednp.aplikasimoviecataloguesub4.myfragment;


import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
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
import com.stimednp.aplikasimoviecataloguesub4.mydb.LoadFavMoviesCallback;
import com.stimednp.aplikasimoviecataloguesub4.mydb.MoviesHelper;
import com.stimednp.aplikasimoviecataloguesub4.mydbadapter.FavMoviesAdapter;
import com.stimednp.aplikasimoviecataloguesub4.mydbentity.MoviesModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.stimednp.aplikasimoviecataloguesub4.mydbmapcursor.MappingHelper.mapCursorToArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavMoviesFragment extends Fragment implements LoadFavMoviesCallback {
    public static final String TAG = FavMoviesFragment.class.getSimpleName();
    private FavMoviesAdapter favMoviesAdapter;
    private RecyclerView recyclervFavMovies;
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private MoviesHelper moviesHelper;
    private SwipeRefreshLayout refreshLayoutMovie;
    private ProgressBar progressBarMovie;
    private TextView textViewEmpty;
    private ArrayList<MoviesModel> list;

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
        moviesHelper = new MoviesHelper(this.getActivity());

        moviesHelper.open();
        list = moviesHelper.query();
        favMoviesAdapter = new FavMoviesAdapter(this.getActivity());
        recyclervFavMovies.setAdapter(favMoviesAdapter);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, getContext());

        if (getContext() != null) {
            getContext().getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);
        }

        evetListener();
        checkingMovieList();

        if (savedInstanceState == null) {
            new LoadMoviesAsync(getContext(), this).execute();
        } else {
            list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                favMoviesAdapter.setListMoviesm(list);
            }
        }
    }

    private void evetListener() {
        favMoviesAdapter.setOnItemClickCallback(new FavMoviesAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(MoviesModel moviesModel) {
                Intent intent = new Intent(getActivity(), DetailsMovieActivity.class);
                Uri uri = Uri.parse(CONTENT_URI + "/" + moviesModel.getId());
                intent.setData(uri);
                intent.putExtra(DetailsMovieActivity.EXTRA_WHERE_FROM, TAG);
                intent.putExtra(DetailsMovieActivity.EXTRA_MOVIE, moviesModel);
                startActivityForResult(intent, DetailsMovieActivity.REQUEST_FAV);
            }
        });
        refreshLayoutMovie.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, list);
    }

    private void checkingMovieList() {
        recyclervFavMovies.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_transition_animation));
        if (list.size() < 1) {
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
        }
        progressBarMovie.setVisibility(View.GONE);
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
    public void postExecute(Cursor cursor) {
        list = mapCursorToArrayList(cursor);
        if (list.size() > 0) {
            favMoviesAdapter.setListMoviesm(list);
        } else {
            favMoviesAdapter.setListMoviesm(list);
        }
        refreshLayoutMovie.setRefreshing(false);
    }

    private static class LoadMoviesAsync extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFavMoviesCallback> weakCallback;

        LoadMoviesAsync(Context context, LoadFavMoviesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(CONTENT_URI,
                    null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().postExecute(cursor);
        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            try {
                new LoadMoviesAsync(context, (LoadFavMoviesCallback) context).execute();
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == DetailsMovieActivity.REQUEST_FAV) {
                if (resultCode == DetailsMovieActivity.RESULT_FAV) {
                    MoviesModel moviesModel = data.getParcelableExtra(DetailsMovieActivity.EXTRA_MOVIE);
                    favMoviesAdapter.addItem(moviesModel);
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
                new LoadMoviesAsync(getContext(), this).execute();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        moviesHelper.close();
    }
}
