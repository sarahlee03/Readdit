package com.example.readdit.ui.reviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.readdit.model.Model;
import com.example.readdit.model.Review;

import java.util.LinkedList;
import java.util.List;

public class ReviewsViewModel extends ViewModel {
    private LiveData<List<Review>> reviewsList;

    public ReviewsViewModel(){
        reviewsList = Model.instance.getAllReviews();
    }

    public LiveData<List<Review>> getAllReviews(){
        return reviewsList;
    }
}
