package com.example.budgetboss.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.budgetboss.data.local.entity.UserEntity;
import androidx.lifecycle.LiveData;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity user);

    @Query("SELECT * FROM users LIMIT 1")
    LiveData<UserEntity> getUser();

    @Query("DELETE FROM users")
    void clearUser();
}
