package com.example.budgetboss.domain.repository;

import androidx.lifecycle.LiveData;
import com.example.budgetboss.data.local.entity.BudgetGoalEntity;
import java.util.List;

public interface BudgetRepository {
    void addBudgetGoal(BudgetGoalEntity budgetGoal);
    LiveData<List<BudgetGoalEntity>> getAllBudgetGoals();
}
