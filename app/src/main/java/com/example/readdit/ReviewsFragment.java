package com.example.readdit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViewsService;

import com.example.readdit.adapters.reviews.ReviewsAdapter;
import com.example.readdit.model.Model;
import com.example.readdit.model.Review;

import java.util.List;

public class ReviewsFragment extends Fragment {

    public ReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);
        RecyclerView rv = view.findViewById(R.id.reviewsfrag_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);

        List<Review> data = Model.instance.getAllReviews();

        ReviewsAdapter adapter = new ReviewsAdapter(getLayoutInflater());
        adapter.data = data;
        rv.setAdapter(adapter);

        adapter.setOnClickListener(new ReviewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("TAG","row was clicked " + position);

            }
        });

        return view;
    }
}