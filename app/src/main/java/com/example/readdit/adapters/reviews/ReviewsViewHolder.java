package com.example.readdit.adapters.reviews;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readdit.R;
import com.example.readdit.model.Review;

public class ReviewsViewHolder extends RecyclerView.ViewHolder{
    public ReviewsAdapter.OnItemClickListener listener;
    TextView id;
    TextView book;
    TextView description;
    int position;

    public ReviewsViewHolder(@NonNull View itemView) {
        super(itemView);
        id = itemView.findViewById(R.id.id_textView);
        book = itemView.findViewById(R.id.book_textView);
        description = itemView.findViewById(R.id.description_textView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });
    }

    public void bindData(Review review, int position) {
        Log.d("TAG",review.book);
        id.setText(review.id);
        book.setText(review.book);
        description.setText(review.description);
        this.position = position;
    }
}
