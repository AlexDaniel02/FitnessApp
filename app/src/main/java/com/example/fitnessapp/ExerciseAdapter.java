package com.example.fitnessapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.Models.EntityLayer.Exercise;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private List<Exercise> exercises;
    private OnDeleteClickListener onDeleteClickListener;

    public ExerciseAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Exercise exercise);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
        return new ExerciseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise currentExercise = exercises.get(position);
        holder.exerciseNameTextView.setText(currentExercise.getName());
        holder.setsRepsTextView.setText(String.format("Sets: %d, Reps: %d", currentExercise.getSets(), currentExercise.getReps()));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private TextView exerciseNameTextView;
        private TextView setsRepsTextView;
        private ImageButton btnDelete;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseNameTextView = itemView.findViewById(R.id.exercise_name_textview);
            setsRepsTextView = itemView.findViewById(R.id.sets_reps_textview);
            btnDelete = itemView.findViewById(R.id.btn_delete);

            btnDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Exercise exercise = exercises.get(position);
                    onDeleteClickListener.onDeleteClick(exercise);
                }
            });
        }
    }
}