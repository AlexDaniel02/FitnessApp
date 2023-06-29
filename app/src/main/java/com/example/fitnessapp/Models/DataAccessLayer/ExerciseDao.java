package com.example.fitnessapp.Models.DataAccessLayer;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitnessapp.Models.EntityLayer.Exercise;
import com.example.fitnessapp.Models.EntityLayer.User;
import com.example.fitnessapp.Models.EntityLayer.Workout;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Insert
    long insertExercise(Exercise exercise);

    @Query("SELECT * FROM Exercise WHERE id = :exerciseId")
    Exercise findExerciseById(int exerciseId);

    @Delete
    void deleteExercise(Exercise exercise);

    @Update
    void updateExercise(Exercise exercise);
}

