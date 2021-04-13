package com.example.readdit.model;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;

import com.example.readdit.ReadditApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Model {
    public final static Model instance = new Model();
    private ModelFirebase modelFirebase = new ModelFirebase();
    private ModelSql modelSql = new ModelSql();
    private LiveData<List<User>> usersList;
    final String PROFILES_FOLDER = "profiles";

    public interface AsyncListener<T> {
        void onComplete(T data);
    }

    // region Image functions
    public void uploadImage(Bitmap imageBmp, String folder, String name, AsyncListener<String> listener) {
        modelFirebase.uploadImage(imageBmp, folder, name, listener);
    }
    // endregion

    // region User functions
    public void addUser(User user, AsyncListener<Boolean> listener) {
        modelFirebase.addUser(user, listener);
    }

    public LiveData<List<User>> getAllUsers(AsyncListener listener) {
        if (usersList == null) {
            usersList = AppLocalDB.db.userDao().getAllUsers();
            refreshAllUsers(listener);
        }
        else {
            listener.onComplete(null);
        }

        return usersList;
    }

    public String getCurrentUserID() {
        return ModelFirebase.getCurrentUserID();
    }

    public LiveData<User> getUserById(String id) {
        refreshAllUsers(null);
        return modelSql.getUserById(id);
    }

    public void refreshAllUsers(AsyncListener listener) {
        Long lastUpdated = ReadditApplication.context
                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong("usersLastUpdateDate", 0);
        modelFirebase.getAllUsers(lastUpdated, new AsyncListener<List<User>>() {
            @Override
            public void onComplete(List<User> data) {
                long lastUpdated = 0;

                for (User user : data) {
                    if (user.isDeleted()) {
                        modelSql.deleteUser(user, null);
                    }
                    else {
                        modelSql.insertUser(user, null);
                    }

                    if(user.getLastUpdated() > lastUpdated) {
                        lastUpdated = user.getLastUpdated();
                    }
                }

                ReadditApplication.context
                        .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                        .edit()
                        .putLong("usersLastUpdateDate", lastUpdated)
                        .apply();

                if (listener != null) {
                    listener.onComplete(null);
                }
            }
        });
    }

    public void deleteUser(User user, AsyncListener<Boolean> listener) {
        user.setDeleted(true);
        this.addUser(user, new AsyncListener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                if (data) {
                    modelFirebase.deleteImage(user.getUserID(), new AsyncListener<Boolean>() {
                        @Override
                        public void onComplete(Boolean data) {
                            modelSql.deleteUser(user, listener);
                        }
                    }, PROFILES_FOLDER);
                }
            }
        });

        refreshAllUsers(null);
    }

    public List<Review> getAllReviews() {
        List<Review> data = new LinkedList<Review>();
        for(int i=0;i<10;i++) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = format.format(new Date());

            Review review = new Review();
            review.id = "" + i;
            review.book = "harry potter";
            review.author = "JK Rowling";
            review.category = "fantasy";
            review.date = dateString;
            review.username = "shahar freidenberg";
            review.rating = 3;
            data.add(review);
        }
        return data;
    }
}
