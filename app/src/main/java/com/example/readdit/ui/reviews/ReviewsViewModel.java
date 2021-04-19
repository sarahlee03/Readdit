package com.example.readdit.ui.reviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.readdit.model.Model;
import com.example.readdit.model.Review;
import com.example.readdit.model.User;

import java.util.List;

public class ReviewsViewModel extends ViewModel {
    private LiveData<List<Review>> reviewsList;
    private LiveData<List<User>> users;

    public ReviewsViewModel(){
        reviewsList = Model.instance.getAllReviews();
        users = Model.instance.getAllUsers(null);
    }

    public LiveData<List<Review>> getAllReviews(){
        return reviewsList;
    }

    public LiveData<List<User>> getAllUsers(){
        return users;
    }
}
