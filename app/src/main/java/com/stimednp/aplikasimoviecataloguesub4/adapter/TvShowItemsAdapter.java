package com.stimednp.aplikasimoviecataloguesub4.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stimednp.aplikasimoviecataloguesub4.R;
import com.stimednp.aplikasimoviecataloguesub4.addingmethod.AllOtherMethod;
import com.stimednp.aplikasimoviecataloguesub4.addingmethod.CustomeOnItemClickListener;
import com.stimednp.aplikasimoviecataloguesub4.myactivity.DetailsMovieActivity;
import com.stimednp.aplikasimoviecataloguesub4.mymodel.TvShowItems;

import java.util.ArrayList;

/**
 * Created by rivaldy on 8/4/2019.
 */

public class TvShowItemsAdapter extends RecyclerView.Adapter<TvShowItemsAdapter.TvShowItemsViewHolder> {
    public static final String TAG = TvShowItemsAdapter.class.getSimpleName();
    private ArrayList<TvShowItems> tvShowData = new ArrayList<>();
    private Context context;

    public TvShowItemsAdapter(Context context) {
        this.context = context;
    }

    public void setTvShowData(ArrayList<TvShowItems> tvShowData) {
        this.tvShowData.clear();
        this.tvShowData.addAll(tvShowData);
        notifyDataSetChanged();
    }

    private ArrayList<TvShowItems> getTvShowData() {
        return tvShowData;
    }

    @NonNull
    @Override
    public TvShowItemsAdapter.TvShowItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_list_movie, parent, false);
        return new TvShowItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowItemsAdapter.TvShowItemsViewHolder holder, int position) {
        holder.bind(getTvShowData().get(position));
        holder.cardViewDesc.setOnClickListener(new CustomeOnItemClickListener(position, new CustomeOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(context, DetailsMovieActivity.class);
                intent.putExtra(DetailsMovieActivity.EXTRA_WHERE_FROM, TAG);
                intent.putExtra(DetailsMovieActivity.EXTRA_MOVIE, getTvShowData().get(position));
                context.startActivity(intent);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return tvShowData.size();
    }

    class TvShowItemsViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewImg, cardViewDesc, cardViewRating;
        TextView tvTitle, tvRelease, tvRating, tvDesc;
        ImageView imgvPoster;

        TvShowItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvRelease = itemView.findViewById(R.id.tv_item_release);
            tvRating = itemView.findViewById(R.id.tv_item_rating);
            tvDesc = itemView.findViewById(R.id.tv_item_desc);
            imgvPoster = itemView.findViewById(R.id.img_item_photo);
            cardViewImg = itemView.findViewById(R.id.card_img);
            cardViewDesc = itemView.findViewById(R.id.card_view_desc);
            cardViewRating = itemView.findViewById(R.id.card_view_rating);
        }

        void bind(TvShowItems tvShowItems) {
            String pathImg = "https://image.tmdb.org/t/p/w300_and_h450_bestv2";
            String title = tvShowItems.getName();
            String release = tvShowItems.getFirst_air_date();
            String voteValue = tvShowItems.getVote_average().toString();
            String overView = tvShowItems.getOverview();
            String imgUrl = tvShowItems.getPoster_path();

            AllOtherMethod allOtherMethod = new AllOtherMethod();
            if (release != null) {
                String myDate = allOtherMethod.changeFormatDate(release);
                tvRelease.setText(myDate);
            } else {
                tvRelease.setText(release);
            }

            tvTitle.setText(title);
            tvRating.setText(voteValue);
            tvDesc.setText(overView);
            if (imgUrl != null) {
                Glide.with(context)
                        .load(pathImg + imgUrl)
                        .into(imgvPoster);
            }
        }
    }
}
