    package com.example.fitnessapp.ViewModels;

    import android.app.Application;

    import androidx.annotation.NonNull;
    import androidx.lifecycle.AndroidViewModel;
    import androidx.lifecycle.LiveData;
    import androidx.lifecycle.MutableLiveData;
    import androidx.lifecycle.ViewModel;
    import androidx.room.Room;

    import com.example.fitnessapp.Models.AppDatabase;
    import com.example.fitnessapp.Models.DataAccessLayer.UserDao;
    import com.example.fitnessapp.Models.EntityLayer.User;
    import com.example.fitnessapp.Models.Repositories.UserRepository;

    public class UserViewModel extends AndroidViewModel {
        private UserRepository userRepository;
        private MutableLiveData<String> operationResult = new MutableLiveData<>();

        public UserViewModel(@NonNull Application application) {
            super(application);
            userRepository = new UserRepository(application);
        }

        public LiveData<String> getOperationResult() {
            return operationResult;
        }

        public void loginUser(String username, String password) {
            AppDatabase.databaseWriteExecutor.execute(() -> {
                User user = userRepository.getUserByUsernameAndPassword(username, password);
                if (user != null) {
                    operationResult.postValue("Login successful!");
                } else {
                    operationResult.postValue("Login failed. Account doesn't exist.");
                }
            });
        }

        public void registerUser(String username, String password) {
            AppDatabase.databaseWriteExecutor.execute(() -> {
                // Check if username already exists
                User existingUser = userRepository.getUserByUsername(username);
                if (existingUser != null) {
                    operationResult.postValue("Registration failed. Username already exists.");
                    return;
                }

                try {
                    User newUser = new User();
                    newUser.username = username;
                    newUser.password = password; // remember to hash and salt passwords in a real application
                    userRepository.insert(newUser);
                    operationResult.postValue("Registration successful!");
                } catch (Exception e) {
                    operationResult.postValue("Registration failed. Please try again.");
                }
            });
        }
    }