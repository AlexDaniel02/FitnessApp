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
    private OnEditClickListener onEditClickListener;

    private boolean shouldShowDeleteButton = true;
    private boolean shouldShowEditButton = true;

    public void setShouldShowDeleteButton(boolean shouldShow) {
        this.shouldShowDeleteButton = shouldShow;
    }

    public void setShouldShowEditButton(boolean shouldShow) {
        this.shouldShowEditButton = shouldShow;
    }

    public ExerciseAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Exercise exercise);
    }

    public interface OnEditClickListener {
        void onEditClick(Exercise exercise);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.onEditClickListener = listener;
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
        private ImageButton btnEdit;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);

            exerciseNameTextView = itemView.findViewById(R.id.exercise_name_textview);
            setsRepsTextView = itemView.findViewById(R.id.sets_reps_textview);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnEdit = itemView.findViewById(R.id.btn_edit);

            if (shouldShowDeleteButton) {
                btnDelete.setVisibility(View.VISIBLE);
            } else {
                btnDelete.setVisibility(View.GONE);
            }

            if (shouldShowEditButton) {
                btnEdit.setVisibility(View.VISIBLE);
            } else {
                btnEdit.setVisibility(View.GONE);
            }

            btnDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onDeleteClickListener != null) {
                    Exercise exercise = exercises.get(position);
                    onDeleteClickListener.onDeleteClick(exercise);
                }
            });

            btnEdit.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onEditClickListener != null) {
                    Exercise exercise = exercises.get(position);
                    onEditClickListener.onEditClick(exercise);
                }
            });
        }
    }
}