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

import com.example.fitnessapp.Models.AppDatabase;
import com.example.fitnessapp.Models.DataAccessLayer.ExerciseDao;
import com.example.fitnessapp.Models.EntityLayer.Exercise;
import com.example.fitnessapp.Models.EntityLayer.Workout;
import com.example.fitnessapp.ViewModels.MyViewModelFactory;
import com.example.fitnessapp.ViewModels.WorkoutViewModel;

import java.util.ArrayList;
import java.util.List;

public class EditWorkoutFragment extends Fragment implements ExerciseAdapter.OnEditClickListener, ExerciseAdapter.OnDeleteClickListener {
    private Workout workout;
    private EditText workoutNameEditText;
    private List<Exercise> exercises = new ArrayList<>();
    private ExerciseAdapter exerciseAdapter;
    private ExerciseDao exerciseDao;

    public EditWorkoutFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            workout = getArguments().getParcelable("workout");
            exerciseDao = AppDatabase.getDatabase(requireContext()).exerciseDao();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_workout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        workoutNameEditText = view.findViewById(R.id.workoutNameEditText);
        Button saveButton = view.findViewById(R.id.saveWorkoutButton);

        if (workout != null) {
            workoutNameEditText.setText(workout.getName());
        }
        RecyclerView exerciseRecyclerView = view.findViewById(R.id.exercise_recycler_view);

        exerciseAdapter = new ExerciseAdapter(exercises);
        exerciseAdapter.setOnDeleteClickListener(this);
        exerciseAdapter.setOnEditClickListener(this);
        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        exerciseRecyclerView.setAdapter(exerciseAdapter);
        MyViewModelFactory factory = new MyViewModelFactory(requireActivity().getApplication());
        WorkoutViewModel workoutViewModel = new ViewModelProvider(this, factory).get(WorkoutViewModel.class);


        workoutViewModel.getExercises(workout).observe(getViewLifecycleOwner(), updatedExercises -> {
            exercises.clear();
            exercises.addAll(updatedExercises);
            exerciseAdapter.notifyDataSetChanged();
        });

        Button addExerciseButton = view.findViewById(R.id.addExerciseButton);

        addExerciseButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_exercise, null);
            EditText exerciseNameEditText = dialogView.findViewById(R.id.exercise_name_edittext);
            EditText exerciseSetsEditText = dialogView.findViewById(R.id.exercise_sets_edittext);
            EditText exerciseRepsEditText = dialogView.findViewById(R.id.exercise_reps_edittext);
            Button dialogAddExerciseButton = dialogView.findViewById(R.id.dialog_add_exercise_button);

            builder.setView(dialogView);
            AlertDialog dialog = builder.create();

            dialogAddExerciseButton.setOnClickListener(v2 -> {
                String newExerciseName = exerciseNameEditText.getText().toString();
                String newExerciseSetsText = exerciseSetsEditText.getText().toString();
                String newExerciseRepsText = exerciseRepsEditText.getText().toString();

                if (newExerciseName.isEmpty() || newExerciseSetsText.isEmpty() || newExerciseRepsText.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Exercise newExercise = new Exercise();
                newExercise.setName(newExerciseName);
                newExercise.setSets(Integer.parseInt(newExerciseSetsText));
                newExercise.setReps(Integer.parseInt(newExerciseRepsText));

                AppDatabase.databaseWriteExecutor.execute(() -> {
                    int id = (int)exerciseDao.insertExercise(newExercise);
                    newExercise.setId(id);
                });

                exercises.add(newExercise);
                exerciseAdapter.notifyDataSetChanged();

                Toast.makeText(requireContext(), "Exercise added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });

        saveButton.setOnClickListener(v3 -> {
            if(exercises.isEmpty()){
                Toast.makeText(requireContext(), "Please add an exercise!", Toast.LENGTH_SHORT).show();
                return;
            }
            String newWorkoutName = workoutNameEditText.getText().toString();
            if (newWorkoutName.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a workout name", Toast.LENGTH_SHORT).show();
            } else {
                workout.setWorkoutName(newWorkoutName);
                workoutViewModel.updateWorkoutWithExercises(workout, exercises);
                Toast.makeText(requireContext(), "Workout updated successfully", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });



            dialog.show();
        });

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

    @Override
    public void onEditClick(Exercise exercise) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_exercise, null);
        EditText exerciseNameEditText = dialogView.findViewById(R.id.exercise_name_edittext);
        EditText exerciseSetsEditText = dialogView.findViewById(R.id.exercise_sets_edittext);
        EditText exerciseRepsEditText = dialogView.findViewById(R.id.exercise_reps_edittext);
        Button dialogUpdateExerciseButton = dialogView.findViewById(R.id.dialog_add_exercise_button);

        exerciseNameEditText.setText(exercise.getName());
        exerciseSetsEditText.setText(String.valueOf(exercise.getSets()));
        exerciseRepsEditText.setText(String.valueOf(exercise.getReps()));

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialogUpdateExerciseButton.setOnClickListener(v -> {
            String newExerciseName = exerciseNameEditText.getText().toString();
            String newExerciseSetsText = exerciseSetsEditText.getText().toString();
            String newExerciseRepsText = exerciseRepsEditText.getText().toString();

            if (newExerciseName.isEmpty() || newExerciseSetsText.isEmpty() || newExerciseRepsText.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            exercise.setName(newExerciseName);
            exercise.setSets(Integer.parseInt(newExerciseSetsText));
            exercise.setReps(Integer.parseInt(newExerciseRepsText));

            AppDatabase.databaseWriteExecutor.execute(() -> {
                exerciseDao.updateExercise(exercise);
            });

            exerciseAdapter.notifyDataSetChanged();

            Toast.makeText(requireContext(), "Exercise updated", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

}
