package com.example.budgetboss.di;

import com.example.budgetboss.data.repository.AuthRepositoryImpl;
import com.example.budgetboss.domain.repository.AuthRepository;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {

    @Binds
    public abstract AuthRepository bindAuthRepository(AuthRepositoryImpl authRepositoryImpl);

    @Binds
    public abstract TransactionRepository bindTransactionRepository(TransactionRepositoryImpl transactionRepositoryImpl);

    @Binds
    public abstract BudgetRepository bindBudgetRepository(BudgetRepositoryImpl budgetRepositoryImpl);
}
