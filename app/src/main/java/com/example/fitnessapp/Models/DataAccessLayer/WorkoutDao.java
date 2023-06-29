package com.example.fitnessapp.Models.DataAccessLayer;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitnessapp.Models.EntityLayer.Workout;

import java.util.List;

@Dao
public interface WorkoutDao {
    @Query("SELECT * FROM workout")
    LiveData<List<Workout>> getAllWorkouts();

    @Insert
    long insertWorkout(Workout workout);

    @Delete
    void deleteWorkout(Workout workout);

    @Update
    void updateWorkout(Workout workout);

    @Query("SELECT * FROM workout WHERE userId = :userId")
    LiveData<List<Workout>> getWorkoutsByUser(int userId);
}