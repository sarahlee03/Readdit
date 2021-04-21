package com.example.readdit.ui.reviews;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readdit.R;
import com.example.readdit.model.Model;
import com.example.readdit.model.Review;
import com.example.readdit.model.ReviewWithUserDetails;
import com.like.LikeButton;
import com.squareup.picasso.Picasso;

public class ReviewsViewHolder extends RecyclerView.ViewHolder{
    public ReviewsAdapter.OnItemClickListener listener;
    TextView book;
    TextView author;
    TextView category;
    TextView date;
    RatingBar rating;
    TextView username;
    TextView summary;
    TextView textReview;
    ImageView image;
    ImageView userImage;
    ProgressBar busy;
    ImageButton delete;
    ImageButton edit;
    LikeButton like;
    LikeButton dislike;
    View line;
    LinearLayout summaryLayout;
    LinearLayout reviewLayout;
    ConstraintLayout constraintLayout;
    TextView likes;
    TextView dislikes;
    protected int position;

    public ReviewsViewHolder(@NonNull View itemView) {
        super(itemView);
        busy = itemView.findViewById(R.id.review_progress);

        book = itemView.findViewById(R.id.review_name_tv);
        author = itemView.findViewById(R.id.review_author_tv);
        category = itemView.findViewById(R.id.review_category_tv);
        date = itemView.findViewById(R.id.review_date_tv);
        rating = itemView.findViewById(R.id.review_ratingbar);
        username = itemView.findViewById(R.id.review_username_tv);
        image = itemView.findViewById(R.id.review_bookimage);
        userImage = itemView.findViewById(R.id.drawer_profile_img);
        summary = itemView.findViewById(R.id.review_summary_tv);
        textReview = itemView.findViewById(R.id.review_review_tv);
        edit = itemView.findViewById(R.id.review_edit_icon);
        delete = itemView.findViewById(R.id.review_delete_icon);
        line = itemView.findViewById(R.id.review_line);
        like = itemView.findViewById(R.id.review_like_button);
        dislike = itemView.findViewById(R.id.review_dislike_button);
        summaryLayout = itemView.findViewById(R.id.layout_summary);
        reviewLayout = itemView.findViewById(R.id.layout_review);
        constraintLayout = itemView.findViewById(R.id.constraintLayout);
        likes = itemView.findViewById(R.id.review_likes_tv);
        dislikes = itemView.findViewById(R.id.review_dislikes_tv);

        edit.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        busy.setVisibility(View.GONE);
        like.setEnabled(false);
        dislike.setEnabled(false);
        reviewLayout.setVisibility(View.GONE);
        summaryLayout.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams params = new ConstraintLayout
                .LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        constraintLayout.setLayoutParams(params);
        line.setVisibility(View.VISIBLE);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });
    }

    public void bindData(ReviewWithUserDetails reviewWithUserDetails, int position) {
        Review review = reviewWithUserDetails.getReview();
        like.setLiked(review.getLikes().contains(Model.instance.getCurrentUserID()));
        dislike.setLiked(review.getDislikes().contains(Model.instance.getCurrentUserID()));
        book.setText(review.getBook());
        author.setText(review.getAuthor());
        category.setText(review.getCategory());
        date.setText(review.getDate());
        rating.setRating((float)review.getRating());
        username.setText(reviewWithUserDetails.getUsername());
        likes.setText(String.valueOf(review.getLikesCount()));
        dislikes.setText(String.valueOf(review.getDislikesCount()));
        if(review.getImage() != null) { Picasso.get().load(review.getImage()).placeholder(R.drawable.book_placeholder).into(image); }
        if(reviewWithUserDetails.getUserImage() != null) { Picasso.get().load(reviewWithUserDetails.getUserImage()).placeholder(R.drawable.profile_placeholder).into(userImage); }
        this.position = position;
    }
}
