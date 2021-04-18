package com.example.readdit.model;

import java.util.HashMap;
import java.util.Map;

public class ReviewDislike {
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

    public ReviewDislike fromMap(Map<String, Object> map) {
        reviewId = (String)map.get("reviewId");
        userId = (String)map.get("userId");
        return this;
    }
}
