package com.example.budgetboss.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.budgetboss.data.local.dao.TransactionDao;
import com.example.budgetboss.data.local.entity.TransactionEntity;
import com.example.budgetboss.domain.models.Transaction;
import com.example.budgetboss.domain.repository.TransactionRepository;
import com.example.budgetboss.utils.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionDao transactionDao;
    private final FirebaseDatabase firebaseDatabase;
    private final FirebaseAuth firebaseAuth;
    private final Executor executor;

    @Inject
    public TransactionRepositoryImpl(TransactionDao transactionDao, FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth) {
        this.transactionDao = transactionDao;
        this.firebaseDatabase = firebaseDatabase;
        this.firebaseAuth = firebaseAuth;
        this.executor = Executors.newSingleThreadExecutor();
        startSync();
    }

    private void startSync() {
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference ref = firebaseDatabase.getReference("transactions").child(userId);
            ref.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(@androidx.annotation.NonNull com.google.firebase.database.DataSnapshot snapshot) {
                    executor.execute(() -> {
                        for (com.google.firebase.database.DataSnapshot child : snapshot.getChildren()) {
                             Transaction transaction = child.getValue(Transaction.class);
                             if (transaction != null) {
                                  transactionDao.insertTransaction(mapToEntity(transaction));
                             }
                        }
                    });
                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull com.google.firebase.database.DatabaseError error) {
                    // Handle error
                }
            });
        }
    }

    @Override
    public void addTransaction(Transaction transaction) {
        // 1. Save to Local DB
        TransactionEntity entity = mapToEntity(transaction);
        executor.execute(() -> transactionDao.insertTransaction(entity));

        // 2. Save to Firebase
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference ref = firebaseDatabase.getReference("transactions").child(userId).child(transaction.getId());
            ref.setValue(transaction); // Transaction model should differ slightly or be compatible
        }
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        TransactionEntity entity = mapToEntity(transaction);
        executor.execute(() -> transactionDao.updateTransaction(entity));

        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference ref = firebaseDatabase.getReference("transactions").child(userId).child(transaction.getId());
            ref.setValue(transaction);
        }
    }

    @Override
    public void deleteTransaction(Transaction transaction) {
        TransactionEntity entity = mapToEntity(transaction);
        executor.execute(() -> transactionDao.deleteTransaction(entity));

        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference ref = firebaseDatabase.getReference("transactions").child(userId).child(transaction.getId());
            ref.removeValue();
        }
    }

    @Override
    public LiveData<List<Transaction>> getAllTransactions() {
        return Transformations.map(transactionDao.getAllTransactions(), entities -> {
            List<Transaction> transactions = new ArrayList<>();
            for (TransactionEntity entity : entities) {
                transactions.add(mapToDomain(entity));
            }
            return transactions;
        });
    }

    @Override
    public LiveData<Resource<Double>> getIncome() {
        // Simplified: Calculate from ALL transactions in memory (not efficient for large data, but okay for MVP)
        // Ideally use DAO query with SUM
        return Transformations.map(transactionDao.getAllTransactions(), entities -> {
             double total = 0;
             for (TransactionEntity entity : entities) {
                 if ("CREDIT".equals(entity.type) || "INCOME".equals(entity.type)) { // Standardize type
                     total += entity.amount;
                 }
             }
             return Resource.success(total);
        });
    }

    @Override
    public LiveData<Resource<Double>> getExpense() {
         return Transformations.map(transactionDao.getAllTransactions(), entities -> {
             double total = 0;
             for (TransactionEntity entity : entities) {
                 if ("DEBIT".equals(entity.type) || "EXPENSE".equals(entity.type)) {
                     total += entity.amount;
                 }
             }
             return Resource.success(total);
        });
    }

    @Override
    public LiveData<Resource<Double>> getBalance() {
         return Transformations.map(transactionDao.getAllTransactions(), entities -> {
             double income = 0;
             double expense = 0;
             for (TransactionEntity entity : entities) {
                 if ("CREDIT".equals(entity.type)) income += entity.amount;
                 else if ("DEBIT".equals(entity.type)) expense += entity.amount;
             }
             return Resource.success(income - expense);
        });
    }

    private TransactionEntity mapToEntity(Transaction transaction) {
        return new TransactionEntity(
                transaction.getId(),
                transaction.getUserId(),
                transaction.getTitle(),
                transaction.getType().toString(),
                transaction.getAmount(),
                transaction.getCategory(),
                transaction.getWalletType().toString(),
                transaction.getDate(),
                transaction.getNotes(),
                transaction.getReceiptPath(),
                System.currentTimeMillis()
        );
    }

    private Transaction mapToDomain(TransactionEntity entity) {
        return new Transaction(
                entity.id,
                entity.userId,
                entity.title,
                entity.amount,
                Transaction.TransactionType.valueOf(entity.type),
                entity.category,
                Transaction.WalletType.valueOf(entity.walletType),
                entity.date,
                entity.notes,
                entity.receiptPath
        );
    }
}
