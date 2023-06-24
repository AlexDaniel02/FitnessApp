package com.example.fitnessapp.Models.EntityLayer;

import androidx.room.Entity;

@Entity(primaryKeys = {"workoutId", "exerciseId"})
public class WorkoutExerciseCrossRef {
    public int workoutId;
    public int exerciseId;
}