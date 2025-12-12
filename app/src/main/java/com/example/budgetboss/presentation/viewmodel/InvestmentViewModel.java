package com.example.budgetboss.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.budgetboss.domain.models.Investment;
import com.example.budgetboss.domain.repository.InvestmentRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class InvestmentViewModel extends ViewModel {
    private final InvestmentRepository repository;

    @Inject
    public InvestmentViewModel(InvestmentRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Investment>> getInvestments() {
        return repository.getAllInvestments();
    }

    public void addInvestment(Investment investment) {
        repository.addInvestment(investment);
    }

    public void updateInvestment(Investment investment) {
        repository.updateInvestment(investment);
    }

    public void deleteInvestment(Investment investment) {
        repository.deleteInvestment(investment);
    }
}
