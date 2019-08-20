package com.stimednp.aplikasimoviecataloguesub4.mydb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.stimednp.aplikasimoviecataloguesub4.mydbentity.Moviesm;

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

public class MovieHelper {
    private static final String DATABASE_TABLE = TABLE_MOVIE;
    private static DatabaseHelper dataBaseHelper;
    private static MovieHelper INSTANCE;

    private static SQLiteDatabase database;

    public MovieHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static MovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovieHelper(context);
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
    public ArrayList<Moviesm> getAllMovies() {
        ArrayList<Moviesm> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        Moviesm moviesm;
        if (cursor.getCount() > 0) {
            do {
                moviesm = new Moviesm();
                moviesm.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                moviesm.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                moviesm.setRelease_date(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RELEASE_DATE)));
                moviesm.setVote_average(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_VOTE_AVERAGE)));
                moviesm.setVote_count(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VOTE_COUNT)));
                moviesm.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OVERVIEW)));
                moviesm.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSTER_PATH)));
                moviesm.setBackdrop_path(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BACK_PATH)));

                arrayList.add(moviesm);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    //save/insert data
    public long insertMovie(Moviesm moviesm) {
        ContentValues args = new ContentValues();
        args.put(COLUMN_TITLE, moviesm.getTitle());
        args.put(COLUMN_RELEASE_DATE, moviesm.getRelease_date());
        args.put(COLUMN_VOTE_AVERAGE, moviesm.getVote_average());
        args.put(COLUMN_VOTE_COUNT, moviesm.getVote_count());
        args.put(COLUMN_OVERVIEW, moviesm.getOverview());
        args.put(COLUMN_POSTER_PATH, moviesm.getPoster_path());
        args.put(COLUMN_BACK_PATH, moviesm.getBackdrop_path());
        return database.insert(DATABASE_TABLE, null, args);
    }

    //update
    public int updateMovie(Moviesm moviesm) {
        ContentValues args = new ContentValues();
        args.put(COLUMN_TITLE, moviesm.getTitle());
        args.put(COLUMN_RELEASE_DATE, moviesm.getRelease_date());
        args.put(COLUMN_VOTE_AVERAGE, moviesm.getVote_average());
        args.put(COLUMN_VOTE_COUNT, moviesm.getVote_count());
        args.put(COLUMN_OVERVIEW, moviesm.getOverview());
        args.put(COLUMN_POSTER_PATH, moviesm.getPoster_path());
        args.put(COLUMN_BACK_PATH, moviesm.getBackdrop_path());
        return database.update(DATABASE_TABLE, args, _ID + "= '" + moviesm.getId() + "'", null);
    }

    //delete
    public int deleteMovie(int id) {
        return database.delete(TABLE_MOVIE, _ID + " = '" + id + "'", null);
    }   //delete
    public int deleteMovieByTitle(String title) {
        return database.delete(TABLE_MOVIE, COLUMN_TITLE + " = '" + title + "'", null);
    }
}
