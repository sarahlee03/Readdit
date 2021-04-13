package com.example.readdit.ui.reviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readdit.R;
import com.example.readdit.model.Review;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsViewHolder>{
    public List<Review> data;
    LayoutInflater inflater;

    public ReviewsAdapter(LayoutInflater inflater){
        this.inflater = inflater;
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
        View view = inflater.inflate(R.layout.review_row,parent,false);
        ReviewsViewHolder holder = new ReviewsViewHolder(view);
        holder.listener = listener;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        Review review = data.get(position);

        holder.bindData(review, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
