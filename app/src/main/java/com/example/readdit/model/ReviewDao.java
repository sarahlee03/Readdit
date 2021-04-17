package com.example.readdit.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReviewDao {
    @Query("select * from Review order by date desc")
    LiveData<List<Review>> getAllReviews();

    @Query("select * from Review where userId=:userId order by date desc")
    LiveData<List<Review>> getReviewsByUserId(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Review... reviews);

    @Delete
    void delete(Review review);
}
