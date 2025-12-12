package com.example.budgetboss.domain.repository;

import androidx.lifecycle.LiveData;

public interface VaultRepository {
    boolean hasPin();
    boolean validatePin(String pin);
    void setPin(String pin);
    double getVaultBalance();
    void addToVault(double amount);
    void withdrawFromVault(double amount);
}
