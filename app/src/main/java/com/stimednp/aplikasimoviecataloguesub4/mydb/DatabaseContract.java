package com.stimednp.aplikasimoviecataloguesub4.mydb;

import android.provider.BaseColumns;

/**
 * Created by rivaldy on 8/19/2019.
 */

class DatabaseContract {
    static String TABLE_MOVIE = "tbl_moviesm";

    static final class MovieColumns implements BaseColumns {
        public static String COLUMN_TITLE = "title";
        static String COLUMN_RELEASE_DATE = "release_date";
        static String COLUMN_VOTE_AVERAGE = "vote_average";
        static String COLUMN_VOTE_COUNT = "vote_count";
        static String COLUMN_OVERVIEW = "overview";
        static String COLUMN_POSTER_PATH = "poster_path";
        static String COLUMN_BACK_PATH = "backdrop_path";
    }
}
