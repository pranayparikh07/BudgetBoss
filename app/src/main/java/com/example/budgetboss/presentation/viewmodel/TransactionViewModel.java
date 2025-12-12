package com.example.budgetboss.presentation.viewmodel;

import androidx.lifecycle.ViewModel;
import com.example.budgetboss.domain.models.Transaction;
import com.example.budgetboss.domain.repository.TransactionRepository;
import com.example.budgetboss.domain.repository.AuthRepository;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TransactionViewModel extends ViewModel {

    private final TransactionRepository transactionRepository;
    private final AuthRepository authRepository;

    @Inject
    public TransactionViewModel(TransactionRepository transactionRepository, AuthRepository authRepository) {
        this.transactionRepository = transactionRepository;
        this.authRepository = authRepository;
    }

    private final androidx.lifecycle.MutableLiveData<String> _error = new androidx.lifecycle.MutableLiveData<>();
    public androidx.lifecycle.LiveData<String> getError() { return _error; }

    public void addTransaction(String title, double amount, Transaction.TransactionType type, String category) {
        String userId = "";
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        
        Transaction transaction = new Transaction(
            userId,
            title,
            amount,
            type,
            category,
            Transaction.WalletType.CASH, // Default
            System.currentTimeMillis(),
            "",
            ""
        );
        transactionRepository.addTransaction(transaction);
    }

    public void deleteTransaction(Transaction transaction, double currentBalance) {
        if (transaction.getType() == Transaction.TransactionType.CREDIT) {
            if (currentBalance - transaction.getAmount() < 0) {
                _error.postValue("Cannot delete: Insufficient balance!");
                return;
            }
        }
        transactionRepository.deleteTransaction(transaction);
    }

    public void updateTransaction(Transaction transaction) {
        transactionRepository.updateTransaction(transaction);
    }

    public androidx.lifecycle.LiveData<com.example.budgetboss.utils.Resource<Double>> getBalance() {
        return transactionRepository.getBalance();
    }
}
