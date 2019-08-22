package com.stimednp.aplikasimoviecataloguesub4.addingmethod;

import android.view.View;

/**
 * Created by rivaldy on 8/3/2019.
 */

public class CustomeOnItemClickListener implements View.OnClickListener {
    private int position;
    private OnItemClickCallback onItemClickCallback;

    public CustomeOnItemClickListener(int position, OnItemClickCallback onItemClickCallback) {
        this.position = position;
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public void onClick(View view) {
        onItemClickCallback.onItemClicked(view, position);
    }

    public interface OnItemClickCallback {
        void onItemClicked(View view, int position);
    }
}
