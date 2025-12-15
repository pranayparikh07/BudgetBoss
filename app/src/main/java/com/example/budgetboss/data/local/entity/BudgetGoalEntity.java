package com.example.budgetboss.data.local.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "budget_goals")
public class BudgetGoalEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String userId;
    public String category;
    public double limitAmount;
    public String period; // Weekly, Monthly
    public long createdAt;
    public String firebaseKey; // Firebase database key for sync

    // Required empty constructor for Firebase
    public BudgetGoalEntity() {
    }

    @Ignore
    public BudgetGoalEntity(String userId, String category, double limitAmount, String period, long createdAt) {
        this.userId = userId;
        this.category = category;
        this.limitAmount = limitAmount;
        this.period = period;
        this.createdAt = createdAt;
    }
}
