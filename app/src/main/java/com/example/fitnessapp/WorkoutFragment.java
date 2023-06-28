package com.example.fitnessapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.fitnessapp.Models.EntityLayer.Workout;

/*public class WorkoutFragment extends Fragment {
    private static final String ARG_WORKOUT = "arg_workout";

    private TextView workoutNameTextView;

    public WorkoutFragment() {
        // Required empty public constructor
    }

    public static WorkoutFragment newInstance(Workout workout) {
        WorkoutFragment fragment = new WorkoutFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_WORKOUT, workout);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout, container, false);
        workoutNameTextView = view.findViewById(R.id.workoutNameTextView);

        // Retrieve the workout from arguments
        Bundle args = getArguments();
        if (args != null) {
            Workout workout = args.getParcelable(ARG_WORKOUT);
            if (workout != null) {
                workoutNameTextView.setText(workout.getName());
            }
        }

        return view;
    }
}*/
