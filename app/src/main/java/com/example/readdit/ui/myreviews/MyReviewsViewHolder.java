package com.example.readdit.ui.myreviews;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.readdit.model.ReviewWithUserDetails;
import com.example.readdit.ui.reviews.ReviewsViewHolder;

public class MyReviewsViewHolder extends ReviewsViewHolder {
    public MyReviewsAdapter.OnItemClickListener listener;

    public MyReviewsViewHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });
    }

    public void bindData(ReviewWithUserDetails review, int position) {
        super.bindData(review, position);
    }
}
