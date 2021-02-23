package com.example.readdit.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.readdit.ReadditApplication;

// If entity is modified, change the version to higher number
@Database(entities = {User.class}, version = 3)
abstract class AppLocalDBRepository extends RoomDatabase {
    public abstract UserDao userDao();
}

public class AppLocalDB {
    static public AppLocalDBRepository db =
            Room.databaseBuilder(ReadditApplication.context,
                    AppLocalDBRepository.class,
                    "Readdit.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
