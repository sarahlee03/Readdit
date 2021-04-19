package com.example.readdit.ui.reviews;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
    EditReviewViewModel viewModel;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Edit Review");
        viewModel = new ViewModelProvider(this).get(EditReviewViewModel.class);

        String reviewId = getIntent().getStringExtra("reviewId");
        busy.setVisibility(View.VISIBLE);
        imageSelected = true;

        viewModel.getReviewById(reviewId).observe(this, new Observer<Review>() {
            @Override
            public void onChanged(Review review) {
                if(review != null) {
                    currReview = review;
                    book.setText(review.getBook());
                    book.setEnabled(false);
                    author.setText(review.getAuthor());
                    author.setEnabled(false);
                    //category.setText(review.getCategory());
                    rating.setRating(((float) review.getRating()));
                    summary.setText(review.getSummary());
                    textReview.setText(review.getReview());
                    if(review.getImage() != null) { Picasso.get().load(review.getImage()).placeholder(R.drawable.book_placeholder).into(bookImage); }
                    category.setSelection(getIndex(category, review.getCategory()));
                    busy.setVisibility(View.GONE);
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

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    private void editReview() {
        if(!isFormValid()) { return; }
        busy();
        currReview.setCategory(((TextView)category.getSelectedView()).getText().toString());
        currReview.setRating(rating.getRating());
        currReview.setSummary(summary.getText().toString());
        currReview.setReview(textReview.getText().toString());
        // save image
        if (bookImage.getDrawable() != null) {
            Bitmap bitMap = ((BitmapDrawable) bookImage.getDrawable()).getBitmap();
            viewModel.uploadImage(bitMap, ReadditApplication.BOOKS_FOLDER, Model.instance.getCurrentUserID() + "/" + currReview.getBook(), new Model.AsyncListener<String>() {
                @Override
                // after image saved
                public void onComplete(String data) {
                    currReview.setImage(data);
                    viewModel.editReview(currReview, new Model.AddReviewListener() {
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
