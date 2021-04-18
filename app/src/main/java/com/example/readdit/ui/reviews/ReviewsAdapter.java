package com.example.readdit.ui.reviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readdit.R;
import com.example.readdit.model.Review;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsViewHolder>{
    LayoutInflater inflater;
    ReviewsViewModel viewModel;

    public ReviewsAdapter(LayoutInflater inflater, ReviewsViewModel viewModel){
        this.inflater = inflater;
        this.viewModel = viewModel;
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    private OnItemClickListener listener;

    public void setOnClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_review,parent,false);

        ReviewsViewHolder holder = new ReviewsViewHolder(view);
        holder.listener = listener;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        Review review = viewModel.getAllReviews().getValue().get(position);

        holder.bindData(review, position);
    }

    @Override
    public int getItemCount() {
        if(viewModel.getAllReviews().getValue() != null) { return viewModel.getAllReviews().getValue().size(); } return 0;
    }
}
