package com.example.readdit.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    private String userID;
    @NonNull
    private String fullName;
    @NonNull
    private String email;
    @NonNull
    private String imageUri;
    private Long lastUpdated;

    // region Constructors


    public User(@NonNull String userID, @NonNull String fullName, @NonNull String email, @NonNull String imageUri) {
        this.userID = userID;
        this.fullName = fullName;
        this.email = email;
        this.imageUri = imageUri;
    }

    User(){}
    // endregion

    // region Getters
    @NonNull
    public String getUserID() {
        return userID;
    }

    @NonNull
    public String getFullName() {
        return fullName;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getImageUri() {
        return imageUri;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }
    // endregion

    // region Setters
    public void setUserID(@NonNull String userID) {
        this.userID = userID;
    }

    public void setFullName(@NonNull String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public void setImageUri(@NonNull String imageUri) {
        this.imageUri = imageUri;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    // endregion

    // region Functions
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userID", userID);
        result.put("fullName", fullName);
        result.put("email", email);
        result.put("imageUri", imageUri);
        result.put("lastUpdated", FieldValue.serverTimestamp());
        return result;
    }

    public User fromMap(Map<String, Object> map) {
        userID = (String)map.get("userID");
        fullName = (String)map.get("fullName");
        email = (String)map.get("email");
        imageUri = (String)map.get("imageUri");
        lastUpdated = ((Timestamp)map.get("lastUpdated")).getSeconds();
        return this;
    }
    // endregion
}
