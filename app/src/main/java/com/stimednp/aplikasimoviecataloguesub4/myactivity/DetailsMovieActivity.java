package com.stimednp.aplikasimoviecataloguesub4.myactivity;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.stimednp.aplikasimoviecataloguesub4.R;
import com.stimednp.aplikasimoviecataloguesub4.adapter.MovieItemsAdapter;
import com.stimednp.aplikasimoviecataloguesub4.adapter.TvShowItemsAdapter;
import com.stimednp.aplikasimoviecataloguesub4.addingmethod.AllOtherMethod;
import com.stimednp.aplikasimoviecataloguesub4.mydbadapter.FavMoviesAdapter;
import com.stimednp.aplikasimoviecataloguesub4.mydbentity.MoviesModel;
import com.stimednp.aplikasimoviecataloguesub4.myfragment.FavMoviesFragment;
import com.stimednp.aplikasimoviecataloguesub4.mymodel.MovieItems;
import com.stimednp.aplikasimoviecataloguesub4.mymodel.TvShowItems;
import com.stimednp.aplikasimoviecataloguesub4.mystackwidget.ImageBannerWidget;
import com.stimednp.aplikasimoviecataloguesub4.roomdb.TvShowRoomDatabase;
import com.stimednp.aplikasimoviecataloguesub4.roomtvshow.TvShow;
import com.stimednp.aplikasimoviecataloguesub4.roomtvshow.TvShowAdapter;

import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_BACK_PATH;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_OVERVIEW;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_POSTER_PATH;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_RELEASE_DATE;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_TITLE;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_VOTE_AVERAGE;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_VOTE_COUNT;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.ID;

public class DetailsMovieActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = DetailsMovieActivity.class.getSimpleName();
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_WHERE_FROM = "extra_where_from";
    public static final String EXTRA_POSITION = "extra_position";
    public static final int REQUEST_FAV = 100;
    public static final int RESULT_DELETE = 301;
    public static final int RESULT_FAV = 101;

    private Toolbar toolbarDetails;
    private TextView tvMovieTitle, tvMovieDesc, tvMovieRelease, tvMovieRating, tvMovieVoteCount;
    private ImageView imgViewFromUrl, imgViewBg;
    private CardView cardViewDetails;
    private FloatingActionButton fabFavoriteFalse;
    private CoordinatorLayout containerCoord;
    private boolean isCheckFavorite;
    private String keyFavorite = "my_key"; //savepreference
    private String mySavePref = "my_savepref_favorite";
    private String strMsgSuccessInsert;
    private String strMsgSuccessDelete;

    private String movieTitle, movieDesc, movieRelease, movieRating, movieVoteCount, movieUrlPhoto, movieUrlBg;
    private int moviesId;
    private String tvShowTitle, tvShowDesc, tvShowRelease, tvShowRating, tvShowVoteCount, tvShowUrlPhoto, tvShowUrlBg;

    private MoviesModel moviesModel;
    private int position;
    private boolean isEdit = false;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);
        //inisial
        containerCoord = findViewById(R.id.container_coordinator_detail);
        toolbarDetails = findViewById(R.id.toolbar_detail);
        CollapsingToolbarLayout collapse = findViewById(R.id.collapse_toolbar_detail);
        tvMovieTitle = findViewById(R.id.tv_title_detail);
        tvMovieDesc = findViewById(R.id.tv_desc_detail);
        tvMovieRelease = findViewById(R.id.tv_release_date_detail);
        tvMovieRating = findViewById(R.id.tv_rating_detail);
        tvMovieVoteCount = findViewById(R.id.tv_vote_count_detail);
        imgViewFromUrl = findViewById(R.id.img_movie_photo_detail);
        imgViewBg = findViewById(R.id.img_bg_detail);
        cardViewDetails = findViewById(R.id.card_view_img_detail);
        fabFavoriteFalse = findViewById(R.id.fab_favorite_false);
        fabFavoriteFalse.setOnClickListener(this);
        strMsgSuccessInsert = getResources().getString(R.string.str_msg_add_fav);
        strMsgSuccessDelete = getResources().getString(R.string.str_msg_delete_fav);
        collapse.setExpandedTitleColor(Color.argb(0, 0, 0, 0));

        //callmethod
        setActionBarToolbar();
        getDataParceable();
        checkingFavorite();

        String whereFrom = getIntent().getStringExtra(EXTRA_WHERE_FROM);

        if (whereFrom.equals(FavMoviesAdapter.TAG) || (whereFrom.equals(FavMoviesFragment.TAG))) {
            moviesModel = getIntent().getParcelableExtra(EXTRA_MOVIE);
            if (moviesModel != null) {
                position = getIntent().getIntExtra(EXTRA_POSITION, 0);
                isEdit = true;
            } else {
                moviesModel = new MoviesModel();
            }
        }

        uri = getIntent().getData();
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) moviesModel = new MoviesModel(cursor);
                cursor.close();
            }
        }
    }

    private void getDataParceable() { //this is get DATA when click
        String whereFrom = getIntent().getStringExtra(EXTRA_WHERE_FROM);
        String pathImg = "https://image.tmdb.org/t/p/w500";
        cardViewDetails.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_scale_animation));

        if (whereFrom.equals(MovieItemsAdapter.TAG)) { //for details TabMoviesFragment
            MovieItems movieItems = getIntent().getParcelableExtra(EXTRA_MOVIE);
            if (movieItems != null) {
                moviesId = movieItems.getId();
                movieTitle = movieItems.getTitle();
                keyFavorite = movieTitle; //key
                movieDesc = movieItems.getOverview();
                movieRelease = movieItems.getRelease_date();
                movieRating = movieItems.getVote_average().toString();
                movieVoteCount = movieItems.getVote_count();
                movieUrlPhoto = movieItems.getPoster_path();
                movieUrlBg = movieItems.getBackdrop_path();

                AllOtherMethod allOtherMethod = new AllOtherMethod();
                String movieDate = allOtherMethod.changeFormatDate(movieRelease);
                String movieYearRelease = allOtherMethod.getLastYear(movieDate);

                tvMovieTitle.setText(String.format(movieTitle + " (%s)", movieYearRelease));
                tvMovieDesc.setText(movieDesc);
                tvMovieRelease.setText(movieDate);
                tvMovieRating.setText(movieRating);
                tvMovieVoteCount.setText(movieVoteCount);
                if (movieUrlPhoto != null)
                    Glide.with(getApplicationContext()).load(pathImg + movieUrlPhoto).into(imgViewFromUrl);
                if (movieUrlBg != null)
                    Glide.with(getApplicationContext()).load(pathImg + movieUrlBg).into(imgViewBg);
            }
        } else if (whereFrom.equals(TvShowItemsAdapter.TAG)) { //for details TabTvShowFragment
            TvShowItems tvShowItems = getIntent().getParcelableExtra(EXTRA_MOVIE);
            if (tvShowItems != null) {
                tvShowTitle = tvShowItems.getName();
                keyFavorite = tvShowTitle; //key
                tvShowDesc = tvShowItems.getOverview();
                tvShowRelease = tvShowItems.getFirst_air_date();
                tvShowRating = tvShowItems.getVote_average().toString();
                tvShowVoteCount = tvShowItems.getVote_count();
                tvShowUrlPhoto = tvShowItems.getPoster_path();
                tvShowUrlBg = tvShowItems.getBackdrop_path();

                AllOtherMethod allOtherMethod = new AllOtherMethod();
                String tvShowDate = allOtherMethod.changeFormatDate(tvShowRelease);
                String tvShowYearRelease = allOtherMethod.getLastYear(tvShowDate);

                tvMovieTitle.setText(String.format(tvShowTitle + " (%s)", tvShowYearRelease));
                tvMovieDesc.setText(tvShowDesc);
                tvMovieRelease.setText(tvShowDate);
                tvMovieRating.setText(tvShowRating);
                tvMovieVoteCount.setText(tvShowVoteCount);
                if (tvShowUrlPhoto != null)
                    Glide.with(getApplicationContext()).load(pathImg + movieUrlPhoto).into(imgViewFromUrl);
                if (tvShowUrlBg != null)
                    Glide.with(getApplicationContext()).load(pathImg + movieUrlBg).into(imgViewBg);
            }
        } else if (whereFrom.equals(FavMoviesAdapter.TAG) || (whereFrom.equals(FavMoviesFragment.TAG))) { //for details MoviesAdapter from dbroom
            MoviesModel moviesModel = getIntent().getParcelableExtra(EXTRA_MOVIE);
            if (moviesModel != null) {
                moviesId = moviesModel.getId();
                movieTitle = moviesModel.getTitle();
                keyFavorite = movieTitle; //key
                movieDesc = moviesModel.getOverview();
                movieRelease = moviesModel.getRelease_date();
                movieRating = moviesModel.getVote_average().toString();
                movieVoteCount = moviesModel.getVote_count();
                movieUrlPhoto = moviesModel.getPoster_path();
                movieUrlBg = moviesModel.getBackdrop_path();

                AllOtherMethod allOtherMethod = new AllOtherMethod();
                String movieDate = allOtherMethod.changeFormatDate(movieRelease);
                String movieYearRelease = allOtherMethod.getLastYear(movieDate);

                tvMovieTitle.setText(String.format(movieTitle + " (%s)", movieYearRelease));
                tvMovieDesc.setText(movieDesc);
                tvMovieRelease.setText(movieDate);
                tvMovieRating.setText(movieRating);
                tvMovieVoteCount.setText(movieVoteCount);
                if (movieUrlPhoto != null)
                    Glide.with(getApplicationContext()).load(pathImg + movieUrlPhoto).into(imgViewFromUrl);
                if (movieUrlBg != null)
                    Glide.with(getApplicationContext()).load(pathImg + movieUrlBg).into(imgViewBg);
            }
        } else if (whereFrom.equals(TvShowAdapter.TAG)) { //for details TvShowAdapter from roomdb
            TvShow tvShow = getIntent().getParcelableExtra(EXTRA_MOVIE);
            if (tvShow != null) {
                tvShowTitle = tvShow.getName();
                keyFavorite = tvShowTitle; //key
                tvShowDesc = tvShow.getOverview();
                tvShowRelease = tvShow.getFirst_air_date();
                tvShowRating = tvShow.getVote_average().toString();
                tvShowVoteCount = tvShow.getVote_count();
                tvShowUrlPhoto = tvShow.getPoster_path();
                tvShowUrlBg = tvShow.getBackdrop_path();

                AllOtherMethod allOtherMethod = new AllOtherMethod();
                String tvShowDate = allOtherMethod.changeFormatDate(tvShowRelease);
                String tvShowYearRelease = allOtherMethod.getLastYear(tvShowDate);

                tvMovieTitle.setText(String.format(tvShowTitle + " (%s)", tvShowYearRelease));
                tvMovieDesc.setText(tvShowDesc);
                tvMovieRelease.setText(tvShowDate);
                tvMovieRating.setText(tvShowRating);
                tvMovieVoteCount.setText(tvShowVoteCount);
                if (tvShowUrlPhoto != null)
                    Glide.with(getApplicationContext()).load(pathImg + movieUrlPhoto).into(imgViewFromUrl);
                if (tvShowUrlBg != null)
                    Glide.with(getApplicationContext()).load(pathImg + movieUrlBg).into(imgViewBg);
            }
        }
    }

    private void setActionBarToolbar() {
        setSupportActionBar(toolbarDetails);
        toolbarDetails.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
        toolbarDetails.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setMovies() { //Insert
        Intent intent = new Intent();
        intent.putExtra(EXTRA_MOVIE, moviesModel);
        intent.putExtra(EXTRA_POSITION, position);

        MoviesModel moviesModel = new MoviesModel();
        moviesModel.setId(moviesId);
        moviesModel.setTitle(movieTitle);
        moviesModel.setRelease_date(movieRelease);
        moviesModel.setVote_average(Double.parseDouble(movieRating));
        moviesModel.setVote_count(movieVoteCount);
        moviesModel.setOverview(movieDesc);
        moviesModel.setPoster_path(movieUrlPhoto);
        moviesModel.setBackdrop_path(movieUrlBg);

        ContentValues values = new ContentValues();
        values.put(ID, moviesId);
        values.put(COLUMN_TITLE, movieTitle);
        values.put(COLUMN_RELEASE_DATE, movieRelease);
        values.put(COLUMN_VOTE_AVERAGE, movieRating);
        values.put(COLUMN_VOTE_COUNT, movieVoteCount);
        values.put(COLUMN_OVERVIEW, movieDesc);
        values.put(COLUMN_POSTER_PATH, movieUrlPhoto);
        values.put(COLUMN_BACK_PATH, movieUrlBg);

        getContentResolver().insert(CONTENT_URI, values);
        showSnackBar(movieTitle + " " + strMsgSuccessInsert);
        setResult(RESULT_FAV, intent);
        updateMyfavmoviesWidget();
    }

    private void deleteMovies() { //delete
        Intent intent = new Intent();
        intent.putExtra(EXTRA_POSITION, 0);
        getContentResolver().delete(uri, null, null);
        showSnackBar(movieTitle + " " + strMsgSuccessDelete);
        setResult(RESULT_DELETE, intent);
        updateMyfavmoviesWidget();
    }

    private void updateMyfavmoviesWidget() {
        Context context = getApplicationContext();
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, ImageBannerWidget.class);
        int[] idAppWidget = widgetManager.getAppWidgetIds(componentName);
        widgetManager.notifyAppWidgetViewDataChanged(idAppWidget, R.id.stack_view);
    }

    private void setTvShows() {
        @SuppressLint("StaticFieldLeak")
        class SetTvShow extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                TvShow tvShow = new TvShow();
                tvShow.setName(tvShowTitle);
                tvShow.setOverview(tvShowDesc);
                tvShow.setFirst_air_date(tvShowRelease);
                tvShow.setVote_average(Double.parseDouble(tvShowRating));
                tvShow.setVote_count(tvShowVoteCount);
                tvShow.setPoster_path(tvShowUrlPhoto);
                tvShow.setBackdrop_path(tvShowUrlBg);
                tvShow.setFavorite(true);
                TvShowRoomDatabase.getDatabase(getApplicationContext()).tvShowDao().insert(tvShow); //adding to db
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                showSnackBar(tvShowTitle + " " + strMsgSuccessInsert);
            }
        }
        SetTvShow setTvShow = new SetTvShow();
        setTvShow.execute();
    }

    private void deleteTvShowByTitle() {
        @SuppressLint("StaticFieldLeak")
        class DeleteTvShowByTitle extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                TvShowRoomDatabase.getDatabase(getApplicationContext()).tvShowDao().deleteByTitle(tvShowTitle);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                showSnackBar(tvShowTitle + " " + strMsgSuccessDelete);
            }
        }
        DeleteTvShowByTitle deleteTvShowByTitle = new DeleteTvShowByTitle();
        deleteTvShowByTitle.execute();
    }

    private void showSnackBar(String msg) {
        Snackbar snackbar = Snackbar.make(containerCoord, msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fab_favorite_false) {
            setIsFavorite();
        }
    }

    private void tesPref(boolean isFavor) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(mySavePref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(keyFavorite, isFavor);
        editor.apply();
    }

    private boolean radRef() {
        SharedPreferences mSharedPreferences = this.getSharedPreferences(mySavePref, Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(keyFavorite, false);
    }

    private void setIsFavorite() {
        String whereFrom = getIntent().getStringExtra(EXTRA_WHERE_FROM);
        if (isCheckFavorite) {
            boolean isFavorite = false;
            tesPref(isFavorite);
            checkingFavorite();
            //delete
            if ((whereFrom.equals(FavMoviesFragment.TAG) || (whereFrom.equals(FavMoviesAdapter.TAG)) || (whereFrom.equals(MovieItemsAdapter.TAG)))) {
                deleteMovies(); //deleteMovies
            } else if ((whereFrom.equals(TvShowItemsAdapter.TAG)) || (whereFrom.equals(TvShowAdapter.TAG))) {
                deleteTvShowByTitle(); //deleteTvShow
            }

        } else {
            boolean isFavorite = true;
            tesPref(isFavorite);
            checkingFavorite();
            //insert
            if ((whereFrom.equals(FavMoviesFragment.TAG) || (whereFrom.equals(FavMoviesAdapter.TAG)) || (whereFrom.equals(MovieItemsAdapter.TAG)))) {
                setMovies(); //insertMovies
            } else if ((whereFrom.equals(TvShowItemsAdapter.TAG)) || (whereFrom.equals(TvShowAdapter.TAG))) {
                setTvShows(); //insertTvShow
            }
        }
    }

    private void checkingFavorite() {
        boolean isFavorite = radRef();
        if (isFavorite) {
            fabFavoriteFalse.setImageResource(R.drawable.ic_favorite_true_24dp);
            isCheckFavorite = true;
        } else {
            fabFavoriteFalse.setImageResource(R.drawable.ic_favorite_border_before);
            isCheckFavorite = false;
        }
    }
}
