package com.example.readdit.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.BoringLayout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.readdit.MainActivity;
import com.example.readdit.ReadditApplication;
import com.example.readdit.ui.reviews.ReviewDetailsViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.example.readdit.ReadditApplication.BOOKS_FOLDER;

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
            if(listener != null) {
                listener.onComplete(null);
            }
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

    // endregion

    // region Reviews
    public LiveData<List<Review>> getAllReviews() {
        if (reviewsList == null) {
            reviewsList = modelSql.getAllReviews();
            refreshAllReviews(null);
        }
        return reviewsList;
    }

    public LiveData<List<Review>> getMyReviews() {
        myReviewsList = modelSql.getReviewsByUserId(getCurrentUserID());
        refreshAllReviews(null);
        return myReviewsList;
    }

    public void refreshAllReviews(AsyncListener listener) {
        Long lastUpdated = ReadditApplication.context
                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong("reviewsLastUpdateDate", 0);
        modelFirebase.getAllReviews(lastUpdated, new AsyncListener<List<Review>>() {
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
                    listener.onComplete(null);
                }
            }
        });
    }

    public LiveData<Review> getReviewById(String id) {
        return modelSql.getReviewById(id);
    }

    public void addReview(final Review review, AsyncListener listener) {
        // save date
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String dateString = format.format(new Date());
        review.setDate(dateString);
        // add review
        modelFirebase.addReview(review, new AsyncListener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                refreshAllReviews(new AsyncListener() {
                    @Override
                    public void onComplete(Object data) {
                        if (listener != null) {
                            listener.onComplete(data);
                        }
                    }
                });
            }
        });
    }

    public void editReview(Review review, AsyncListener listener) {
        modelFirebase.editReview(review, new AsyncListener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                refreshAllReviews(new AsyncListener<Boolean>() {
                    @Override
                    public void onComplete(Boolean data) {
                        if (listener != null) listener.onComplete(data);
                    }
                });
            }
        });
    }

    public void deleteReview(Review review, AsyncListener listener) {
        review.setDeleted(true);
        // update review with isDeleted=false
        modelFirebase.editReview(review, new AsyncListener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                // delete review image
                modelFirebase.deleteImage(getCurrentUserID() + "/" + review.getBook(), new AsyncListener<Boolean>() {
                    @Override
                    public void onComplete(Boolean data) {
                        // delete review from sql
                        refreshAllReviews(new AsyncListener<Boolean>() {
                            @Override
                            public void onComplete(Boolean data) {
                                listener.onComplete(data);
                            }
                        });
                    }
                }, BOOKS_FOLDER);
            }
        });
    }

    public void getReviewsListByUID(String userId, AsyncListener<List<Review>> listener) {
        modelSql.getReviewsListByUID(userId, listener);
    }
    // endregion
}