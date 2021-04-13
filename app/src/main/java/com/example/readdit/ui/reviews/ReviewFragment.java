package com.example.readdit.ui.reviews;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.readdit.R;
import com.example.readdit.ui.reviews.ReviewFragmentArgs;

public class ReviewFragment extends Fragment {

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_review, container, false);
        String reviewId = ReviewFragmentArgs.fromBundle(getArguments()).getReviewId();
        Log.d("TAG","review id is:" + reviewId);
        // use picasso
        return view;
    }
}