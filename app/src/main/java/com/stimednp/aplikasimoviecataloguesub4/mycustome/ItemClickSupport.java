package com.stimednp.aplikasimoviecataloguesub4.mycustome;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stimednp.aplikasimoviecataloguesub4.R;

/**
 * Created by rivaldy on 8/20/2019.
 */

public class ItemClickSupport {
    private RecyclerView mRecyclerView;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                mOnItemClickListener.onItemClicked(mRecyclerView, holder.getAdapterPosition(), v);
            }
        }
    };

    private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (mOnItemLongClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                mOnItemLongClickListener.onItemClicked(mRecyclerView, holder.getAdapterPosition(), v);
            }
            return false;
        }
    };

    private RecyclerView.OnChildAttachStateChangeListener mStateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(@NonNull View view) {
            if (mOnItemClickListener != null) {
                view.setOnClickListener(mOnClickListener);
            }
            if (mOnItemLongClickListener != null) {
                view.setOnLongClickListener(mOnLongClickListener);
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(@NonNull View view) {

        }
    };

    private ItemClickSupport(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mRecyclerView.setTag(R.id.item_id_clicks, this);
        mRecyclerView.addOnChildAttachStateChangeListener(mStateChangeListener);
    }

    public static ItemClickSupport addTo(RecyclerView recyclerView) {
        ItemClickSupport itemClickSupport = (ItemClickSupport) recyclerView.getTag(R.id.item_id_clicks);
        if (itemClickSupport == null) {
            itemClickSupport = new ItemClickSupport(recyclerView);
        }
        return itemClickSupport;
    }

    public static ItemClickSupport removeFrom(RecyclerView recyclerView) {
        ItemClickSupport itemClickSupport = (ItemClickSupport) recyclerView.getTag(R.id.item_id_clicks);
        if (itemClickSupport != null) {
            itemClickSupport.detach(recyclerView);
        }
        return itemClickSupport;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        mOnItemLongClickListener = longClickListener;
    }

    private void detach(RecyclerView recyclerView) {
        recyclerView.removeOnChildAttachStateChangeListener(mStateChangeListener);
        recyclerView.setTag(R.id.item_id_clicks);
    }

    public interface OnItemClickListener {
        void onItemClicked(RecyclerView recyclerView, int position, View view);
    }

    public interface OnItemLongClickListener {
        void onItemClicked(RecyclerView recyclerView, int position, View view);
    }
}
