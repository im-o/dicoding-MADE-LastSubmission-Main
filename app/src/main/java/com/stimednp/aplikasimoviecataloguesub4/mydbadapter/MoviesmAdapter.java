package com.stimednp.aplikasimoviecataloguesub4.mydbadapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stimednp.aplikasimoviecataloguesub4.R;
import com.stimednp.aplikasimoviecataloguesub4.addingmethod.AllOtherMethod;
import com.stimednp.aplikasimoviecataloguesub4.addingmethod.CustomeOnItemClickListener;
import com.stimednp.aplikasimoviecataloguesub4.myactivity.DetailsMovieActivity;
import com.stimednp.aplikasimoviecataloguesub4.mydbentity.Moviesm;

import java.util.ArrayList;

/**
 * Created by rivaldy on 8/19/2019.
 */

public class MoviesmAdapter extends RecyclerView.Adapter<MoviesmAdapter.MoviesmViewHolder> {
    public static final String TAG = MoviesmAdapter.class.getSimpleName();
    private Activity mActivity;
    private ArrayList<Moviesm> moviesmList;

    public MoviesmAdapter(Activity mActivity, ArrayList<Moviesm> moviesmList) {
        this.mActivity = mActivity;
        this.moviesmList = moviesmList;
    }

    public ArrayList<Moviesm> getmoviesmList() {
        return moviesmList;
    }

    public void setListMoviesm(ArrayList<Moviesm> listMoviesm) {
        if (listMoviesm.size() > 0) {
            this.moviesmList.clear();
        }
        this.moviesmList.addAll(listMoviesm);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MoviesmAdapter.MoviesmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_list_movie, parent, false);
        return new MoviesmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesmAdapter.MoviesmViewHolder holder, int position) {
        holder.bind(moviesmList.get(position));
        holder.cardViewDesc.setOnClickListener(new CustomeOnItemClickListener(position, new CustomeOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Toast.makeText(mActivity, "KLIK", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mActivity, DetailsMovieActivity.class);
                intent.putExtra(DetailsMovieActivity.EXTRA_WHERE_FROM, TAG);
                intent.putExtra(DetailsMovieActivity.EXTRA_MOVIE, getmoviesmList().get(position));
//                activity.startActivity(intent);
                mActivity.startActivityForResult(intent, DetailsMovieActivity.REQUEST_ADD);
            }
        }));
    }

    @Override
    public int getItemCount() {
        if (moviesmList == null) {
            return 0;
        } else {
            return moviesmList.size();
        }
    }

    class MoviesmViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewImg, cardViewDesc, cardViewRating;
        TextView tvTitle, tvRelease, tvRating, tvDesc;
        ImageView imgvPoster;
        RecyclerView recyclerView;

        MoviesmViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvRelease = itemView.findViewById(R.id.tv_item_release);
            tvRating = itemView.findViewById(R.id.tv_item_rating);
            tvDesc = itemView.findViewById(R.id.tv_item_desc);
            imgvPoster = itemView.findViewById(R.id.img_item_photo);
            cardViewImg = itemView.findViewById(R.id.card_img);
            cardViewDesc = itemView.findViewById(R.id.card_view_desc);
            cardViewRating = itemView.findViewById(R.id.card_view_rating);
            recyclerView = itemView.findViewById(R.id.rv_tab_movies_room);
        }

        void bind(Moviesm movieItems) {
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
            Glide.with(mActivity)
                    .load(pathImg + imgUrl)
                    .into(imgvPoster);
        }
    }

    public void addItem(Moviesm moviesm) {
        this.moviesmList.add(moviesm);
        notifyItemInserted(moviesmList.size() - 1);
    }

    public void updateItem(int position, Moviesm moviesm) {
        this.moviesmList.set(position, moviesm);
        notifyItemChanged(position, moviesm);
    }

    public void removeItem(int position) {
        this.moviesmList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, moviesmList.size());
    }
}
