package com.example.budgetboss.domain.models;

import java.io.Serializable;
import java.util.UUID;

public class Transaction implements Serializable {
    private String id;
    private String userId;
    private String title;
    private double amount;
    private TransactionType type;
    private String category;
    private WalletType walletType;
    private long date;
    private String notes;
    private String receiptPath;

    public Transaction(String id, String userId, String title, double amount, TransactionType type, String category, WalletType walletType, long date, String notes, String receiptPath) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.walletType = walletType;
        this.date = date;
        this.notes = notes;
        this.receiptPath = receiptPath;
    }

    public Transaction(String userId, String title, double amount, TransactionType type, String category, WalletType walletType, long date, String notes, String receiptPath) {
        this(UUID.randomUUID().toString(), userId, title, amount, type, category, walletType, date, notes, receiptPath);
    }

    public enum TransactionType {
        CREDIT, DEBIT
    }

    public enum WalletType {
        UPI, CASH, VAULT
    }

    // Getters and Setters omitted for brevity, but would be here
    public Transaction() {
        // Required for Firebase
    }

    public void setId(String id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setTitle(String title) { this.title = title; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setType(TransactionType type) { this.type = type; }
    public void setCategory(String category) { this.category = category; }
    public void setWalletType(WalletType walletType) { this.walletType = walletType; }
    public void setDate(long date) { this.date = date; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setReceiptPath(String receiptPath) { this.receiptPath = receiptPath; }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getTitle() { return title; }
    public double getAmount() { return amount; }
    public TransactionType getType() { return type; }
    public String getCategory() { return category; }
    public WalletType getWalletType() { return walletType; }
    public long getDate() { return date; }
    public String getNotes() { return notes; }
    public String getReceiptPath() { return receiptPath; }
}
