package com.example.budgetboss.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.budgetboss.domain.repository.VaultRepository;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class VaultRepositoryImpl implements VaultRepository {

    private final SharedPreferences prefs;
    private static final String PREF_NAME = "budget_boss_vault";
    private static final String KEY_PIN = "vault_pin";
    private static final String KEY_BALANCE = "vault_balance";

    @Inject
    public VaultRepositoryImpl(@ApplicationContext Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
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
    }

    @Override
    public double getVaultBalance() {
        // In a real app, this should probably be encrypted or stored in DB.
        // For local MVP using SharedPreferences is okay.
        return Double.longBitsToDouble(prefs.getLong(KEY_BALANCE, Double.doubleToLongBits(0.0)));
    }

    @Override
    public void addToVault(double amount) {
        double current = getVaultBalance();
        double newBalance = current + amount;
        prefs.edit().putLong(KEY_BALANCE, Double.doubleToLongBits(newBalance)).apply();
    }

    @Override
    public void withdrawFromVault(double amount) {
        double current = getVaultBalance();
        double newBalance = current - amount;
        if (newBalance < 0) newBalance = 0;
        prefs.edit().putLong(KEY_BALANCE, Double.doubleToLongBits(newBalance)).apply();
    }
}
