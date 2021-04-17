package com.example.readdit.ui.reviews;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.readdit.R;
import com.example.readdit.model.Model;
import com.example.readdit.model.Review;

import java.util.List;

public class ReviewsFragment extends Fragment {
    ReviewsViewModel viewModel;
    SwipeRefreshLayout refreshLayout;

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

        viewModel = new ViewModelProvider(this).get(ReviewsViewModel.class);

        ReviewsAdapter adapter = new ReviewsAdapter(getLayoutInflater(), viewModel);
        rv.setAdapter(adapter);

        adapter.setOnClickListener(new ReviewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("TAG","row was clicked " + position);
                String id = viewModel.getAllReviews().getValue().get(position).getId();
                NavDirections action = ReviewsFragmentDirections.actionReviewsFragmentToReviewFragment(id);
                Navigation.findNavController(view).navigate(action);
            }
        });

        // handle refresh
        refreshLayout = view.findViewById(R.id.reviews_swipe);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                reloadData();

            }
        });

        viewModel.getAllReviews().observe(getViewLifecycleOwner(), new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    void reloadData(){
        Model.instance.refreshAllReviews(new Model.GetAllReviewsListener() {
            @Override
            public void onComplete() {
                refreshLayout.setRefreshing(false);
            }
        });
    }
}