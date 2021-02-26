package com.example.readdit.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.readdit.ReadditApplication;

import java.util.List;

public class Model {
    public final static Model instance = new Model();
    private ModelFirebase modelFirebase = new ModelFirebase();
    private ModelSql modelSql = new ModelSql();
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
                .getLong("UsersLastUpdateDate", 0);
        modelFirebase.getAllUsers(lastUpdated, new AsyncListener<List<User>>() {
            @Override
            public void onComplete(List<User> data) {
                final long[] lastUpdated = {0};

                for (User user : data) {
                    modelSql.insertUser(user, new AsyncListener() {
                        @Override
                        public void onComplete(Object data) {
                            if(user.getLastUpdated() > lastUpdated[0]) {
                                lastUpdated[0] = user.getLastUpdated();
                            }
                        }
                    });
                }

                ReadditApplication.context
                        .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                        .edit()
                        .putLong("lastUpdated", lastUpdated[0])
                        .apply();

                if (listener != null) {
                    listener.onComplete(null);
                }
            }
        });
    }
    // endregion
}
