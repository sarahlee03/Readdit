package com.example.readdit.ui.reviews;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.readdit.R;
import com.example.readdit.ReadditApplication;
import com.example.readdit.model.Model;
import com.example.readdit.model.Review;
import com.example.readdit.model.User;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

public class ReviewDetailsFragment extends ReviewFragment {
    Review currReview;

    public ReviewDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        String reviewId = ReviewFragmentArgs.fromBundle(getArguments()).getReviewId();
        Log.d("TAG","review id is:" + reviewId);

        busy.setVisibility(View.VISIBLE);
        // for review details
        busy.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);
        like.setEnabled(true);
        dislike.setEnabled(true);
        summary.setVisibility(View.VISIBLE);
        textReview.setVisibility(View.VISIBLE);
        summaryt.setVisibility(View.VISIBLE);
        textReviewt.setVisibility(View.VISIBLE);
        ConstraintLayout.LayoutParams params = new ConstraintLayout
                .LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        constraintLayout.setLayoutParams(params);
        line.setVisibility(View.GONE);

        Model.instance.getReviewById(reviewId).observe(getViewLifecycleOwner(), new Observer<Review>() {
            @Override
            public void onChanged(Review review) {
                if(review != null) {
                    currReview = review;
                    book.setText(review.getBook());
                    author.setText(review.getAuthor());
                    category.setText(review.getCategory());
                    date.setText(review.getDate());
                    rating.setRating(((float) review.getRating()));
                    username.setText
                            (review.getUsername());
                    summary.setText(review.getSummary());
                    textReview.setText(review.getReview());
                    if(review.getImage() != null) { Picasso.get().load(review.getImage()).placeholder(R.drawable.book_placeholder).into(image); }
                    if(review.getUserImage() != null) { Picasso.get().load(review.getUserImage()).placeholder(R.drawable.profile_placeholder).into(userImage); }

                    // edit and delete icons
                    // get the current user
                    ReadditApplication.currUser.observe(getActivity(), new Observer<User>() {
                        @Override
                        public void onChanged(User user) {
                            if(review.getUserId().equals(user.getUserID())) {
                                edit.setVisibility(View.VISIBLE);
                                delete.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    busy.setVisibility(View.INVISIBLE);
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open edit review activity - without killing the main activity
                Intent intent = new Intent(getActivity(), EditReviewActivity.class);
                intent.putExtra("reviewId", reviewId);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.instance.deleteReview(currReview, new Model.AddReviewListener() {
                    @Override
                    public void onComplete() {
                        Navigation.findNavController(view).popBackStack();
                    }
                });
            }
        });

        like.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });

        dislike.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });

        return view;
    }
}