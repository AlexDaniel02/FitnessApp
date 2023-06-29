package com.example.fitnessapp.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.fitnessapp.Models.AppDatabase;
import com.example.fitnessapp.Models.DataAccessLayer.ExerciseDao;
import com.example.fitnessapp.Models.DataAccessLayer.WorkoutDao;
import com.example.fitnessapp.Models.DataAccessLayer.WorkoutExerciseCrossRefDao;
import com.example.fitnessapp.Models.EntityLayer.Exercise;
import com.example.fitnessapp.Models.EntityLayer.Workout;
import com.example.fitnessapp.Models.EntityLayer.WorkoutExerciseCrossRef;

import java.util.ArrayList;
import java.util.List;

public class WorkoutViewModel extends ViewModel {
    private final WorkoutDao workoutDao;
    private final ExerciseDao exerciseDao;
    private final WorkoutExerciseCrossRefDao crossRefDao;
    private LiveData<List<Workout>> workouts;
    private final MutableLiveData<List<Exercise>> exercises;
    public WorkoutViewModel(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        workoutDao = db.workoutDao();
        crossRefDao = db.workoutExerciseCrossRefDao();
        workouts = workoutDao.getAllWorkouts();
        exercises = new MutableLiveData<>();
        exerciseDao = db.exerciseDao();
    }

    public LiveData<List<Workout>> getWorkoutsByUser(int userId) {
        return workoutDao.getWorkoutsByUser(userId);
    }

    public void addWorkoutWithExercises(Workout workout, List<Exercise> exercises) {
        AppDatabase.databaseWriteExecutor.execute(() -> {


            for (Exercise exercise : exercises) {
                WorkoutExerciseCrossRef crossRef = new WorkoutExerciseCrossRef();
                crossRef.workoutId = workout.id;
                crossRef.exerciseId = exercise.id;
                crossRefDao.insertWorkoutExerciseCrossRef(crossRef);
            }
        });
    }
    public void updateWorkoutWithExercises(Workout workout, List<Exercise> exercises) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            workoutDao.updateWorkout(workout);
            List<WorkoutExerciseCrossRef> oldCrossRefs = crossRefDao.findExercisesForWorkout(workout.id);
            for (WorkoutExerciseCrossRef oldCrossRef : oldCrossRefs) {
                crossRefDao.deleteWorkoutExerciseCrossRef(oldCrossRef);
            }

            for (Exercise exercise : exercises) {
                WorkoutExerciseCrossRef crossRef = new WorkoutExerciseCrossRef();
                crossRef.workoutId = workout.id;
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

    public LiveData<List<Exercise>> getExercises(Workout workout) {
        new Thread(() -> {
            List<WorkoutExerciseCrossRef> refs = crossRefDao.findExercisesForWorkout(workout.getId());
            List<Exercise> exerciseList = new ArrayList<>();
            for (WorkoutExerciseCrossRef ref : refs) {
                Exercise exercise = exerciseDao.findExerciseById(ref.exerciseId);
                if (exercise != null) {
                    exerciseList.add(exercise);
                }
            }
            exercises.postValue(exerciseList);
        }).start();

        return exercises;
    }
}
