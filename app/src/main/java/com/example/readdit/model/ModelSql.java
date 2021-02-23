package com.example.readdit.model;

import android.os.AsyncTask;

public class ModelSql {
    public void getUserById(String id, Model.AsyncListener<User> listener) {
        class MyAsyncTask extends AsyncTask {
            User user;

            @Override
            protected Object doInBackground(Object[] objects) {
                user = AppLocalDB.db.userDao().getUserByUID(id);
                return user;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                listener.onComplete(user);

            }
        }

        new MyAsyncTask().execute();
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
                listener.onComplete(user);

            }
        }

        new MyAsyncTask().execute();
    }
}
