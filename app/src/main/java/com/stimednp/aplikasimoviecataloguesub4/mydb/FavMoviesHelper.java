package com.stimednp.aplikasimoviecataloguesub4.mydb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.stimednp.aplikasimoviecataloguesub4.mydbentity.FavMoviesModel;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_BACK_PATH;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_OVERVIEW;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_POSTER_PATH;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_RELEASE_DATE;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_TITLE;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_VOTE_AVERAGE;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_VOTE_COUNT;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.TABLE_MOVIE;

/**
 * Created by rivaldy on 8/19/2019.
 */

public class FavMoviesHelper {
    private static final String DATABASE_TABLE = TABLE_MOVIE;
    private static DatabaseHelper dataBaseHelper;
    private static FavMoviesHelper INSTANCE;

    private static SQLiteDatabase database;

    public FavMoviesHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static FavMoviesHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavMoviesHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    //CRUD
    public ArrayList<FavMoviesModel> getAllMovies() {
        ArrayList<FavMoviesModel> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        FavMoviesModel favMoviesModel;
        if (cursor.getCount() > 0) {
            do {
                favMoviesModel = new FavMoviesModel();
                favMoviesModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                favMoviesModel.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                favMoviesModel.setRelease_date(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RELEASE_DATE)));
                favMoviesModel.setVote_average(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_VOTE_AVERAGE)));
                favMoviesModel.setVote_count(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VOTE_COUNT)));
                favMoviesModel.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OVERVIEW)));
                favMoviesModel.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSTER_PATH)));
                favMoviesModel.setBackdrop_path(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BACK_PATH)));

                arrayList.add(favMoviesModel);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    //save/insert data
    public long insertMovie(FavMoviesModel favMoviesModel) {
        ContentValues args = new ContentValues();
        args.put(COLUMN_TITLE, favMoviesModel.getTitle());
        args.put(COLUMN_RELEASE_DATE, favMoviesModel.getRelease_date());
        args.put(COLUMN_VOTE_AVERAGE, favMoviesModel.getVote_average());
        args.put(COLUMN_VOTE_COUNT, favMoviesModel.getVote_count());
        args.put(COLUMN_OVERVIEW, favMoviesModel.getOverview());
        args.put(COLUMN_POSTER_PATH, favMoviesModel.getPoster_path());
        args.put(COLUMN_BACK_PATH, favMoviesModel.getBackdrop_path());
        return database.insert(DATABASE_TABLE, null, args);
    }

    //update
    public int updateMovie(FavMoviesModel favMoviesModel) {
        ContentValues args = new ContentValues();
        args.put(COLUMN_TITLE, favMoviesModel.getTitle());
        args.put(COLUMN_RELEASE_DATE, favMoviesModel.getRelease_date());
        args.put(COLUMN_VOTE_AVERAGE, favMoviesModel.getVote_average());
        args.put(COLUMN_VOTE_COUNT, favMoviesModel.getVote_count());
        args.put(COLUMN_OVERVIEW, favMoviesModel.getOverview());
        args.put(COLUMN_POSTER_PATH, favMoviesModel.getPoster_path());
        args.put(COLUMN_BACK_PATH, favMoviesModel.getBackdrop_path());
        return database.update(DATABASE_TABLE, args, _ID + "= '" + favMoviesModel.getId() + "'", null);
    }

    //delete
    public int deleteMovie(int id) {
        return database.delete(TABLE_MOVIE, _ID + " = '" + id + "'", null);
    }   //delete
    public int deleteMovieByTitle(String title) {
        return database.delete(TABLE_MOVIE, COLUMN_TITLE + " = '" + title + "'", null);
    }
}
