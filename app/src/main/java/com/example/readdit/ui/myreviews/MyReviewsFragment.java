package com.example.readdit.ui.myreviews;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.readdit.R;
import com.example.readdit.model.Model;
import com.example.readdit.model.Review;
import com.example.readdit.ui.reviews.ReviewsFragmentDirections;

import java.util.List;

public class MyReviewsFragment extends Fragment {
    MyReviewsViewModel viewModel;

    public MyReviewsFragment() {
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

        viewModel = new ViewModelProvider(this).get(MyReviewsViewModel.class);

        MyReviewsAdapter adapter = new MyReviewsAdapter(getLayoutInflater(), viewModel);
        rv.setAdapter(adapter);

        adapter.setOnClickListener(new MyReviewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("TAG","row was clicked " + position);
                String id = viewModel.getMyReviews().getValue().get(position).getId();
                NavDirections action = MyReviewsFragmentDirections.actionNavMyReviewsToReviewFragment(id);
                Navigation.findNavController(view).navigate(action);
            }
        });

        viewModel.getMyReviews().observe(getViewLifecycleOwner(), new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}