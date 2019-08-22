package com.stimednp.aplikasimoviecataloguesub4.mydb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.stimednp.aplikasimoviecataloguesub4.mydbentity.MoviesModel;

import java.util.ArrayList;

import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_BACK_PATH;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_OVERVIEW;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_POSTER_PATH;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_RELEASE_DATE;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_TITLE;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_VOTE_AVERAGE;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.COLUMN_VOTE_COUNT;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.ID;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.TABLE_NAME;

/**
 * Created by rivaldy on 8/19/2019.
 */

public class MoviesHelper {
    private static final String TAG = MoviesHelper.class.getSimpleName();
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DatabaseHelper dataBaseHelper;
    private static MoviesHelper INSTANCE;

    private static SQLiteDatabase database;

    public MoviesHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static MoviesHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MoviesHelper(context);
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
            Log.d(TAG, "query : database.close();");
    }

    //CRUD
    public ArrayList<MoviesModel> query() {
        ArrayList<MoviesModel> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                ID + " DESC",
                null);
        cursor.moveToFirst();
        MoviesModel moviesModel;
        if (cursor.getCount() > 0) {
            do {
                moviesModel = new MoviesModel();
                moviesModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                moviesModel.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                moviesModel.setRelease_date(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RELEASE_DATE)));
                moviesModel.setVote_average(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_VOTE_AVERAGE)));
                moviesModel.setVote_count(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VOTE_COUNT)));
                moviesModel.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OVERVIEW)));
                moviesModel.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSTER_PATH)));
                moviesModel.setBackdrop_path(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BACK_PATH)));

                arrayList.add(moviesModel);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        Log.d(TAG, "query : cursor.close();");
        database.close();
        cursor.close();
//        dataBaseHelper.close();
        return arrayList;
    }

//    public ArrayList<MoviesModel> getAllMovies() {
//        ArrayList<MoviesModel> arrayList = new ArrayList<>();
//        Cursor cursor = database.query(DATABASE_TABLE, null,
//                null,
//                null,
//                null,
//                null,
//                ID + " ASC",
//                null);
//        cursor.moveToFirst();
//        MoviesModel moviesModel;
//        if (cursor.getCount() > 0) {
//            do {
//                moviesModel = new MoviesModel();
//                moviesModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
//                moviesModel.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
//                moviesModel.setRelease_date(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RELEASE_DATE)));
//                moviesModel.setVote_average(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_VOTE_AVERAGE)));
//                moviesModel.setVote_count(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VOTE_COUNT)));
//                moviesModel.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OVERVIEW)));
//                moviesModel.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSTER_PATH)));
//                moviesModel.setBackdrop_path(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BACK_PATH)));
//
//                arrayList.add(moviesModel);
//                cursor.moveToNext();
//            } while (!cursor.isAfterLast());
//        }
//        cursor.close();
//        return arrayList;
//    }

    //save/insert data

    public long insert(MoviesModel moviesModel){
        ContentValues initialValues = new ContentValues();
        initialValues.put(ID, moviesModel.getId());
        initialValues.put(ID, moviesModel.getId());
        initialValues.put(COLUMN_TITLE, moviesModel.getTitle());
        initialValues.put(COLUMN_RELEASE_DATE, moviesModel.getRelease_date());
        initialValues.put(COLUMN_VOTE_AVERAGE, moviesModel.getVote_average());
        initialValues.put(COLUMN_VOTE_COUNT, moviesModel.getVote_count());
        initialValues.put(COLUMN_OVERVIEW, moviesModel.getOverview());
        initialValues.put(COLUMN_POSTER_PATH, moviesModel.getPoster_path());
        initialValues.put(COLUMN_BACK_PATH, moviesModel.getBackdrop_path());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

//    public long insertMovie(MoviesModel moviesModel) {
//        ContentValues args = new ContentValues();
//        args.put(ID, moviesModel.getId());
//        args.put(COLUMN_TITLE, moviesModel.getTitle());
//        args.put(COLUMN_RELEASE_DATE, moviesModel.getRelease_date());
//        args.put(COLUMN_VOTE_AVERAGE, moviesModel.getVote_average());
//        args.put(COLUMN_VOTE_COUNT, moviesModel.getVote_count());
//        args.put(COLUMN_OVERVIEW, moviesModel.getOverview());
//        args.put(COLUMN_POSTER_PATH, moviesModel.getPoster_path());
//        args.put(COLUMN_BACK_PATH, moviesModel.getBackdrop_path());
//        return database.insert(DATABASE_TABLE, null, args);
//    }

    //update
//    public int updateMovie(MoviesModel moviesModel) {
//        ContentValues args = new ContentValues();
//        args.put(ID, moviesModel.getId());
//        args.put(COLUMN_TITLE, moviesModel.getTitle());
//        args.put(COLUMN_RELEASE_DATE, moviesModel.getRelease_date());
//        args.put(COLUMN_VOTE_AVERAGE, moviesModel.getVote_average());
//        args.put(COLUMN_VOTE_COUNT, moviesModel.getVote_count());
//        args.put(COLUMN_OVERVIEW, moviesModel.getOverview());
//        args.put(COLUMN_POSTER_PATH, moviesModel.getPoster_path());
//        args.put(COLUMN_BACK_PATH, moviesModel.getBackdrop_path());
//        return database.update(DATABASE_TABLE, args, ID + "= '" + moviesModel.getId() + "'", null);
//    }
//
//
// delete
    public int delete(int id) {
        return database.delete(TABLE_NAME, ID + " = '" + id + "'", null);
    }   //delete
//    public int deleteMovie(int id) {
//        return database.delete(TABLE_NAME, ID + " = '" + id + "'", null);
//    }
    //delete
//
//    public int deleteMovieByTitle(String title) {
//        return database.delete(TABLE_NAME, COLUMN_TITLE + " = '" + title + "'", null);
//    }

    //for provider
    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null,
                ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                ID + " ASC");
    }

    public long insertProvider(ContentValues values){
        return database.insert(DATABASE_TABLE, null, values);
    }
    public int updateProvider(String id, ContentValues values){
        return database.update(DATABASE_TABLE, values, ID + " = ?", new String[]{id});
    }
    public int deleteProvider(String id){
        return database.delete(DATABASE_TABLE, ID + " = ?", new String[]{id});
    }
}
