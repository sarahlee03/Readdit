package com.example.readdit.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ReviewWithUserDetails {
    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    Review review;
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String userId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    String username;
    String userImage;
}
