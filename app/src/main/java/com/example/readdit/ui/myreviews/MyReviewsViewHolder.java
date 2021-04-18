package com.example.readdit.ui.myreviews;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readdit.R;
import com.example.readdit.model.Review;
import com.example.readdit.model.ReviewWithLikesAndDislikes;
import com.example.readdit.ui.reviews.ReviewsViewHolder;
import com.squareup.picasso.Picasso;

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

    public void bindData(ReviewWithLikesAndDislikes review, int position) {
        super.bindData(review, position);
    }
}
