package com.example.fitnessapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        exerciseAdapter = new ExerciseAdapter(exercises);
        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        exerciseRecyclerView.setAdapter(exerciseAdapter);
        exerciseAdapter.setOnDeleteClickListener(this);
        Button addExerciseButton = view.findViewById(R.id.add_exercise_button);
        addExerciseButton.setOnClickListener(v -> {
            Exercise newExercise = new Exercise();

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_exercise, null);
            EditText exerciseNameEditText = dialogView.findViewById(R.id.exercise_name_edittext);
            EditText exerciseSetsEditText = dialogView.findViewById(R.id.exercise_sets_edittext);
            EditText exerciseRepsEditText = dialogView.findViewById(R.id.exercise_reps_edittext);
            Button dialogAddExerciseButton = dialogView.findViewById(R.id.dialog_add_exercise_button);
            builder.setView(dialogView);

            AlertDialog dialog = builder.create();

            dialogAddExerciseButton.setOnClickListener(b -> {
                String exerciseName = exerciseNameEditText.getText().toString();

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

        Button addWorkoutButton = view.findViewById(R.id.add_workout_button);
        addWorkoutButton.setOnClickListener(v -> {
            String workoutName = workoutNameEditText.getText().toString();

            Workout newWorkout = new Workout();
            if(workoutName.isEmpty())
            {
                Toast.makeText(requireContext(), "Please enter a workout name.", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sharedPref = requireActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
            int userId = sharedPref.getInt("currentUserId", -1);

            System.out.println(userId);

            newWorkout.setWorkoutName(workoutName);
            newWorkout.setUserId(userId);

            if(exercises.isEmpty()){
                Toast.makeText(requireContext(), "Please add some exercises.", Toast.LENGTH_SHORT).show();
                return;
            }

            AppDatabase.databaseWriteExecutor.execute(() -> {
                newWorkout.id= (int) workoutDao.insertWorkout(newWorkout);
                workoutViewModel.addWorkoutWithExercises(newWorkout, exercises);
            });

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