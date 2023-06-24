package com.example.fitnessapp.Models.EntityLayer;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
    public class Workout {
        @PrimaryKey(autoGenerate = true)
        public int id;

        @ColumnInfo(name = "name")
        public String name;
        public String getName()
        {
            return name;
        }
    }

