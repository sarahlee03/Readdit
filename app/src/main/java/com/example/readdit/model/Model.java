package com.example.readdit.model;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;

import com.example.readdit.ReadditApplication;

import java.util.List;

public class Model {
    public final static Model instance = new Model();
    private ModelFirebase modelFirebase = new ModelFirebase();
    private LiveData<List<User>> usersList;


    public interface AsyncListener<T> {
        void onComplete(T data);
    }

    // region Image functions
    public void uploadImage(Bitmap imageBmp, String name, AsyncListener<String> listener) {
        modelFirebase.uploadImage(imageBmp, name, listener);
    }
    // endregion

    // region User functions
    public void addUser(User user, AsyncListener<Boolean> listener) {
        modelFirebase.addUser(user, listener);
    }

    public LiveData<List<User>> getAllUsers() {
        if (usersList == null) {
            usersList = AppLocalDB.db.userDao().getAllUsers();
        }
        else {
            refreshAllUsers(null);
        }

        return usersList;
    }

    public void refreshAllUsers(AsyncListener listener) {
        Long lastUpdated = ReadditApplication.context
                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong("UsersLastUpdateDate", 0);
        modelFirebase.getAllUsers(lastUpdated, new AsyncListener<List<User>>() {
            @Override
            public void onComplete(List<User> data) {
                for (User user : data) {
                    long lastUpdated = 0;
                    AppLocalDB.db.userDao().insertAll(user);

                    if(user.getLastUpdated() > lastUpdated) {
                        lastUpdated = user.getLastUpdated();
                    }

                    ReadditApplication.context
                            .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                            .edit()
                            .putLong("lastUpdated", lastUpdated)
                            .apply();

                    if (listener != null) {
                        listener.onComplete(null);
                    }
                }
            }
        });
    }
    // endregion
}
