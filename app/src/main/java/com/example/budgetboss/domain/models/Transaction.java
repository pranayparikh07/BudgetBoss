package com.example.budgetboss.domain.models;

import java.util.UUID;

public class Transaction {
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

    public Transaction(String userId, String title, double amount, TransactionType type, String category, WalletType walletType, long date, String notes, String receiptPath) {
        this.id = UUID.randomUUID().toString();
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

    public enum TransactionType {
        CREDIT, DEBIT
    }

    public enum WalletType {
        UPI, CASH, VAULT
    }

    // Getters and Setters omitted for brevity, but would be here
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
