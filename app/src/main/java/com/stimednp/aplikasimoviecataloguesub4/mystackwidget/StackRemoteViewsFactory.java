package com.stimednp.aplikasimoviecataloguesub4.mystackwidget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.stimednp.aplikasimoviecataloguesub4.R;
import com.stimednp.aplikasimoviecataloguesub4.mydbentity.MoviesModel;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.CONTENT_URI;

/**
 * Created by rivaldy on 8/17/2019.
 */

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Cursor cursor;
    private ArrayList<MoviesModel> moviesList = new ArrayList<>();


    StackRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        if (moviesList.size() != 0) {
            moviesList.clear();
        }
        final long identifyToken = Binder.clearCallingIdentity();
        //load query
        cursor = mContext.getContentResolver().query(CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                MoviesModel moviesModel = new MoviesModel(cursor);
                moviesList.add(moviesModel);
            }
        }
        Binder.restoreCallingIdentity(identifyToken);
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public int getCount() {
        if (moviesList == null) {
            return 0;
        } else {
            return moviesList.size();
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        if (moviesList.size() != 0) {
            Object urlImg = moviesList.get(position).getBackdrop_path();
            if (urlImg != null) {
                String pathImg = "https://image.tmdb.org/t/p/w500" + urlImg.toString();
                try {
                    Bitmap bitmap = Glide.with(mContext)
                            .asBitmap()
                            .load(pathImg)
                            .submit(512, 512)
                            .get();

                    mRemoteViews.setImageViewBitmap(R.id.imgv_item_widget, bitmap);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        Bundle extras = new Bundle();
        extras.putInt(ImageBannerWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        mRemoteViews.setOnClickFillInIntent(R.id.imgv_item_widget, fillInIntent);
        return mRemoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return cursor.moveToPosition(position) ? cursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
