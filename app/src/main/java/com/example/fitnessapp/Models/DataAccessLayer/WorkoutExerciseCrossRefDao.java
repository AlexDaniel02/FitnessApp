package com.example.fitnessapp.Models.DataAccessLayer;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitnessapp.Models.EntityLayer.WorkoutExerciseCrossRef;

import java.util.List;

@Dao
public interface WorkoutExerciseCrossRefDao {
    @Insert
    void insertWorkoutExerciseCrossRef(WorkoutExerciseCrossRef crossRef);

    @Query("SELECT * FROM WorkoutExerciseCrossRef WHERE workoutId = :workoutId")
    List<WorkoutExerciseCrossRef> findExercisesForWorkout(int workoutId);

    @Delete
    void deleteWorkoutExerciseCrossRef(WorkoutExerciseCrossRef crossRef);
}