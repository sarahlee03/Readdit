package com.example.readdit.ui.myreviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.readdit.model.Model;
import com.example.readdit.model.Review;

import java.util.List;

public class MyReviewsViewModel extends ViewModel {
    private LiveData<List<Review>> reviewsList;

    public MyReviewsViewModel(){
        reviewsList = Model.instance.getMyReviews();
    }

    public LiveData<List<Review>> getMyReviews(){
        return reviewsList;
    }

}