package com.example.budgetboss.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.budgetboss.domain.repository.VaultRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class VaultRepositoryImpl implements VaultRepository {

    private final SharedPreferences prefs;
    private final FirebaseDatabase firebaseDatabase;
    private final FirebaseAuth firebaseAuth;
    private final MutableLiveData<Double> vaultBalanceLiveData = new MutableLiveData<>(0.0);
    
    private static final String PREF_NAME = "budget_boss_vault";
    private static final String KEY_PIN = "vault_pin";
    private static final String KEY_BALANCE = "vault_balance";

    @Inject
    public VaultRepositoryImpl(@ApplicationContext Context context, FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.firebaseDatabase = firebaseDatabase;
        this.firebaseAuth = firebaseAuth;
        startSync();
    }

    private void startSync() {
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            DatabaseReference ref = firebaseDatabase.getReference("vault").child(userId);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Double balance = snapshot.child("balance").getValue(Double.class);
                    if (balance != null) {
                        vaultBalanceLiveData.postValue(balance);
                        // Also cache locally
                        prefs.edit().putLong(KEY_BALANCE, Double.doubleToLongBits(balance)).apply();
                    }
                    
                    String pin = snapshot.child("pin").getValue(String.class);
                    if (pin != null) {
                        prefs.edit().putString(KEY_PIN, pin).apply();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Fall back to local
                    vaultBalanceLiveData.postValue(getLocalVaultBalance());
                }
            });
        }
    }

    @Override
    public boolean hasPin() {
        return prefs.contains(KEY_PIN);
    }

    @Override
    public boolean validatePin(String pin) {
        String storedPin = prefs.getString(KEY_PIN, "");
        return storedPin.equals(pin);
    }

    @Override
    public void setPin(String pin) {
        prefs.edit().putString(KEY_PIN, pin).apply();
        
        // Sync to Firebase
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            firebaseDatabase.getReference("vault").child(userId).child("pin").setValue(pin);
        }
    }

    private double getLocalVaultBalance() {
        return Double.longBitsToDouble(prefs.getLong(KEY_BALANCE, Double.doubleToLongBits(0.0)));
    }

    @Override
    public double getVaultBalance() {
        return getLocalVaultBalance();
    }
    
    public LiveData<Double> getVaultBalanceLiveData() {
        return vaultBalanceLiveData;
    }

    @Override
    public void addToVault(double amount) {
        double current = getVaultBalance();
        double newBalance = current + amount;
        updateBalance(newBalance);
    }

    @Override
    public void withdrawFromVault(double amount) {
        double current = getVaultBalance();
        double newBalance = current - amount;
        if (newBalance < 0) newBalance = 0;
        updateBalance(newBalance);
    }
    
    private void updateBalance(double newBalance) {
        // Update local cache
        prefs.edit().putLong(KEY_BALANCE, Double.doubleToLongBits(newBalance)).apply();
        vaultBalanceLiveData.postValue(newBalance);
        
        // Sync to Firebase
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            firebaseDatabase.getReference("vault").child(userId).child("balance").setValue(newBalance);
        }
    }
}
