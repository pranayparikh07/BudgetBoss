package com.example.budgetboss.di;

import android.content.Context;

import androidx.room.Room;

import com.example.budgetboss.data.local.AppDatabase;
import com.example.budgetboss.data.local.dao.BudgetDao;
import com.example.budgetboss.data.local.dao.TransactionDao;
import com.example.budgetboss.data.local.dao.UserDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "budgetboss_db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    public UserDao provideUserDao(AppDatabase database) {
        return database.userDao();
    }

    @Provides
    public TransactionDao provideTransactionDao(AppDatabase database) {
        return database.transactionDao();
    }

    @Provides
    public BudgetDao provideBudgetDao(AppDatabase database) {
        return database.budgetDao();
    }
}
