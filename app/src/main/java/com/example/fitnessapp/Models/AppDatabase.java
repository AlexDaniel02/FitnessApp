package com.example.fitnessapp.Models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fitnessapp.Models.DataAccessLayer.ExerciseDao;
import com.example.fitnessapp.Models.DataAccessLayer.UserDao;
import com.example.fitnessapp.Models.DataAccessLayer.WorkoutDao;
import com.example.fitnessapp.Models.DataAccessLayer.WorkoutExerciseCrossRefDao;
import com.example.fitnessapp.Models.EntityLayer.Exercise;
import com.example.fitnessapp.Models.EntityLayer.User;
import com.example.fitnessapp.Models.EntityLayer.Workout;
import com.example.fitnessapp.Models.EntityLayer.WorkoutExerciseCrossRef;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Exercise.class, Workout.class, WorkoutExerciseCrossRef.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ExerciseDao exerciseDao();
    public abstract WorkoutDao workoutDao();
    public abstract WorkoutExerciseCrossRefDao workoutExerciseCrossRefDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}