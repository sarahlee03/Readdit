package com.example.readdit.ui.reviews;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.readdit.model.Model;
import com.example.readdit.model.Review;
import com.example.readdit.model.User;

public class EditReviewViewModel extends ViewModel {
    private LiveData<Review> review;

    public LiveData<Review> getReviewById(String reviewId){
        review = Model.instance.getReviewById(reviewId);
        return review;
    }
    public void uploadImage(Bitmap imageBmp, String folder, String name, Model.AsyncListener<String> listener) {
        Model.instance.uploadImage(imageBmp, folder, name, listener);
    }
    public void editReview(Review review, Model.AsyncListener listener) {
        Model.instance.editReview(review, listener);
    }
}
