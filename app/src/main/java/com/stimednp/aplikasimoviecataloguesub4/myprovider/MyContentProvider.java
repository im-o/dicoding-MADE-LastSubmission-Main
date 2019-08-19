package com.stimednp.aplikasimoviecataloguesub4.myprovider;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stimednp.aplikasimoviecataloguesub4.roomdb.MoviesRoomDatabase;
import com.stimednp.aplikasimoviecataloguesub4.roommovies.Movies;
import com.stimednp.aplikasimoviecataloguesub4.roommovies.MoviesDao;

/**
 * Created by rivaldy on 8/19/2019.
 */

@SuppressLint("Registered")
public class MyContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.stimednp.aplikasimoviecataloguesub4.myprovider";
    public static final Uri URI_MOVIES = Uri.parse("content://" + AUTHORITY + "/" + Movies.TABLE_NAME);
    private static final int CODE_MOVIES_DIR = 1;
    private static final int CODE_MOVIES_ITEM = 2;
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, Movies.TABLE_NAME, CODE_MOVIES_DIR);
        MATCHER.addURI(AUTHORITY, Movies.TABLE_NAME + "/*", CODE_MOVIES_ITEM);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_MOVIES_DIR || code == CODE_MOVIES_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }

            MoviesDao moviesDao = MoviesRoomDatabase.getDatabase(context).moviesDao();
            final Cursor cursor;
            if (code == CODE_MOVIES_DIR) {
                cursor = moviesDao.selectAll();
            } else {
                cursor = moviesDao.selectById((int) ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_MOVIES_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + Movies.TABLE_NAME;
            case CODE_MOVIES_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + Movies.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
//        switch (MATCHER.match(uri)) {
//            case CODE_MOVIES_DIR:
//                final Context context = getContext();
//                if (context == null) {
//                    return null;
//                }
//                if (values != null) {
//                    final int id = MoviesRoomDatabase.getDatabase(context).moviesDao().insert(Movies.fromContentValues(values));
//                    context.getContentResolver().notifyChange(uri, null);
//                    return ContentUris.withAppendedId(uri, id);
//                }
//            case CODE_MOVIES_ITEM:
//                throw new IllegalArgumentException("Infalid URI, cannot insert with ID: " + uri);
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
//        switch (MATCHER.match(uri)){
//            case CODE_MOVIES_DIR:
//                throw new IllegalArgumentException("Infalid URI, cannot update without ID: " + uri);
//            case CODE_MOVIES_ITEM:
//                final Context context = getContext();
//                if (context == null){
//                    return 0;
//                }
//                final String count = MoviesRoomDatabase.getDatabase(context).moviesDao().deleteByTitle(String.valueOf(ContentUris.parseId(uri)));
//                context.getContentResolver().notifyChange(uri, null);
//                return Integer.parseInt(count);
//        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
