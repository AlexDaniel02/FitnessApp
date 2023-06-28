package com.example.fitnessapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.Models.EntityLayer.Workout;
import com.example.fitnessapp.ViewModels.MyViewModelFactory;
import com.example.fitnessapp.ViewModels.WorkoutViewModel;

import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private WorkoutAdapter workoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        recyclerView = findViewById(R.id.workout_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        workoutAdapter = new WorkoutAdapter();
        recyclerView.setAdapter(workoutAdapter);

        WorkoutViewModel workoutViewModel = new ViewModelProvider(this, new MyViewModelFactory(getApplication())).get(WorkoutViewModel.class);
        workoutViewModel.getWorkouts().observe(this, workouts -> {
            workoutAdapter.setup(workouts);
        });

        Button createWorkoutButton = findViewById(R.id.create_workout_button);
        createWorkoutButton.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new CreateWorkoutFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }
}
