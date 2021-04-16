package com.example.readdit.ui.reviews;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readdit.R;
import com.example.readdit.model.Review;
import com.squareup.picasso.Picasso;

public class ReviewsViewHolder extends RecyclerView.ViewHolder{
    public ReviewsAdapter.OnItemClickListener listener;
    TextView book;
    TextView author;
    TextView category;
    TextView date;
    RatingBar rating;
    TextView username;
    ImageView image;
    ImageView userImage;
    int position;

    public ReviewsViewHolder(@NonNull View itemView) {
        super(itemView);
        book = itemView.findViewById(R.id.review_name_tv);
        author = itemView.findViewById(R.id.review_author_tv);
        category = itemView.findViewById(R.id.review_category_tv);
        date = itemView.findViewById(R.id.review_date_tv);
        rating = itemView.findViewById(R.id.review_ratingbar);
        username = itemView.findViewById(R.id.review_username_tv);
        image = itemView.findViewById(R.id.review_bookimage);
        userImage = itemView.findViewById(R.id.drawer_profile_img);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });
    }

    public void bindData(Review review, int position) {
        book.setText(review.getBook());
        author.setText(review.getAuthor());
        category.setText(review.getCategory());
        date.setText(review.getDate());
        rating.setRating((float)review.getRating());
        username.setText(review.getUsername());
        if(review.getImage() != null) { Picasso.get().load(review.getImage()).placeholder(R.drawable.book_placeholder).into(image); }
        if(review.getUserImage() != null) { Picasso.get().load(review.getUserImage()).placeholder(R.drawable.profile_placeholder).into(userImage); }
        this.position = position;
    }
}
