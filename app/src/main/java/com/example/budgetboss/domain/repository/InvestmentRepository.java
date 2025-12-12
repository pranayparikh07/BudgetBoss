package com.example.budgetboss.domain.repository;

import androidx.lifecycle.LiveData;
import com.example.budgetboss.domain.models.Investment;
import java.util.List;

public interface InvestmentRepository {
    void addInvestment(Investment investment);

    void updateInvestment(Investment investment);

    void deleteInvestment(Investment investment);

    LiveData<List<Investment>> getAllInvestments();
}
