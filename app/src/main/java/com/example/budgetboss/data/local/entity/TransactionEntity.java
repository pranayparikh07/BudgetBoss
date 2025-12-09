package com.example.budgetboss.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "transactions")
public class TransactionEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String userId;
    public String title;
    public String type; // CREDIT, DEBIT
    public double amount;
    public String category;
    public String walletType; // UPI, CASH, VAULT
    public long date;
    public String notes;
    public String receiptPath;
    public long syncedAt;

    public TransactionEntity(@NonNull String id, String userId, String title, String type, double amount, String category, String walletType, long date, String notes, String receiptPath, long syncedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.walletType = walletType;
        this.date = date;
        this.notes = notes;
        this.receiptPath = receiptPath;
        this.syncedAt = syncedAt;
    }
}
