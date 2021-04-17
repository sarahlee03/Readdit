package com.example.readdit.ui.reviews;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;

import com.example.readdit.R;
import com.example.readdit.ReadditApplication;
import com.example.readdit.model.Model;
import com.example.readdit.model.Review;
import com.example.readdit.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import static com.example.readdit.R.layout.new_review_activity;

public class EditReviewActivity extends NewReviewActivity {
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Edit Review");

        String reviewId = getIntent().getStringExtra("reviewId");
        busy.setVisibility(View.VISIBLE);

        Model.instance.getReviewById(reviewId, new Model.GetReviewListener() {
            @Override
            public void onComplete(Review review) {
                book.setText(review.getBook());
                author.setText(review.getAuthor());
                category.setText(review.getCategory());
                rating.setRating(((float) review.getRating()));
                summary.setText(review.getSummary());
                textReview.setText(review.getReview());
                if(review.getImage() != null) { Picasso.get().load(review.getImage()).placeholder(R.drawable.book_placeholder).into(bookImage); }

                busy.setVisibility(View.INVISIBLE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //editReview();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
