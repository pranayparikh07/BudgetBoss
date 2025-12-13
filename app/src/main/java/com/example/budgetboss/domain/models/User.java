package com.example.budgetboss.domain.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private String id;
    private String username;
    private String email;
    private double upiBalance;
    private double cashBalance;
    private double vaultBalance;
    private long createdAt;

    // Required empty constructor for Firebase
    public User() {
    }

    public User(String id, String username, String email, double upiBalance, double cashBalance, double vaultBalance, long createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.upiBalance = upiBalance;
        this.cashBalance = cashBalance;
        this.vaultBalance = vaultBalance;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public double getUpiBalance() { return upiBalance; }
    public void setUpiBalance(double upiBalance) { this.upiBalance = upiBalance; }
    
    public double getCashBalance() { return cashBalance; }
    public void setCashBalance(double cashBalance) { this.cashBalance = cashBalance; }
    
    public double getVaultBalance() { return vaultBalance; }
    public void setVaultBalance(double vaultBalance) { this.vaultBalance = vaultBalance; }
    
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
