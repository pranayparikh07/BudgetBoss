package com.example.budgetboss.domain.models;

public class User {
    private String id;
    private String username;
    private String email;
    private double upiBalance;
    private double cashBalance;
    private double vaultBalance;
    private long createdAt;

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
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public double getUpiBalance() { return upiBalance; }
    public double getCashBalance() { return cashBalance; }
    public double getVaultBalance() { return vaultBalance; }
    public long getCreatedAt() { return createdAt; }
}
