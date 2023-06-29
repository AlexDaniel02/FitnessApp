package com.example.fitnessapp.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fitnessapp.Models.AppDatabase;
import com.example.fitnessapp.Models.Repositories.UserRepository;
import com.example.fitnessapp.Models.EntityLayer.User;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private MutableLiveData<String> operationResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    private MutableLiveData<User> loggedInUser = new MutableLiveData<>();

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public LiveData<Boolean> getIsLoggedIn() {
        return isLoggedIn;
    }

    public LiveData<String> getOperationResult() {
        return operationResult;
    }

    public LiveData<User> getLoggedInUser() {
        return loggedInUser;
    }

    public void loginUser(String username, String password) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            User user = userRepository.getUserByUsernameAndPassword(username, password);
            if (user != null) {
                operationResult.postValue("Login successful!");
                isLoggedIn.postValue(true);
                loggedInUser.postValue(user);
            } else {
                operationResult.postValue("Login failed. Account doesn't exist.");
                isLoggedIn.postValue(false);
            }
        });
    }

    public void registerUser(String username, String password) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            User existingUser = userRepository.getUserByUsername(username);
            if (existingUser != null) {
                operationResult.postValue("Registration failed. Username already exists.");
                return;
            }

            try {
                User newUser = new User();
                newUser.username = username;
                newUser.password = password;
                userRepository.insert(newUser);
                operationResult.postValue("Registration successful!");
            } catch (Exception e) {
                operationResult.postValue("Registration failed. Please try again.");
            }
        });
    }
}
