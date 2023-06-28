package com.example.fitnessapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.Models.EntityLayer.Workout;
import com.example.fitnessapp.ViewModels.MyViewModelFactory;
import com.example.fitnessapp.ViewModels.WorkoutViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListWorkoutFragment extends Fragment implements WorkoutAdapter.OnDeleteClickListener{
    private List<Workout> workouts = new ArrayList<>();
    private WorkoutAdapter workoutAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_workout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView workoutRecyclerView = view.findViewById(R.id.workout_recycler_view);
        Button createWorkoutButton = view.findViewById(R.id.create_workout_button);

        workoutAdapter = new WorkoutAdapter(workouts);
        workoutRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        workoutRecyclerView.setAdapter(workoutAdapter);
        workoutAdapter.setOnDeleteClickListener(this);
        MyViewModelFactory factory = new MyViewModelFactory(requireActivity().getApplication());
        WorkoutViewModel workoutViewModel = new ViewModelProvider(this, factory).get(WorkoutViewModel.class);
        workoutViewModel.getWorkouts().observe(getViewLifecycleOwner(), updatedWorkouts -> {
            workouts.clear();
            workouts.addAll(updatedWorkouts);
            workoutAdapter.notifyDataSetChanged();
        });

        createWorkoutButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new CreateWorkoutFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }
    @Override
    public void onDeleteClick(Workout workout) {
        MyViewModelFactory factory = new MyViewModelFactory(requireActivity().getApplication());
        WorkoutViewModel workoutViewModel = new ViewModelProvider(this, factory).get(WorkoutViewModel.class);
        workoutViewModel.deleteWorkout(workout);
    }

}