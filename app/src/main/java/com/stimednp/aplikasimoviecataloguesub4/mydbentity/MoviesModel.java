package com.stimednp.aplikasimoviecataloguesub4.mydbentity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract;

import static android.provider.BaseColumns._ID;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.getColumnDouble;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.getColumnInt;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.getColumnString;

/**
 * Created by rivaldy on 8/19/2019.
 */

public class MoviesModel implements Parcelable {
    private int id;
    private String title;
    private String release_date;
    private Double vote_average;
    private String vote_count;
    private String overview;
    private String poster_path;
    private String backdrop_path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.release_date);
        dest.writeValue(this.vote_average);
        dest.writeString(this.vote_count);
        dest.writeString(this.overview);
        dest.writeString(this.poster_path);
        dest.writeString(this.backdrop_path);
    }

    public MoviesModel() {
    }

    public MoviesModel(int id, String title, String release_date, Double vote_average, String vote_count,
                       String overview, String poster_path, String backdrop_path) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.overview = overview;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
    }

    public MoviesModel(Cursor cursor) {
        this.id = getColumnInt(cursor, DatabaseContract.MovieColumns.ID);
        this.title = getColumnString(cursor, DatabaseContract.MovieColumns.COLUMN_TITLE);
        this.release_date = getColumnString(cursor, DatabaseContract.MovieColumns.COLUMN_RELEASE_DATE);
        this.vote_average = getColumnDouble(cursor, DatabaseContract.MovieColumns.COLUMN_VOTE_AVERAGE);
        this.vote_count = getColumnString(cursor, DatabaseContract.MovieColumns.COLUMN_VOTE_COUNT);
        this.overview = getColumnString(cursor, DatabaseContract.MovieColumns.COLUMN_OVERVIEW);
        this.poster_path = getColumnString(cursor, DatabaseContract.MovieColumns.COLUMN_POSTER_PATH);
        this.backdrop_path = getColumnString(cursor, DatabaseContract.MovieColumns.COLUMN_BACK_PATH);;
    }

    private MoviesModel(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.release_date = in.readString();
        this.vote_average = (Double) in.readValue(Double.class.getClassLoader());
        this.vote_count = in.readString();
        this.overview = in.readString();
        this.poster_path = in.readString();
        this.backdrop_path = in.readString();
    }

    public static final Creator<MoviesModel> CREATOR = new Creator<MoviesModel>() {
        @Override
        public MoviesModel createFromParcel(Parcel source) {
            return new MoviesModel(source);
        }

        @Override
        public MoviesModel[] newArray(int size) {
            return new MoviesModel[size];
        }
    };
}
