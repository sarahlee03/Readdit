package com.example.readdit.ui.reviews;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.readdit.model.User;

import java.util.List;

public class ReviewsFragment extends Fragment {
    ReviewsViewModel viewModel;
    SwipeRefreshLayout refreshLayout;
    View view;

    public ReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reviews, container, false);
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

                ReviewsFragmentDirections.ActionReviewsFragmentToReviewDetailsFragment direc = ReviewsFragmentDirections.actionReviewsFragmentToReviewDetailsFragment(id);
                Navigation.findNavController(view).navigate(direc);
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

        viewModel.getAllUsers().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter.notifyDataSetChanged();
            }
        });


        return view;
    }

    void reloadData(){
        Model.instance.refreshAllUsers(new Model.AsyncListener() {
            @Override
            public void onComplete(Object data) {
                Model.instance.refreshAllReviews(new Model.AsyncListener() {
                    @Override
                    public void onComplete(Object data) {
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }
}