package com.example.readdit.ui.reviews;

import android.content.Intent;
import android.os.Bundle;

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

public class ReviewFragment extends Fragment {
    Review currReview;
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

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_review, container, false);
        String reviewId = ReviewFragmentArgs.fromBundle(getArguments()).getReviewId();
        Log.d("TAG","review id is:" + reviewId);

        busy = view.findViewById(R.id.review_progress);
        busy.setVisibility(View.VISIBLE);

        book = view.findViewById(R.id.review_name_tv);
        author = view.findViewById(R.id.review_author_tv);
        category = view.findViewById(R.id.review_category_tv);
        date = view.findViewById(R.id.review_date_tv);
        rating = view.findViewById(R.id.review_ratingbar);
        username = view.findViewById(R.id.review_username_tv);
        image = view.findViewById(R.id.review_bookimage);
        userImage = view.findViewById(R.id.drawer_profile_img);
        summary = view.findViewById(R.id.review_summary_tv);
        textReview = view.findViewById(R.id.review_review_tv);
        edit = view.findViewById(R.id.review_edit_icon);
        delete = view.findViewById(R.id.review_delete_icon);
        edit.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.INVISIBLE);
        like = view.findViewById(R.id.review_like_button);
        dislike = view.findViewById(R.id.review_dislike_button);

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
                    username.setText(review.getUsername());
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