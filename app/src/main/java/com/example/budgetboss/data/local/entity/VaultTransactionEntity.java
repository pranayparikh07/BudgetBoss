package com.example.budgetboss.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vault_transactions")
public class VaultTransactionEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String userId;
    public String type; // ADD, WITHDRAW
    public double amount;
    public long date;
    public String reason;

    public VaultTransactionEntity(String userId, String type, double amount, long date, String reason) {
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.reason = reason;
    }
}
