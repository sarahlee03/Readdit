package com.example.readdit;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.readdit.model.User;

public class ReadditApplication extends Application {
    public static Context context;
    public static LiveData<User> currUser;

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
