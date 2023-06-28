package com.example.fitnessapp.Models.DataAccessLayer;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.fitnessapp.Models.EntityLayer.Workout;

import java.util.List;

@Dao
public interface WorkoutDao {
    @Query("SELECT * FROM workout")
    LiveData<List<Workout>> getAllWorkouts();

    @Insert
    long insertWorkout(Workout workout);

    @Query("SELECT MAX(id) FROM workout")
    @Nullable
    LiveData<Integer> getLastWorkoutId();

    @Delete
    void deleteWorkout(Workout workout);
}