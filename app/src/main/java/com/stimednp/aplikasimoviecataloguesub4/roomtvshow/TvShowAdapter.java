package com.stimednp.aplikasimoviecataloguesub4.roomtvshow;

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

import java.util.ArrayList;

/**
 * Created by rivaldy on 8/4/2019.
 */

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder> {
    public static final String TAG = TvShowAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<TvShow> tvshowList;

    public TvShowAdapter(Context context, ArrayList<TvShow> tvshowList) {
        this.context = context;
        this.tvshowList = tvshowList;
    }

    public void setTvshowList(ArrayList<TvShow> tvshowList) {
        this.tvshowList = tvshowList;
        notifyDataSetChanged();
    }

    private ArrayList<TvShow> getTvshowList() {
        return tvshowList;
    }

    @NonNull
    @Override
    public TvShowAdapter.TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_list_movie, parent, false);
        return new TvShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowAdapter.TvShowViewHolder holder, int position) {
        holder.bind(tvshowList.get(position));
        holder.cardViewDesc.setOnClickListener(new CustomeOnItemClickListener(position, new CustomeOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(context, DetailsMovieActivity.class);
                intent.putExtra(DetailsMovieActivity.EXTRA_WHERE_FROM, TAG);
                intent.putExtra(DetailsMovieActivity.EXTRA_MOVIE, getTvshowList().get(position));
                context.startActivity(intent);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return tvshowList.size();
    }

    class TvShowViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewImg, cardViewDesc, cardViewRating;
        TextView tvTitle, tvRelease, tvRating, tvDesc;
        ImageView imgvPoster;

        TvShowViewHolder(@NonNull View itemView) {
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

        void bind(TvShow tvShow) {
            String pathImg = "https://image.tmdb.org/t/p/w300_and_h450_bestv2";
            String title = tvShow.getName();
            String release = tvShow.getFirst_air_date();
            String voteValue = tvShow.getVote_average().toString();
            String overView = tvShow.getOverview();
            String imgUrl = tvShow.getPoster_path();

            AllOtherMethod allOtherMethod = new AllOtherMethod();
            String myDate = allOtherMethod.changeFormatDate(release);

            tvTitle.setText(title);
            tvRelease.setText(myDate);
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
