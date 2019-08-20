package com.stimednp.aplikasimoviecataloguesub4.myactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.stimednp.aplikasimoviecataloguesub4.mydb.MovieHelper;
import com.stimednp.aplikasimoviecataloguesub4.mydbadapter.MoviesmAdapter;
import com.stimednp.aplikasimoviecataloguesub4.mydbentity.Moviesm;
import com.stimednp.aplikasimoviecataloguesub4.myfragment.FavMoviesFragment;
import com.stimednp.aplikasimoviecataloguesub4.myfragment.NavMoviesFragment;
import com.stimednp.aplikasimoviecataloguesub4.mymodel.MovieItems;
import com.stimednp.aplikasimoviecataloguesub4.mymodel.TvShowItems;
import com.stimednp.aplikasimoviecataloguesub4.roomdb.TvShowRoomDatabase;
import com.stimednp.aplikasimoviecataloguesub4.roomtvshow.TvShow;
import com.stimednp.aplikasimoviecataloguesub4.roomtvshow.TvShowAdapter;

public class DetailsMovieActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_WHERE_FROM = "extra_where_from";
    public static final int REQUEST_ADD = 100;
    public static final int RESULT_DELETE = 301;
    public static final int RESULT_ADD = 101;
    public static final String EXTRA_POSITION = "extra_position";

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

    private Moviesm moviesm;
    private int position;
    private MovieHelper movieHelper;
    private boolean isEdit = false;

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
        movieHelper = MovieHelper.getInstance(getApplicationContext());
        movieHelper.open();

        if (whereFrom.equals(MoviesmAdapter.TAG) || (whereFrom.equals(FavMoviesFragment.TAG))){
            moviesm = getIntent().getParcelableExtra(EXTRA_MOVIE);
            if (moviesm != null){
                position = getIntent().getIntExtra(EXTRA_POSITION, 0);
                isEdit = true;
            } else {
                moviesm = new Moviesm();
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
                Glide.with(getApplicationContext()).load(pathImg + movieUrlPhoto).into(imgViewFromUrl);
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
                Glide.with(getApplicationContext()).load(pathImg + tvShowUrlPhoto).into(imgViewFromUrl);
                Glide.with(getApplicationContext()).load(pathImg + tvShowUrlBg).into(imgViewBg);
            }
        } else if (whereFrom.equals(MoviesmAdapter.TAG) || (whereFrom.equals(FavMoviesFragment.TAG))) { //for details MoviesAdapter from dbroom
            Moviesm moviesm = getIntent().getParcelableExtra(EXTRA_MOVIE);
            if (moviesm != null) {
                moviesId = moviesm.getId();
                movieTitle = moviesm.getTitle();
                keyFavorite = movieTitle; //key
                movieDesc = moviesm.getOverview();
                movieRelease = moviesm.getRelease_date();
                movieRating = moviesm.getVote_average().toString();
                movieVoteCount = moviesm.getVote_count();
                movieUrlPhoto = moviesm.getPoster_path();
                movieUrlBg = moviesm.getBackdrop_path();

                AllOtherMethod allOtherMethod = new AllOtherMethod();
                String movieDate = allOtherMethod.changeFormatDate(movieRelease);
                String movieYearRelease = allOtherMethod.getLastYear(movieDate);

                tvMovieTitle.setText(String.format(movieTitle + " (%s)", movieYearRelease));
                tvMovieDesc.setText(movieDesc);
                tvMovieRelease.setText(movieDate);
                tvMovieRating.setText(movieRating);
                tvMovieVoteCount.setText(movieVoteCount);
                Glide.with(getApplicationContext()).load(pathImg + movieUrlPhoto).into(imgViewFromUrl);
                Glide.with(getApplicationContext()).load(pathImg + movieUrlBg).into(imgViewBg);

//                Intent intent = new Intent();
//                intent.putExtra(EXTRA_MOVIE, moviesm);
//                intent.putExtra(EXTRA_POSITION, position);
//
//                if (isEdit){
//                    long result = movieHelper.getAllMovies().size();
//                    if (result > 0){
//                        setResult(RESULT_ADD, intent);
//                    } else {
//                        Toast.makeText(this, "GAGAL", Toast.LENGTH_SHORT).show();
//                    }
//                }
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
                Glide.with(getApplicationContext()).load(pathImg + tvShowUrlPhoto).into(imgViewFromUrl);
                Glide.with(getApplicationContext()).load(pathImg + tvShowUrlBg).into(imgViewBg);
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



    private void insertFavorite() {
        moviesm.setTitle(movieTitle);
        moviesm.setOverview(movieDesc);
        moviesm.setRelease_date(movieRelease);
        moviesm.setVote_average(Double.parseDouble(movieRating));
        moviesm.setVote_count(movieVoteCount);
        moviesm.setPoster_path(movieUrlPhoto);
        moviesm.setBackdrop_path(movieUrlBg);

        Intent intent = new Intent();
        intent.putExtra(EXTRA_MOVIE, moviesm);
        intent.putExtra(EXTRA_POSITION, position);
        Log.d("INI", "insertFavorite: " + moviesm);
        int result = (int) movieHelper.insertMovie(moviesm);
        if (result > 0) {
            moviesm.setId(result);
            setResult(RESULT_ADD, intent);
        }
    }

    //Room
    private void setMovies() {
        @SuppressLint("StaticFieldLeak")
        class SetMovies extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                Moviesm moviesm = new Moviesm();
                moviesm.setTitle(movieTitle);
                moviesm.setOverview(movieDesc);
                moviesm.setRelease_date(movieRelease);
                moviesm.setVote_average(Double.parseDouble(movieRating));
                moviesm.setVote_count(movieVoteCount);
                moviesm.setPoster_path(movieUrlPhoto);
                moviesm.setBackdrop_path(movieUrlBg);

                Intent intent = new Intent();
                intent.putExtra(EXTRA_MOVIE, moviesm);
                intent.putExtra(EXTRA_POSITION, position);

                long result = movieHelper.insertMovie(moviesm);
                if (result > 0) {
                    moviesm.setId((int) result);
                    setResult(RESULT_ADD, intent);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Snackbar snackbar = Snackbar.make(containerCoord, strMsgSuccessInsert, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }
        SetMovies setMovies = new SetMovies();
        setMovies.execute();
    }

    private void deleteMoviesByTitle() {
        @SuppressLint("StaticFieldLeak")
        class DeleteMovies extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                long result = movieHelper.deleteMovieByTitle(movieTitle);
                Log.d("INIII", "deleteMoviesByTitle: "+result+" :: "+movieTitle+" :: "+position);
                if (result > 0) {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_POSITION, position);
                    setResult(RESULT_DELETE, intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal Hapus", Toast.LENGTH_SHORT).show();
                }
//                MoviesRoomDatabase.getDatabase(getApplicationContext()).moviesDao().deleteByTitle(movieTitle);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Snackbar snackbar = Snackbar.make(containerCoord, strMsgSuccessDelete, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }
        DeleteMovies deleteMoviesByTitle = new DeleteMovies();
        deleteMoviesByTitle.execute();
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
                Snackbar snackbar = Snackbar.make(containerCoord, strMsgSuccessInsert, Snackbar.LENGTH_SHORT);
                snackbar.show();
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
                Snackbar snackbar = Snackbar.make(containerCoord, strMsgSuccessDelete, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }
        DeleteTvShowByTitle deleteTvShowByTitle = new DeleteTvShowByTitle();
        deleteTvShowByTitle.execute();
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
            if ((whereFrom.equals(FavMoviesFragment.TAG) || (whereFrom.equals(MoviesmAdapter.TAG))  || (whereFrom.equals(MovieItemsAdapter.TAG)))) {
                deleteMoviesByTitle(); //deleteMovies
            } else if ((whereFrom.equals(TvShowItemsAdapter.TAG)) || (whereFrom.equals(TvShowAdapter.TAG))) {
                deleteTvShowByTitle(); //deleteTvShow
            }

        } else {
            boolean isFavorite = true;
            tesPref(isFavorite);
            checkingFavorite();
            //insert
            if ((whereFrom.equals(FavMoviesFragment.TAG) || (whereFrom.equals(MoviesmAdapter.TAG)) || (whereFrom.equals(MovieItemsAdapter.TAG)))) {
//                insertFavorite();
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
