package com.example.readdit.ui.reviews;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.readdit.ReadditApplication;
import com.example.readdit.model.Model;
import com.example.readdit.model.Review;
import com.example.readdit.model.User;

public class NewReviewViewModel extends ViewModel {
    public LiveData<User> getCurrentUser() {
        return ReadditApplication.currUser;
    }
    public void uploadImage(Bitmap imageBmp, String folder, String name, Model.AsyncListener<String> listener) {
        Model.instance.uploadImage(imageBmp, folder, name, listener);
    }
    public void addReview(Review review, Model.AsyncListener listener) {
        Model.instance.addReview(review, listener);
    }
}
