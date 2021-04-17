package com.example.readdit.model;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

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
    private LiveData<List<Review>> reviewsList;
    private LiveData<List<Review>> myReviewsList;

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
                    }, ReadditApplication.PROFILES_FOLDER);
                }
            }
        });

        refreshAllUsers(null);
    }

    // reviews
    public LiveData<List<Review>> getAllReviews() {
        if (reviewsList == null) {
            reviewsList = modelSql.getAllReviews();
            refreshAllReviews(null);
        }
        return reviewsList;
    }

    public LiveData<List<Review>> getMyReviews() {
        if (myReviewsList == null) {
            myReviewsList = modelSql.getReviewsByUserId(getCurrentUserID());
            refreshAllReviews(null);
        }
        return myReviewsList;
    }

    public void refreshAllReviews(final GetAllReviewsListener listener) {
        Long lastUpdated = ReadditApplication.context
                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong("reviewsLastUpdateDate", 0);
        modelFirebase.getAllReviews(lastUpdated, new ModelFirebase.GetAllReviewsListener() {
            @Override
            public void onComplete(List<Review> data) {
                long lastUpdated = 0;
                for (Review review : data) {
                    if (review.isDeleted()) {
                        modelSql.deleteReview(review, null);
                    }
                    else {
                        modelSql.addReview(review, null);
                    }

                    if(review.getLastUpdated() > lastUpdated) {
                        lastUpdated = review.getLastUpdated();
                    }
                }

                ReadditApplication.context
                        .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                        .edit()
                        .putLong("reviewsLastUpdateDate", lastUpdated)
                        .apply();

                if (listener != null) {
                    listener.onComplete();
                }
            }
        });
    }

    public void getReviewById(String id, final GetReviewListener listener) {
        modelSql.getReviewById(id, new GetReviewListener() {
            @Override
            public void onComplete(Review review) {
                listener.onComplete(review);
            }
        });
    }

    public void addReview(final Review review, final AddReviewListener listener) {
        // save date
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String dateString = format.format(new Date());
        review.setDate(dateString);
        // add review
        modelFirebase.addReview(review, new AddReviewListener() {
            @Override
            public void onComplete() {
                refreshAllReviews(new GetAllReviewsListener() {
                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });
            }
        });
    }

    public interface AddReviewListener {
        void onComplete();
    }

    public interface GetAllReviewsListener{
        void onComplete();
    }

    public interface GetReviewListener {
        void onComplete(Review review);
    }

}
