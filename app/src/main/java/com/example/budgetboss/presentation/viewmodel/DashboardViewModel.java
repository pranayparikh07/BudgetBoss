package com.example.budgetboss.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.budgetboss.domain.models.Transaction;
import com.example.budgetboss.domain.models.User;
import com.example.budgetboss.domain.repository.AuthRepository;
import com.example.budgetboss.domain.repository.TransactionRepository;
import com.example.budgetboss.utils.Resource;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DashboardViewModel extends ViewModel {

    private final TransactionRepository transactionRepository;
    private final AuthRepository authRepository;

    @Inject
    public DashboardViewModel(TransactionRepository transactionRepository, AuthRepository authRepository) {
        this.transactionRepository = transactionRepository;
        this.authRepository = authRepository;
    }

    public LiveData<List<Transaction>> getRecentTransactions() {
        return transactionRepository.getAllTransactions();
    }

    public LiveData<Resource<Double>> getIncome() {
        return transactionRepository.getIncome();
    }

    public LiveData<Resource<Double>> getExpense() {
        return transactionRepository.getExpense();
    }

    public LiveData<Resource<Double>> getBalance() {
        return transactionRepository.getBalance();
    }

    public LiveData<User> getCurrentUser() {
        return authRepository.getCurrentUser();
    }

    public void logout() {
        authRepository.logout();
    }

    public void deleteTransaction(Transaction transaction) {
        transactionRepository.deleteTransaction(transaction);
    }

    public void addTransaction(Transaction transaction) {
        transactionRepository.addTransaction(transaction);
    }
}
