package com.example.readdit.ui.myreviews;

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
import com.example.readdit.model.ReviewLike;
import com.example.readdit.model.ReviewWithLikesAndDislikes;

import java.util.List;
import java.util.stream.Collectors;

public class MyReviewsAdapter extends RecyclerView.Adapter<MyReviewsViewHolder>{
    LayoutInflater inflater;
    MyReviewsViewModel viewModel;

    public MyReviewsAdapter(LayoutInflater inflater, MyReviewsViewModel viewModel){
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
    public MyReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_review,parent,false);
        MyReviewsViewHolder holder = new MyReviewsViewHolder(view);
        holder.listener = listener;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyReviewsViewHolder holder, int position) {
        ReviewWithLikesAndDislikes reviewWithLikesAndDislikes = new ReviewWithLikesAndDislikes();
        reviewWithLikesAndDislikes.review = viewModel.getMyReviews().getValue().get(position);
        Model.instance.getAllReviewLikes(reviewWithLikesAndDislikes.review.getId(), new Model.GetReviewLikesListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(List<ReviewLike> reviewLikes) {
                reviewWithLikesAndDislikes.likes = reviewLikes.stream().map(ReviewLike::getUserId).collect(Collectors.toList());
            }
        });

        holder.bindData(reviewWithLikesAndDislikes, position);
    }

    @Override
    public int getItemCount() {
        if(viewModel.getMyReviews().getValue() != null) { return viewModel.getMyReviews().getValue().size(); } return 0;
    }
}
