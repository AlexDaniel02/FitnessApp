package com.example.fitnessapp.Models.EntityLayer;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "reps")
    public int reps;

    @ColumnInfo(name = "sets")
    public int sets;
    public void setName(String name)
    {
        this.name=name;
    }
    public void setReps(int reps)
    {
        this.reps=reps;
    }
    public void setSets(int sets)
    {
        this.sets=sets;
    }
    public void setId(int id)
    {
        this.id=id;
    }
    public String getName()
    {
        return name;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }
}
