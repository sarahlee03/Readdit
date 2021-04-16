package com.example.readdit.model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ModelSql {

    // reviews
    public LiveData<List<Review>> getAllReviews(){
        return AppLocalDB.db.reviewDao().getAllReviews();
    }

    public LiveData<List<Review>> getReviewsByUserId(String userId){
        return AppLocalDB.db.reviewDao().getReviewsByUserId(userId);
    }

    public interface AddReviewListener{
        void onComplete();
    }

    public void addReview(final Review review, final Model.AddReviewListener listener){
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDB.db.reviewDao().insertAll(review);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null){
                    listener.onComplete();
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    public void deleteReview(Review review, Model.AsyncListener<Boolean> listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDB.db.reviewDao().delete(review);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                if (listener != null) {
                    listener.onComplete(true);
                }

            }
        }
    }

    // users
    public LiveData<User> getUserById(String id) {
        return AppLocalDB.db.userDao().getUserByUID(id);
    }

    public void insertUser(User user, Model.AsyncListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDB.db.userDao().insertAll(user);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if(listener != null) listener.onComplete(user);

            }
        }

        new MyAsyncTask().execute();
    }

    public void deleteUser(User user, Model.AsyncListener<Boolean> listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDB.db.userDao().delete(user);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                if (listener != null) {
                    listener.onComplete(true);
                }

            }
        }

        new MyAsyncTask().execute();
    }
}
