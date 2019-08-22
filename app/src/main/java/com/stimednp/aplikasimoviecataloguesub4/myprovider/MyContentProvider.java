package com.stimednp.aplikasimoviecataloguesub4.myprovider;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stimednp.aplikasimoviecataloguesub4.mydb.MoviesHelper;
import com.stimednp.aplikasimoviecataloguesub4.myfragment.FavMoviesFragment;

import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.AUTHORITY;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.TABLE_NAME;

/**
 * Created by rivaldy on 8/19/2019.
 */

@SuppressLint("Registered")
public class MyContentProvider extends ContentProvider {
    private static final int MOVIES = 1;
    private static final int MOVIES_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private MoviesHelper moviesHelper;

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIES);
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", MOVIES_ID);
    }

    @Override
    public boolean onCreate() {
        moviesHelper = MoviesHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        moviesHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                cursor = moviesHelper.queryProvider();
                break;
            case MOVIES_ID:
                cursor = moviesHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        moviesHelper.open();
        long added;
        if (sUriMatcher.match(uri) == MOVIES) {
            added = moviesHelper.insertProvider(values);
        } else {
            added = 0;
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(CONTENT_URI, new FavMoviesFragment.DataObserver(new Handler(), getContext()));
        }
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        moviesHelper.open();
        int deleted;
        if (sUriMatcher.match(uri) == MOVIES_ID) {
            deleted = moviesHelper.deleteProvider(uri.getLastPathSegment());
        } else {
            deleted = 0;
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(CONTENT_URI, new FavMoviesFragment.DataObserver(new Handler(), getContext()));
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        moviesHelper.open();
        int updated;
        if (sUriMatcher.match(uri) == MOVIES_ID) {
            updated = moviesHelper.updateProvider(uri.getLastPathSegment(), values);
        } else {
            updated = 0;
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(CONTENT_URI, new FavMoviesFragment.DataObserver(new Handler(), getContext()));
        }
        return updated;
    }
}
