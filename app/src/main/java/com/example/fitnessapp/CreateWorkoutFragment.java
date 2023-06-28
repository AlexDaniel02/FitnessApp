package com.example.fitnessapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.Models.EntityLayer.Exercise;
import com.example.fitnessapp.Models.EntityLayer.Workout;
import com.example.fitnessapp.ViewModels.MyViewModelFactory;
import com.example.fitnessapp.ViewModels.WorkoutViewModel;

import java.util.ArrayList;
import java.util.List;

public class CreateWorkoutFragment extends Fragment {
    private EditText workoutNameEditText;
    private List<Exercise> exercises = new ArrayList<>();
    private ExerciseAdapter exerciseAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_workout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        workoutNameEditText = view.findViewById(R.id.workout_name_edittext);
        RecyclerView exerciseRecyclerView = view.findViewById(R.id.exercise_recycler_view);

        // Set up RecyclerView for exercises
        exerciseAdapter = new ExerciseAdapter(exercises);
        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        exerciseRecyclerView.setAdapter(exerciseAdapter);

        // Add Exercise button
        Button addExerciseButton = view.findViewById(R.id.add_exercise_button);
        addExerciseButton.setOnClickListener(v -> {
            // Create a new Exercise object and add it to the list
            Exercise newExercise = new Exercise();
            // Set the exercise sets and reps based on user input
            // ...

            exercises.add(newExercise);
            exerciseAdapter.notifyDataSetChanged();
        });

        // Add Workout button
        Button addWorkoutButton = view.findViewById(R.id.add_workout_button);
        addWorkoutButton.setOnClickListener(v -> {
            String workoutName = workoutNameEditText.getText().toString();

            // Create a new Workout object with the name
            Workout newWorkout = new Workout();
            newWorkout.setWorkoutName(workoutName);

            // Add the new workout to the database
            WorkoutViewModel workoutViewModel = new ViewModelProvider(this, new MyViewModelFactory(this.getActivity().getApplication())).get(WorkoutViewModel.class);
            workoutViewModel.addWorkoutWithExercises(newWorkout, exercises);

            // Navigate back to WorkoutActivity
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }
}
