    package com.example.fitnessapp;

    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.fragment.app.FragmentTransaction;
    import androidx.lifecycle.ViewModelProvider;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.fitnessapp.Models.EntityLayer.Workout;
    import com.example.fitnessapp.ViewModels.MyViewModelFactory;
    import com.example.fitnessapp.ViewModels.WorkoutViewModel;

    import java.util.ArrayList;
    import java.util.List;

    public class WorkoutActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_workout);

            if (savedInstanceState == null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new ListWorkoutFragment());
                transaction.commit();
            }
        }
    }