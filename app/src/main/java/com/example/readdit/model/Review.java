package com.example.readdit.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Review {
    @PrimaryKey
    @NonNull
    private String id;
    private String book;
    private String author;
    private String category;
    private double rating;
    private String username;
    private String userId;
    private String userImage;
    private String summary;
    private String review;
    private String date;
    private String image;
    private boolean isDeleted;
    private Long lastUpdated;

    public String getImage() {
        return image;
    }

    public void setImage(@NonNull String image) {
        this.image = image;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("book", book);
        result.put("author", author);
        result.put("category", category);
        result.put("rating", rating);
        result.put("username", username);
        result.put("userImage", userImage);
        result.put("userId", userId);
        result.put("date", date);
        result.put("summary", summary);
        result.put("review", review);
        result.put("image", image);
        result.put("lastUpdated", FieldValue.serverTimestamp());
        result.put("isDeleted", isDeleted);
        return result;
    }

    @NonNull
    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(@NonNull String userImage) {
        this.userImage = userImage;
    }

    public Review fromMap(Map<String, Object> map) {
        id = (String)map.get("id");
        book = (String)map.get("book");
        author = (String)map.get("author");
        category = (String)map.get("category");
        rating = (double)map.get("rating");
        date = (String)map.get("date");
        username = (String)map.get("username");
        summary = (String)map.get("summary");
        review = (String)map.get("review");
        image = (String)map.get("image");
        userImage = (String)map.get("userImage");
        userId = (String)map.get("userId");
        lastUpdated = (Long)((Timestamp)map.get("lastUpdated")).getSeconds();
        isDeleted = ((boolean)map.get("isDeleted"));
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
