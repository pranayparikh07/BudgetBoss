package com.example.budgetboss.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.budgetboss.domain.models.Investment;
import com.example.budgetboss.domain.repository.InvestmentRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class InvestmentRepositoryImpl implements InvestmentRepository {

    private final MutableLiveData<List<Investment>> investments = new MutableLiveData<>(new ArrayList<>());
    private final FirebaseDatabase firebaseDatabase;
    private final FirebaseAuth firebaseAuth;

    @Inject
    public InvestmentRepositoryImpl(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth) {
        this.firebaseDatabase = firebaseDatabase;
        this.firebaseAuth = firebaseAuth;
        startSync();
    }

    private void startSync() {
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference ref = firebaseDatabase.getReference("investments").child(userId);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Investment> list = new ArrayList<>();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Investment investment = child.getValue(Investment.class);
                        if (investment != null) {
                            list.add(investment);
                        }
                    }
                    investments.postValue(list);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }

    @Override
    public void addInvestment(Investment investment) {
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference ref = firebaseDatabase.getReference("investments").child(userId).child(investment.getId());
            ref.setValue(investment);
        }
    }

    @Override
    public void updateInvestment(Investment investment) {
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference ref = firebaseDatabase.getReference("investments").child(userId).child(investment.getId());
            ref.setValue(investment);
        }
    }

    @Override
    public void deleteInvestment(Investment investment) {
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference ref = firebaseDatabase.getReference("investments").child(userId).child(investment.getId());
            ref.removeValue();
        }
    }

    @Override
    public LiveData<List<Investment>> getAllInvestments() {
        return investments;
    }
}
