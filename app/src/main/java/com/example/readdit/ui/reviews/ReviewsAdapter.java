package com.example.readdit.ui.reviews;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readdit.R;
import com.example.readdit.model.Model;
import com.example.readdit.model.Review;
import com.example.readdit.model.ReviewDislike;
import com.example.readdit.model.ReviewLike;
import com.example.readdit.model.ReviewWithLikesAndDislikes;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsViewHolder>{
    LayoutInflater inflater;
    ReviewsViewModel viewModel;

    public ReviewsAdapter(LayoutInflater inflater, ReviewsViewModel viewModel){
        this.inflater = inflater;
        this.viewModel = viewModel;
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    private OnItemClickListener listener;

    public void setOnClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_review,parent,false);

        ReviewsViewHolder holder = new ReviewsViewHolder(view);
        holder.listener = listener;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        ReviewWithLikesAndDislikes reviewWithLikesAndDislikes = new ReviewWithLikesAndDislikes();
        reviewWithLikesAndDislikes.review = viewModel.getAllReviews().getValue().get(position);

        Model.instance.getAllReviewLikes(reviewWithLikesAndDislikes.review.getId(), new Model.GetReviewLikesListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(List<ReviewLike> reviewLikes) {
                reviewWithLikesAndDislikes.likes = reviewLikes.stream().map(ReviewLike::getUserId).collect(Collectors.toList());
                Model.instance.getAllReviewDislikes(reviewWithLikesAndDislikes.review.getId(), new Model.GetReviewDislikesListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(List<ReviewDislike> reviewDislikes) {
                        reviewWithLikesAndDislikes.dislikes = reviewDislikes.stream().map(ReviewDislike::getUserId).collect(Collectors.toList());
                        holder.bindData(reviewWithLikesAndDislikes, position);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        if(viewModel.getAllReviews().getValue() != null) { return viewModel.getAllReviews().getValue().size(); } return 0;
    }
}
