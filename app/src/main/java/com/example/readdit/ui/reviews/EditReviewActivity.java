package com.example.readdit.ui.reviews;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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

import java.util.List;

import static com.example.readdit.R.layout.new_review_activity;

public class EditReviewActivity extends NewReviewActivity {
    Review currReview;

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
        imageSelected = true;

        Model.instance.getReviewById(reviewId).observe(this, new Observer<Review>() {
            @Override
            public void onChanged(Review review) {
                if(review != null) {
                    currReview = review;
                    book.setText(review.getBook());
                    book.setEnabled(false);
                    author.setText(review.getAuthor());
                    author.setEnabled(false);
                    category.setText(review.getCategory());
                    rating.setRating(((float) review.getRating()));
                    summary.setText(review.getSummary());
                    textReview.setText(review.getReview());
                    if(review.getImage() != null) { Picasso.get().load(review.getImage()).placeholder(R.drawable.book_placeholder).into(bookImage); }

                    busy.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editReview();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void editReview() {
        if(!isFormValid()) { return; }
        busy();
        currReview.setCategory(category.getText().toString());
        currReview.setRating(rating.getRating());
        currReview.setSummary(summary.getText().toString());
        currReview.setReview(textReview.getText().toString());
        // save image
        if (bookImage.getDrawable() != null) {
            Bitmap bitMap = ((BitmapDrawable) bookImage.getDrawable()).getBitmap();
            Model.instance.uploadImage(bitMap, ReadditApplication.BOOKS_FOLDER, Model.instance.getCurrentUserID() + "/" + currReview.getBook(), new Model.AsyncListener<String>() {
                @Override
                // after image saved
                public void onComplete(String data) {
                    currReview.setImage(data);
                    Model.instance.editReview(currReview, new Model.AddReviewListener() {
                        @Override
                        public void onComplete() {
                            finish();
                        }
                    });
                }
            });
        }
    }
}
