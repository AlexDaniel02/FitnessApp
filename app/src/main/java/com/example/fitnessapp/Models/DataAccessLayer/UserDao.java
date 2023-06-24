package com.example.fitnessapp.Models.DataAccessLayer;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitnessapp.Models.EntityLayer.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User WHERE username=:username AND password=:password LIMIT 1")
    User findUser(String username, String password);
    @Query("SELECT * FROM User WHERE username = :username")
    User getUserByUsername(String username);
    @Query("SELECT * FROM User WHERE username = :username AND password = :password")
    User getUserByUsernameAndPassword(String username,String password);
    @Insert
    void registerUser(User user);


}
