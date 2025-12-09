package com.example.budgetboss.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.budgetboss.data.local.dao.BudgetDao;
import com.example.budgetboss.data.local.dao.TransactionDao;
import com.example.budgetboss.data.local.dao.UserDao;
import com.example.budgetboss.data.local.entity.BudgetGoalEntity;
import com.example.budgetboss.data.local.entity.TransactionEntity;
import com.example.budgetboss.data.local.entity.UserEntity;
import com.example.budgetboss.data.local.entity.VaultTransactionEntity;

@Database(entities = {UserEntity.class, TransactionEntity.class, BudgetGoalEntity.class, VaultTransactionEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract TransactionDao transactionDao();
    public abstract BudgetDao budgetDao();
}
