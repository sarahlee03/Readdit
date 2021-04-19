package com.example.readdit.ui.myreviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.readdit.model.Model;
import com.example.readdit.model.Review;
import com.example.readdit.model.User;

import java.util.List;

public class MyReviewsViewModel extends ViewModel {
    private LiveData<List<Review>> reviewsList;
    private LiveData<List<User>> users;

    public MyReviewsViewModel(){
        reviewsList = Model.instance.getMyReviews();
        users = Model.instance.getAllUsers(null);
    }

    public LiveData<List<Review>> getMyReviews(){
        return reviewsList;
    }

    public LiveData<List<User>> getAllUsers(){
        return users;
    }
}