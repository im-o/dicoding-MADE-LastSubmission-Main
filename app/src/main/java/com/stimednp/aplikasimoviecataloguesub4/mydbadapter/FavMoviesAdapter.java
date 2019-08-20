package com.stimednp.aplikasimoviecataloguesub4.mydbadapter;

import android.app.Activity;
import android.util.Log;
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
import com.stimednp.aplikasimoviecataloguesub4.mydbentity.FavMoviesModel;

import java.util.ArrayList;

/**
 * Created by rivaldy on 8/19/2019.
 */

public class FavMoviesAdapter extends RecyclerView.Adapter<FavMoviesAdapter.MoviesmViewHolder> {
    public static final String TAG = FavMoviesAdapter.class.getSimpleName();
    private Activity mActivity;
    private ArrayList<FavMoviesModel> favMoviesModelList = new ArrayList<>();

    public FavMoviesAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public ArrayList<FavMoviesModel> getmoviesmList() {
        return favMoviesModelList;
    }

    public void setListMoviesm(ArrayList<FavMoviesModel> listFavMoviesModel) {
        if (listFavMoviesModel.size() > 0) {
            this.favMoviesModelList.clear();
        }
        this.favMoviesModelList.addAll(listFavMoviesModel);
        notifyDataSetChanged();
    }

    //click custome
    private OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(FavMoviesModel favMoviesModel);
    }

    @NonNull
    @Override
    public FavMoviesAdapter.MoviesmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_favorite, parent, false);
        return new MoviesmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavMoviesAdapter.MoviesmViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(favMoviesModelList.get(holder.getAdapterPosition()));
            }
        });
        holder.bind(favMoviesModelList.get(position));
//        holder.cardViewDesc.setOnClickListener(new CustomeOnItemClickListener(position, new CustomeOnItemClickListener.OnItemClickCallback() {
//            @Override
//            public void onItemClicked(View view, int position) {
//                Toast.makeText(mActivity, "KLIK", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(mActivity, DetailsMovieActivity.class);
//                intent.putExtra(DetailsMovieActivity.EXTRA_WHERE_FROM, TAG);
//                intent.putExtra(DetailsMovieActivity.EXTRA_POSITION, position);
//                intent.putExtra(DetailsMovieActivity.EXTRA_MOVIE, getmoviesmList().get(position));
////                activity.startActivity(intent);
//                mActivity.startActivityForResult(intent, DetailsMovieActivity.REQUEST_ADD);
//            }
//        }));
    }

    @Override
    public int getItemCount() {
        if (favMoviesModelList == null) {
            return 0;
        } else {
            return favMoviesModelList.size();
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

        void bind(FavMoviesModel movieItems) {
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

    public void addItem(FavMoviesModel favMoviesModel) {
        this.favMoviesModelList.add(favMoviesModel);
        notifyItemInserted(favMoviesModelList.size() - 1);
    }

    public void updateItem(int position, FavMoviesModel favMoviesModel) {
        this.favMoviesModelList.set(position, favMoviesModel);
        notifyItemChanged(position, favMoviesModel);
    }

    public void removeItem(int position) {
        Log.d(TAG, "removeItem position : "+position);
        this.favMoviesModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favMoviesModelList.size());
    }

    public void reloadItem() {
        notifyDataSetChanged();
//        notifyItemRangeChanged(position, favMoviesModelList.size());
    }
}
