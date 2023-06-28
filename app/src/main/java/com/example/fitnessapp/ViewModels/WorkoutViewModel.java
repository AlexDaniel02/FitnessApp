package com.example.fitnessapp.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.fitnessapp.Models.AppDatabase;
import com.example.fitnessapp.Models.DataAccessLayer.WorkoutDao;
import com.example.fitnessapp.Models.DataAccessLayer.WorkoutExerciseCrossRefDao;
import com.example.fitnessapp.Models.EntityLayer.Exercise;
import com.example.fitnessapp.Models.EntityLayer.Workout;
import com.example.fitnessapp.Models.EntityLayer.WorkoutExerciseCrossRef;

import java.util.List;

public class WorkoutViewModel extends ViewModel {
    private final WorkoutDao workoutDao;
    private final WorkoutExerciseCrossRefDao crossRefDao; // assuming you have this DAO
    private LiveData<List<Workout>> workouts;


    public WorkoutViewModel(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        workoutDao = db.workoutDao();
        crossRefDao = db.workoutExerciseCrossRefDao(); // initialize this DAO
        workouts = workoutDao.getAllWorkouts();
    }

    public LiveData<List<Workout>> getWorkouts() {
        return workouts;
    }

    public void addWorkoutWithExercises(Workout workout, List<Exercise> exercises) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            int workoutId = workoutDao.getLastWorkoutId().getValue(); // assuming insertWorkout returns the ID of the inserted workout

            for (Exercise exercise : exercises) {
                WorkoutExerciseCrossRef crossRef = new WorkoutExerciseCrossRef();
                crossRef.workoutId = workoutId;
                crossRef.exerciseId = exercise.id;
                crossRefDao.insertWorkoutExerciseCrossRef(crossRef);
            }
        });
    }

    public void deleteWorkout(Workout workout) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutDao.deleteWorkout(workout);
        });
    }
}
