package com.example.fitnessapp.Models.Repositories;

import android.app.Application;

import com.example.fitnessapp.Models.AppDatabase;
import com.example.fitnessapp.Models.DataAccessLayer.UserDao;
import com.example.fitnessapp.Models.EntityLayer.User;

public class UserRepository {
    private UserDao userDao;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        userDao = db.userDao();
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        return userDao.getUserByUsernameAndPassword(username, password);
    }

    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    public void insert(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.registerUser(user);
        });
    }
}