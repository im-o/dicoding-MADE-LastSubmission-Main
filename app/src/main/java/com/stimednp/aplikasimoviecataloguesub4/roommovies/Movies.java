package com.stimednp.aplikasimoviecataloguesub4.roommovies;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by rivaldy on 8/4/2019.
 */
@Entity(tableName = Movies.TABLE_NAME)
public class Movies implements Parcelable {
    public static final String TABLE_NAME = "movies";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ORI_TITLE = "original_title";
    public static final String COLUMN_RELEASE_DATE = "release_date";
    public static final String COLUMN_VOTE_AVERAGE = "vote_average";
    public static final String COLUMN_VOTE_COUNT = "vote_count";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_POSTER_PATH = "poster_path";
    public static final String COLUMN_BACK_PATH = "backdrop_path";
    public static final String COLUMN_ISFAVORITE = "isFavorite";


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public int id;

    @ColumnInfo(name = COLUMN_TITLE)
    private String title;

    @ColumnInfo(name = COLUMN_ORI_TITLE)
    private String original_title;

    @ColumnInfo(name = COLUMN_RELEASE_DATE)
    private String release_date;

    @ColumnInfo(name = COLUMN_VOTE_AVERAGE)
    private Double vote_average;

    @ColumnInfo(name = COLUMN_VOTE_COUNT)
    private String vote_count;

    @ColumnInfo(name = COLUMN_OVERVIEW)
    private String overview;

    @ColumnInfo(name = COLUMN_POSTER_PATH)
    private String poster_path;

    @ColumnInfo(name = COLUMN_BACK_PATH)
    private String backdrop_path;

    @ColumnInfo(name = COLUMN_ISFAVORITE )
    private boolean isFavorite;

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

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
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

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static Movies fromContentValues(ContentValues values){
        final Movies movies = new Movies();
        if (values.containsKey(COLUMN_ID)){
            movies.id = values.getAsInteger(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_TITLE)){
            movies.title = values.getAsString(COLUMN_TITLE);
        }
        return movies;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.original_title);
        dest.writeString(this.release_date);
        dest.writeValue(this.vote_average);
        dest.writeString(this.vote_count);
        dest.writeString(this.overview);
        dest.writeString(this.poster_path);
        dest.writeString(this.backdrop_path);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
    }

    public Movies() {
    }

    protected Movies(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.original_title = in.readString();
        this.release_date = in.readString();
        this.vote_average = (Double) in.readValue(Double.class.getClassLoader());
        this.vote_count = in.readString();
        this.overview = in.readString();
        this.poster_path = in.readString();
        this.backdrop_path = in.readString();
        this.isFavorite = in.readByte() != 0;
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel source) {
            return new Movies(source);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };
}
