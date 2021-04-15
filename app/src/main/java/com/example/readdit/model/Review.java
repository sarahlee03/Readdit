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
    @NonNull
    private String book;
    @NonNull
    private String author;
    @NonNull
    private String category;
    @NonNull
    private double rating;
    @NonNull
    private String username;
    @NonNull
    private String summary;
    @NonNull
    private String review;
    @NonNull
    private String date;
    @NonNull
    private String image;
    @NonNull
    private boolean isDeleted;
    @NonNull
    private Long lastUpdated;

    @NonNull
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
        result.put("date", date);
        result.put("summary", summary);
        result.put("review", review);
        result.put("image", image);
        result.put("lastUpdated", FieldValue.serverTimestamp());
        result.put("isDeleted", isDeleted);
        return result;
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
        lastUpdated = (Long)((Timestamp)map.get("lastUpdated")).getSeconds();
        isDeleted = ((boolean)map.get("isDeleted"));
        return this;
    }
}
