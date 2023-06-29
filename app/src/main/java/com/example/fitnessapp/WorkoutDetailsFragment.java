package com.example.fitnessapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class WorkoutDetailsFragment extends Fragment{
    private List<Exercise> exercises = new ArrayList<>();
    private ExerciseAdapter exerciseAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workout_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView workoutNameTextView = view.findViewById(R.id.workout_name_textview);
        RecyclerView exerciseRecyclerView = view.findViewById(R.id.exercise_recycler_view);

        Workout workout = requireArguments().getParcelable("workout");

        if (workout != null) {
            workoutNameTextView.setText(workout.getName());
        }

        exerciseAdapter = new ExerciseAdapter(exercises);
        exerciseAdapter.setShouldShowDeleteButton(false);
        exerciseAdapter.setShouldShowEditButton(false);
        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        exerciseRecyclerView.setAdapter(exerciseAdapter);
        MyViewModelFactory factory = new MyViewModelFactory(requireActivity().getApplication());
        WorkoutViewModel workoutViewModel = new ViewModelProvider(this, factory).get(WorkoutViewModel.class);

        workoutViewModel.getExercises(workout).observe(getViewLifecycleOwner(), updatedExercises -> {
            exercises.clear();
            exercises.addAll(updatedExercises);
            exerciseAdapter.notifyDataSetChanged();
        });
    }
}
