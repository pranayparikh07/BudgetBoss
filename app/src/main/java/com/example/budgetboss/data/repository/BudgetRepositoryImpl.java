package com.example.budgetboss.data.repository;

import androidx.lifecycle.LiveData;
import com.example.budgetboss.data.local.dao.BudgetDao;
import com.example.budgetboss.data.local.entity.BudgetGoalEntity;
import com.example.budgetboss.domain.repository.BudgetRepository;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.inject.Inject;

public class BudgetRepositoryImpl implements BudgetRepository {

    private final BudgetDao budgetDao;
    private final Executor executor;

    @Inject
    public BudgetRepositoryImpl(BudgetDao budgetDao) {
        this.budgetDao = budgetDao;
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void addBudgetGoal(BudgetGoalEntity budgetGoal) {
        executor.execute(() -> budgetDao.insertBudgetGoal(budgetGoal));
    }

    @Override
    public void updateBudgetGoal(BudgetGoalEntity budgetGoal) {
        executor.execute(() -> budgetDao.updateBudgetGoal(budgetGoal));
    }

    @Override
    public void deleteBudgetGoal(BudgetGoalEntity budgetGoal) {
        executor.execute(() -> budgetDao.deleteBudgetGoal(budgetGoal));
    }

    @Override
    public LiveData<List<BudgetGoalEntity>> getAllBudgetGoals() {
        return budgetDao.getAllBudgetGoals();
    }
}
