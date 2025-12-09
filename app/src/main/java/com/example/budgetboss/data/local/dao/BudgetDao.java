package com.example.budgetboss.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.budgetboss.data.local.entity.BudgetGoalEntity;

import java.util.List;

@Dao
public interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBudgetGoal(BudgetGoalEntity budgetGoal);

    @Update
    void updateBudgetGoal(BudgetGoalEntity budgetGoal);

    @Delete
    void deleteBudgetGoal(BudgetGoalEntity budgetGoal);

    @Query("SELECT * FROM budget_goals")
    LiveData<List<BudgetGoalEntity>> getAllBudgetGoals();
}
