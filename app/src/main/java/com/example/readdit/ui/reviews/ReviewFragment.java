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

public class ReviewFragment extends Fragment {
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
    View view;
    TextView summaryt;
    TextView textReviewt;
    ConstraintLayout constraintLayout;

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_review, container, false);
        busy = view.findViewById(R.id.review_progress);

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
        line = view.findViewById(R.id.review_line);
        like = view.findViewById(R.id.review_like_button);
        dislike = view.findViewById(R.id.review_dislike_button);
        summaryt = view.findViewById(R.id.review_row_summaryt_tv);
        textReviewt = view.findViewById(R.id.review_reviewt_tv);
        constraintLayout = view.findViewById(R.id.constraintLayout);

        // for review row
        edit.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        busy.setVisibility(View.GONE);
        like.setEnabled(false);
        dislike.setEnabled(false);
        summary.setVisibility(View.GONE);
        textReview.setVisibility(View.GONE);
        summaryt.setVisibility(View.GONE);
        textReviewt.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams params = new ConstraintLayout
                .LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        constraintLayout.setLayoutParams(params);
        line.setVisibility(View.VISIBLE);

        return view;
    }
}