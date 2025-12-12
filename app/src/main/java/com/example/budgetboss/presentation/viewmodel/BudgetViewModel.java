package com.example.budgetboss.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.budgetboss.data.local.entity.BudgetGoalEntity;
import com.example.budgetboss.domain.repository.BudgetRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class BudgetViewModel extends ViewModel {

    private final BudgetRepository budgetRepository;

    @Inject
    public BudgetViewModel(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public LiveData<List<BudgetGoalEntity>> getAllBudgets() {
        return budgetRepository.getAllBudgetGoals();
    }

    public void addBudget(BudgetGoalEntity budget) {
        budgetRepository.addBudgetGoal(budget);
    }

    public void updateBudget(BudgetGoalEntity budget) {
        budgetRepository.updateBudgetGoal(budget);
    }

    public void deleteBudget(BudgetGoalEntity budget) {
        budgetRepository.deleteBudgetGoal(budget);
    }
}
