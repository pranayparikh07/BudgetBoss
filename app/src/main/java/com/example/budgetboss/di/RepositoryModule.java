package com.example.budgetboss.di;

import com.example.budgetboss.data.repository.AuthRepositoryImpl;
import com.example.budgetboss.domain.repository.AuthRepository;
import com.example.budgetboss.data.repository.TransactionRepositoryImpl;
import com.example.budgetboss.domain.repository.TransactionRepository;
import com.example.budgetboss.data.repository.BudgetRepositoryImpl;
import com.example.budgetboss.domain.repository.BudgetRepository;
import com.example.budgetboss.domain.repository.VaultRepository;
import com.example.budgetboss.data.repository.VaultRepositoryImpl;
import com.example.budgetboss.domain.repository.InvestmentRepository;
import com.example.budgetboss.data.repository.InvestmentRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {

    @Binds
    public abstract AuthRepository bindAuthRepository(AuthRepositoryImpl authRepositoryImpl);

    @Binds
    public abstract TransactionRepository bindTransactionRepository(TransactionRepositoryImpl transactionRepositoryImpl);

    @Binds
    @Singleton
    public abstract BudgetRepository bindBudgetRepository(BudgetRepositoryImpl budgetRepositoryImpl);

    @Binds
    @Singleton
    public abstract VaultRepository bindVaultRepository(VaultRepositoryImpl vaultRepositoryImpl);

    @Binds
    @Singleton
    public abstract InvestmentRepository bindInvestmentRepository(InvestmentRepositoryImpl investmentRepositoryImpl);
}
