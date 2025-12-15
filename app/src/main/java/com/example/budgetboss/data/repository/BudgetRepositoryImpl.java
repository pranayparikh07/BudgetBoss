package com.example.budgetboss.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.example.budgetboss.data.local.dao.BudgetDao;
import com.example.budgetboss.data.local.entity.BudgetGoalEntity;
import com.example.budgetboss.domain.repository.BudgetRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.inject.Inject;

public class BudgetRepositoryImpl implements BudgetRepository {

    private final BudgetDao budgetDao;
    private final FirebaseDatabase firebaseDatabase;
    private final FirebaseAuth firebaseAuth;
    private final Executor executor;

    @Inject
    public BudgetRepositoryImpl(BudgetDao budgetDao, FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth) {
        this.budgetDao = budgetDao;
        this.firebaseDatabase = firebaseDatabase;
        this.firebaseAuth = firebaseAuth;
        this.executor = Executors.newSingleThreadExecutor();
        startSync();
    }

    private void startSync() {
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference ref = firebaseDatabase.getReference("budgets").child(userId);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    executor.execute(() -> {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            BudgetGoalEntity budget = child.getValue(BudgetGoalEntity.class);
                            if (budget != null) {
                                // Store the Firebase key for proper sync
                                budget.firebaseKey = child.getKey();
                                budgetDao.insertBudgetGoal(budget);
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }

    @Override
    public void addBudgetGoal(BudgetGoalEntity budgetGoal) {
        // Generate a unique key for Firebase first
        String uniqueKey;
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            uniqueKey = firebaseDatabase.getReference("budgets").child(userId).push().getKey();
        } else {
            uniqueKey = "local_" + System.currentTimeMillis();
        }
        
        // Store the Firebase key for later updates/deletes
        budgetGoal.firebaseKey = uniqueKey;
        
        executor.execute(() -> budgetDao.insertBudgetGoal(budgetGoal));
        
        // Sync to Firebase with the same unique key
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference ref = firebaseDatabase.getReference("budgets").child(userId).child(uniqueKey);
            ref.setValue(budgetGoal);
        }
    }

    @Override
    public void updateBudgetGoal(BudgetGoalEntity budgetGoal) {
        executor.execute(() -> budgetDao.updateBudgetGoal(budgetGoal));
        
        // Sync to Firebase using the stored firebaseKey
        if (firebaseAuth.getCurrentUser() != null && budgetGoal.firebaseKey != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference ref = firebaseDatabase.getReference("budgets").child(userId).child(budgetGoal.firebaseKey);
            ref.setValue(budgetGoal);
        }
    }

    @Override
    public void deleteBudgetGoal(BudgetGoalEntity budgetGoal) {
        executor.execute(() -> budgetDao.deleteBudgetGoal(budgetGoal));
        
        // Remove from Firebase using the stored firebaseKey
        if (firebaseAuth.getCurrentUser() != null && budgetGoal.firebaseKey != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference ref = firebaseDatabase.getReference("budgets").child(userId).child(budgetGoal.firebaseKey);
            ref.removeValue();
        }
    }

    @Override
    public LiveData<List<BudgetGoalEntity>> getAllBudgetGoals() {
        return budgetDao.getAllBudgetGoals();
    }
}
