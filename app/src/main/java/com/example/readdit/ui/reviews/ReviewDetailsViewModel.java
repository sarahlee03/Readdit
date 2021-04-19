package com.example.readdit.ui.reviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.readdit.ReadditApplication;
import com.example.readdit.model.Model;
import com.example.readdit.model.Review;
import com.example.readdit.model.User;

import java.util.List;

public class ReviewDetailsViewModel extends ViewModel {
    private LiveData<Review> review;
    private LiveData<User> user;

    public LiveData<Review> getReviewById(String reviewId){
        review = Model.instance.getReviewById(reviewId);
        return review;
    }

    public LiveData<User> getUserById(String userId){
        user = Model.instance.getUserById(userId);
        return user;
    }

    public void editReview(Review review) {
        Model.instance.editReview(review, null);
    }

    public void deleteReview(Review review, Model.AsyncListener listener) {
        Model.instance.deleteReview(review, listener);
    }

    public LiveData<User> getCurrentUser() {
        return ReadditApplication.currUser;
    }

}
