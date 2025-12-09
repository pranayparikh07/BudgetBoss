package com.example.budgetboss.domain.repository;

import androidx.lifecycle.LiveData;
import com.example.budgetboss.domain.models.Transaction;
import com.example.budgetboss.utils.Resource;
import java.util.List;

public interface TransactionRepository {
    void addTransaction(Transaction transaction);
    LiveData<List<Transaction>> getAllTransactions();
    LiveData<Resource<Double>> getIncome();
    LiveData<Resource<Double>> getExpense();
    LiveData<Resource<Double>> getBalance();
}
