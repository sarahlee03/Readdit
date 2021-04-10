package com.example.readdit.adapters.reviews;

import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readdit.R;
import com.example.readdit.model.Review;

public class ReviewsViewHolder extends RecyclerView.ViewHolder{
    public ReviewsAdapter.OnItemClickListener listener;
    TextView book;
    TextView author;
    TextView category;
    TextView date;
    RatingBar rating;
    TextView username;
    int position;

    public ReviewsViewHolder(@NonNull View itemView) {
        super(itemView);
        book = itemView.findViewById(R.id.review_name_tv);
        author = itemView.findViewById(R.id.review_author_tv);
        category = itemView.findViewById(R.id.review_category_tv);
        date = itemView.findViewById(R.id.review_date_tv);
        rating = itemView.findViewById(R.id.review_ratingbar);
        username = itemView.findViewById(R.id.review_username_tv);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });
    }

    public void bindData(Review review, int position) {
        Log.d("TAG",review.book);
        book.setText(review.book);
        author.setText(review.author);
        category.setText(review.category);
        date.setText(review.date);
        rating.setRating(review.rating);
        username.setText(review.username);
        this.position = position;
    }
}
