package com.example.readdit.ui.myreviews;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readdit.R;
import com.example.readdit.model.Review;
import com.example.readdit.model.ReviewWithUserDetails;
import com.example.readdit.model.User;

import java.util.Collections;
import java.util.List;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyReviewsViewHolder holder, int position) {
        List<Review> reviews = viewModel.getMyReviews().getValue();
        Collections.sort(reviews, (r1, r2) -> { return r2.getDateTime().compareTo(r1.getDateTime()); });
        ReviewWithUserDetails review = new ReviewWithUserDetails();
        Review currReview = reviews.get(position);
        review.setReview(currReview);
        review.setUserId(currReview.getUserId());
        List<User> users = viewModel.getAllUsers().getValue();
        User user = users.stream().filter(u -> u.getUserID().equals(review.getUserId())).findFirst().orElse(null);
        if(user != null) {
            review.setUsername(user.getFullName());
            review.setUserImage(user.getImageUri());
        }
        holder.bindData(review, position);
    }

    @Override
    public int getItemCount() {
        if(viewModel.getMyReviews().getValue() != null) { return viewModel.getMyReviews().getValue().size(); } return 0;
    }
}
