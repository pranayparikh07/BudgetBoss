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

    public void addTransaction(String title, double amount, Transaction.TransactionType type, String category) {
        String userId = "";
        // Ideally fetch from AuthRepository.getCurrentUser(), but handling LiveData inside VM method for ID is tricky async-wise without transformations.
        // For MVP, direct access or assuming user is logged in (guarded by UI flow).
        // Let's us FirebaseAuth instance directly or expose a sync method in Repo. 
        // For now, I'll assume valid user. Note: In clean arch, use a UseCase.
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
}
