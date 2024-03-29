package com.example.readdit.model;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private String userId;
    private String summary;
    private String review;
    private String date;
    private String image;
    private boolean isDeleted;
    private Long lastUpdated;

    public String getLikes() {
        return likes != null ? likes : "";
    }

    public int getLikesCount() {
        return getLikes().isEmpty() ? 0 : getLikes().split(",").length;
    }

    public int getDislikesCount() {
        return getDislikes().isEmpty() ? 0 : getDislikes().split(",").length;
    }

    public void addLike(String userId) {
        String str = getLikes().isEmpty() ? userId : getLikes() + "," + userId;
        setLikes(str);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void removeLike(String userId) {
        ArrayList<String> str = new ArrayList<String>(Arrays.asList(getLikes().split(",")));
        str.remove(userId);
        setLikes(String.join(",", str));
    }

    public void addDislike(String userId) {
        String str = getDislikes().isEmpty() ? userId : getDislikes() + "," + userId;
        setDislikes(str);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void removeDislike(String userId) {
        ArrayList<String> str = new ArrayList<String>(Arrays.asList(getDislikes().split(",")));
        str.remove(userId);
        setDislikes(String.join(",", str));
    }


    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getDislikes() {
        return dislikes != null ? dislikes : "";
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }

    private String likes;
    private String dislikes;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDateTime getDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss");
        return LocalDateTime.parse(getDate(), formatter);
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
        result.put("userId", userId);
        result.put("date", date);
        result.put("summary", summary);
        result.put("review", review);
        result.put("likes", likes);
        result.put("dislikes", dislikes);
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
        summary = (String)map.get("summary");
        review = (String)map.get("review");
        image = (String)map.get("image");
        userId = (String)map.get("userId");
        likes = (String)map.get("likes");
        dislikes = (String)map.get("dislikes");
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
