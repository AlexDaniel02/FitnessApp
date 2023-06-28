package com.example.fitnessapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.RoomDatabase;

import com.example.fitnessapp.Models.AppDatabase;
import com.example.fitnessapp.Models.DataAccessLayer.ExerciseDao;
import com.example.fitnessapp.Models.DataAccessLayer.WorkoutDao;
import com.example.fitnessapp.Models.EntityLayer.Exercise;
import com.example.fitnessapp.Models.EntityLayer.Workout;
import com.example.fitnessapp.ViewModels.MyViewModelFactory;
import com.example.fitnessapp.ViewModels.WorkoutViewModel;

import java.util.ArrayList;
import java.util.List;

public class CreateWorkoutFragment extends Fragment implements ExerciseAdapter.OnDeleteClickListener{
    private EditText workoutNameEditText;
    private List<Exercise> exercises = new ArrayList<>();
    private ExerciseAdapter exerciseAdapter;
    private ExerciseDao exerciseDao;
    private WorkoutDao workoutDao;
    private WorkoutViewModel workoutViewModel;
    public CreateWorkoutFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the ExerciseDao from the AppDatabase
        exerciseDao = AppDatabase.getDatabase(requireContext()).exerciseDao();
        workoutDao= AppDatabase.getDatabase(requireContext()).workoutDao();
        MyViewModelFactory factory = new MyViewModelFactory(requireActivity().getApplication());
        workoutViewModel = new ViewModelProvider(this, factory).get(WorkoutViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_workout, container, false);
        workoutNameEditText = view.findViewById(R.id.workout_name_edittext);
        RecyclerView exerciseRecyclerView = view.findViewById(R.id.exercise_recycler_view);

        // Set up RecyclerView for exercises
        exerciseAdapter = new ExerciseAdapter(exercises);
        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        exerciseRecyclerView.setAdapter(exerciseAdapter);
        exerciseAdapter.setOnDeleteClickListener(this);
        // Add Exercise button
        Button addExerciseButton = view.findViewById(R.id.add_exercise_button);
        addExerciseButton.setOnClickListener(v -> {
            // Create a new Exercise object and add it to the list
            Exercise newExercise = new Exercise();

            // Create the AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_exercise, null);
            EditText exerciseNameEditText = dialogView.findViewById(R.id.exercise_name_edittext);
            EditText exerciseSetsEditText = dialogView.findViewById(R.id.exercise_sets_edittext);
            EditText exerciseRepsEditText = dialogView.findViewById(R.id.exercise_reps_edittext);
            Button dialogAddExerciseButton = dialogView.findViewById(R.id.dialog_add_exercise_button);
            builder.setView(dialogView);

            AlertDialog dialog = builder.create();

            dialogAddExerciseButton.setOnClickListener(b -> {
                // Set the exercise name based on user input
                String exerciseName = exerciseNameEditText.getText().toString();

                // Set the exercise sets and reps based on user input
                String setsText = exerciseSetsEditText.getText().toString();
                String repsText = exerciseRepsEditText.getText().toString();
                if (repsText.isEmpty() || setsText.isEmpty() || exerciseName.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                newExercise.setName(exerciseName);
                int sets = Integer.parseInt(setsText);
                    newExercise.setSets(sets);
                    int reps = Integer.parseInt(repsText);
                    newExercise.setReps(reps);
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    newExercise.id= (int) exerciseDao.insertExercise(newExercise);
                });

                exercises.add(newExercise);
                exerciseAdapter.notifyDataSetChanged();
                dialog.dismiss();
            });

            dialog.show();
        });

        // Add Workout button
        Button addWorkoutButton = view.findViewById(R.id.add_workout_button);
        addWorkoutButton.setOnClickListener(v -> {
            String workoutName = workoutNameEditText.getText().toString();

            // Create a new Workout object with the name
            Workout newWorkout = new Workout();
            if(workoutName.isEmpty())
            {
                Toast.makeText(requireContext(), "Please enter an workout name.", Toast.LENGTH_SHORT).show();
                return;
            }
            newWorkout.setWorkoutName(workoutName);

            // Insert the workout into the database
            AppDatabase.databaseWriteExecutor.execute(() -> {
                newWorkout.id= (int) workoutDao.insertWorkout(newWorkout);
                workoutViewModel.addWorkoutWithExercises(newWorkout, exercises);
            });

            // Navigate back to ListWorkoutFragment
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
    @Override
    public void onDeleteClick(Exercise exercise) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            exerciseDao.deleteExercise(exercise);
        });

        exercises.remove(exercise);
        exerciseAdapter.notifyDataSetChanged();
        Toast.makeText(requireContext(), "Exercise deleted", Toast.LENGTH_SHORT).show();
    }
}