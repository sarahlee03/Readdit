package com.example.readdit.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

public class ReviewLike {
    public String reviewId;

    public String getUserId() {
        return userId;
    }

    public String userId;

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("reviewId", reviewId);
        result.put("userId", userId);
        return result;
    }

    public ReviewLike fromMap(Map<String, Object> map) {
        reviewId = (String)map.get("reviewId");
        userId = (String)map.get("userId");
        return this;
    }
}
