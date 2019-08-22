package com.stimednp.aplikasimoviecataloguesub4.mymodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rivaldy on 8/3/2019.
 */

public class TvShowItems implements Parcelable {
    private String name;
    private String original_name;
    private String first_air_date;
    private Double vote_average;
    private String vote_count;
    private String overview;
    private String poster_path;
    private String backdrop_path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public String getVote_count() {
        return vote_count;
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

    public String getBackdrop_path() {
        return backdrop_path;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.original_name);
        dest.writeString(this.first_air_date);
        dest.writeValue(this.vote_average);
        dest.writeString(this.vote_count);
        dest.writeString(this.overview);
        dest.writeString(this.poster_path);
        dest.writeString(this.backdrop_path);
    }

    private TvShowItems(Parcel in) {
        this.name = in.readString();
        this.original_name = in.readString();
        this.first_air_date = in.readString();
        this.vote_average = (Double) in.readValue(Double.class.getClassLoader());
        this.vote_count = in.readString();
        this.overview = in.readString();
        this.poster_path = in.readString();
        this.backdrop_path = in.readString();
    }

    public static final Parcelable.Creator<TvShowItems> CREATOR = new Parcelable.Creator<TvShowItems>() {
        @Override
        public TvShowItems createFromParcel(Parcel source) {
            return new TvShowItems(source);
        }

        @Override
        public TvShowItems[] newArray(int size) {
            return new TvShowItems[size];
        }
    };
}
