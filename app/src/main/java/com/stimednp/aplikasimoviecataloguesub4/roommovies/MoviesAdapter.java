package com.stimednp.aplikasimoviecataloguesub4.roommovies;

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

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    public static final String TAG = MoviesAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<Movies> moviesList;

    public MoviesAdapter(Context context, ArrayList<Movies> moviesList) {
        this.context = context;
        this.moviesList = moviesList;
    }

    public void setMoviesList(ArrayList<Movies> moviesList) {
        this.moviesList = moviesList;
        notifyDataSetChanged();
    }

    private ArrayList<Movies> getMoviesList() {
        return moviesList;
    }

    @NonNull
    @Override
    public MoviesAdapter.MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_list_movie, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.MoviesViewHolder holder, int position) {
        holder.bind(moviesList.get(position));
        holder.cardViewDesc.setOnClickListener(new CustomeOnItemClickListener(position, new CustomeOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(context, DetailsMovieActivity.class);
                intent.putExtra(DetailsMovieActivity.EXTRA_WHERE_FROM, TAG);
                intent.putExtra(DetailsMovieActivity.EXTRA_MOVIE, getMoviesList().get(position));
                context.startActivity(intent);
            }
        }));
    }

    @Override
    public int getItemCount() {
        if (moviesList == null) {
            return 0;
        } else {
            return moviesList.size();
        }
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewImg, cardViewDesc, cardViewRating;
        TextView tvTitle, tvRelease, tvRating, tvDesc;
        ImageView imgvPoster;

        MoviesViewHolder(@NonNull View itemView) {
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

        void bind(Movies movieItems) {
            String pathImg = "https://image.tmdb.org/t/p/w300_and_h450_bestv2";
            String title = movieItems.getTitle();
            String release = movieItems.getRelease_date();
            String voteValue = movieItems.getVote_average().toString();
            String overView = movieItems.getOverview();
            String imgUrl = movieItems.getPoster_path();

            AllOtherMethod allOtherMethod = new AllOtherMethod();
            String myDate = allOtherMethod.changeFormatDate(release);
            tvTitle.setText(title);
            tvRating.setText(voteValue);
            tvDesc.setText(overView);
            tvRelease.setText(myDate);
            Glide.with(context)
                    .load(pathImg + imgUrl)
                    .into(imgvPoster);
        }
    }
}
