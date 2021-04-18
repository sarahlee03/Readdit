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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    SwipeRefreshLayout refreshLayout;

    public MyReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.my_reviews_fragment, container, false);
        RecyclerView rv = view.findViewById(R.id.myreviewsfrag_list);
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
                MyReviewsFragmentDirections.ActionNavMyReviewsToReviewDetailsFragment direc = MyReviewsFragmentDirections.actionNavMyReviewsToReviewDetailsFragment(id);
                Navigation.findNavController(view).navigate(direc);
            }
        });

        viewModel.getMyReviews().observe(getViewLifecycleOwner(), new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                adapter.notifyDataSetChanged();
            }
        });

        // handle refresh
        refreshLayout = view.findViewById(R.id.myreviews_swipe);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                reloadData();
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