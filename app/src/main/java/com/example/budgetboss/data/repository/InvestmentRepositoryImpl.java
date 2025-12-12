package com.example.budgetboss.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.budgetboss.domain.models.Investment;
import com.example.budgetboss.domain.repository.InvestmentRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class InvestmentRepositoryImpl implements InvestmentRepository {

    // For MVP, we'll use in-memory List + Mock.
    // Ideally this goes to Room/Firebase.
    // I'll implement a simple MutableLiveData list.

    private final MutableLiveData<List<Investment>> investments = new MutableLiveData<>(new ArrayList<>());
    private final List<Investment> investmentList = new ArrayList<>();

    @Inject
    public InvestmentRepositoryImpl() {
        // Add dummy data for visual check
        addInvestment(new Investment("1", "Home Loan", 5000000, Investment.InvestmentType.LOAN_TAKEN, 8.5,
                System.currentTimeMillis(), "HDFC"));
        addInvestment(new Investment("2", "SIP Bluechip", 5000, Investment.InvestmentType.SIP, 12,
                System.currentTimeMillis(), "Monthly"));
    }

    @Override
    public void addInvestment(Investment investment) {
        investmentList.add(investment);
        investments.postValue(investmentList);
    }

    @Override
    public void updateInvestment(Investment investment) {
        for (int i = 0; i < investmentList.size(); i++) {
            if (investmentList.get(i).getId().equals(investment.getId())) {
                investmentList.set(i, investment);
                break;
            }
        }
        investments.postValue(investmentList);
    }

    @Override
    public void deleteInvestment(Investment investment) {
        investmentList.remove(investment);
        investments.postValue(investmentList);
    }

    @Override
    public LiveData<List<Investment>> getAllInvestments() {
        return investments;
    }
}
