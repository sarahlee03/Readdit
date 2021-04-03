package com.example.readdit.model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

public class ModelSql {
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
