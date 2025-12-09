package com.example.budgetboss.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String username;
    public String email;
    public double upiBalance;
    public double cashBalance;
    public double vaultBalance;
    public String vaultPin;
    public long createdAt;

    public UserEntity(@NonNull String id, String username, String email, double upiBalance, double cashBalance, double vaultBalance, String vaultPin, long createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.upiBalance = upiBalance;
        this.cashBalance = cashBalance;
        this.vaultBalance = vaultBalance;
        this.vaultPin = vaultPin;
        this.createdAt = createdAt;
    }
}
