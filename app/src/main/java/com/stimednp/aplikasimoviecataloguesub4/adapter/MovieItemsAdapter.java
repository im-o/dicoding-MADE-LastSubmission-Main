package com.stimednp.aplikasimoviecataloguesub4.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import com.stimednp.aplikasimoviecataloguesub4.mymodel.MovieItems;

import java.util.ArrayList;

import static com.stimednp.aplikasimoviecataloguesub4.mydb.DatabaseContract.MovieColumns.CONTENT_URI;

/**
 * Created by rivaldy on 8/4/2019.
 */

public class MovieItemsAdapter extends RecyclerView.Adapter<MovieItemsAdapter.MovieItemsViewHolder> {
    public static final String TAG = MovieItemsAdapter.class.getSimpleName();
    private ArrayList<MovieItems> moviesData = new ArrayList<>();
    private Activity activity;

    public MovieItemsAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setMoviesData(ArrayList<MovieItems> list) {
        moviesData.clear();
        moviesData.addAll(list);
        notifyDataSetChanged();
    }

    private ArrayList<MovieItems> getMoviesData() {
        return moviesData;
    }

    @NonNull
    @Override
    public MovieItemsAdapter.MovieItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_list_movie, parent, false);
        return new MovieItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieItemsAdapter.MovieItemsViewHolder holder, int position) {
        holder.bind(getMoviesData().get(position));
        holder.cardViewDesc.setOnClickListener(new CustomeOnItemClickListener(position, new CustomeOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, DetailsMovieActivity.class);
                Uri uri = Uri.parse(CONTENT_URI + "/" + getMoviesData().get(position).getId());
                intent.setData(uri);
                intent.putExtra(DetailsMovieActivity.EXTRA_WHERE_FROM, TAG);
                intent.putExtra(DetailsMovieActivity.EXTRA_MOVIE, getMoviesData().get(position));
                activity.startActivityForResult(intent, DetailsMovieActivity.REQUEST_FAV);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return moviesData.size();
    }

    class MovieItemsViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewImg, cardViewDesc, cardViewRating;
        TextView tvTitle, tvRelease, tvRating, tvDesc;
        ImageView imgvPoster;

        MovieItemsViewHolder(@NonNull View itemView) {
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

        void bind(MovieItems movieItems) {
            String pathImg = "https://image.tmdb.org/t/p/w300_and_h450_bestv2";
            String title = movieItems.getTitle();
            String release = movieItems.getRelease_date();
            String voteValue = movieItems.getVote_average().toString();
            String overView = movieItems.getOverview();
            String imgUrl = movieItems.getPoster_path();

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
                Glide.with(activity)
                        .load(pathImg + imgUrl)
                        .into(imgvPoster);
            }
        }
    }
}
