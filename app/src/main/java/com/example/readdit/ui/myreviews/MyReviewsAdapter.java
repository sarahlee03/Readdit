package com.example.readdit.ui.myreviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readdit.R;
import com.example.readdit.model.Review;

import java.util.List;

public class MyReviewsAdapter extends RecyclerView.Adapter<MyReviewsViewHolder>{
    LayoutInflater inflater;
    MyReviewsViewModel viewModel;

    public MyReviewsAdapter(LayoutInflater inflater, MyReviewsViewModel viewModel){
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
    public MyReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_review,parent,false);
        MyReviewsViewHolder holder = new MyReviewsViewHolder(view);
        holder.listener = listener;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyReviewsViewHolder holder, int position) {
        Review review = viewModel.getMyReviews().getValue().get(position);

        holder.bindData(review, position);
    }

    @Override
    public int getItemCount() {
        if(viewModel.getMyReviews().getValue() != null) { return viewModel.getMyReviews().getValue().size(); } return 0;
    }
}
