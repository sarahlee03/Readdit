package com.example.readdit;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.readdit.model.User;

public class ReadditApplication extends Application {
    public static Context context;
    public static LiveData<User> currUser;
    public static final String USERS_COLLECTION = "users";
    public static final String REVIEWS_COLLECTION = "reviews";
    public static final String PROFILES_FOLDER = "profiles";
    public static final String BOOKS_FOLDER = "books";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        currUser = null;
    }

    public static void setCurrentUser(LiveData<User> user) {
        currUser = user;
    }
}
