package com.example.readdit.ui.reviews;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.readdit.R;
import com.example.readdit.model.Model;
import com.example.readdit.model.Review;
import com.example.readdit.ui.reviews.ReviewFragmentArgs;
import com.squareup.picasso.Picasso;

public class ReviewFragment extends Fragment {
    TextView book;
    TextView author;
    TextView category;
    TextView date;
    RatingBar rating;
    TextView username;
    ImageView image;
    ImageView userImage;

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

        book = view.findViewById(R.id.review_name_tv);
        author = view.findViewById(R.id.review_author_tv);
        category = view.findViewById(R.id.review_category_tv);
        date = view.findViewById(R.id.review_date_tv);
        rating = view.findViewById(R.id.review_ratingbar);
        username = view.findViewById(R.id.review_username_tv);
        image = view.findViewById(R.id.review_bookimage);
        userImage = view.findViewById(R.id.drawer_profile_img);

        Model.instance.getReview(reviewId, new Model.GetReviewListener() {
            @Override
            public void onComplete(Review review) {
                book.setText(review.getBook());
                author.setText(review.getAuthor());
                category.setText(review.getCategory());
                date.setText(review.getDate());
                rating.setRating(((float) review.getRating()));
                username.setText(review.getUsername());
                if(review.getImage() != null) { Picasso.get().load(review.getImage()).placeholder(R.drawable.book_placeholder).into(image); }
                if(review.getUserImage() != null) { Picasso.get().load(review.getUserImage()).placeholder(R.drawable.profile_placeholder).into(userImage); }
                }
        });

        return view;
    }
}