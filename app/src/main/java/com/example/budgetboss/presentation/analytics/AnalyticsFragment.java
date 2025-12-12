package com.example.budgetboss.presentation.analytics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetboss.databinding.FragmentAnalyticsBinding;
import com.example.budgetboss.presentation.viewmodel.DashboardViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AnalyticsFragment extends Fragment {

    private FragmentAnalyticsBinding binding;
    private DashboardViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAnalyticsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class); // Reuse DashboardVM for now as it has income/expense

        viewModel.getIncome().observe(getViewLifecycleOwner(), incomeRes -> {
            viewModel.getExpense().observe(getViewLifecycleOwner(), expenseRes -> {
                if (binding != null && incomeRes.data != null && expenseRes.data != null) {
                    double income = incomeRes.data;
                    double expense = expenseRes.data;
                    double total = income + expense;
                    
                    if (total > 0) {
                        int incomePct = (int) ((income / total) * 100);
                        binding.pbIncomeExpense.setProgress(incomePct);
                        binding.tvIncomePct.setText("Income: " + incomePct + "%");
                        binding.tvExpensePct.setText("Expense: " + (100 - incomePct) + "%");
                    }
                }
            });
        });
        
        // Mock Top Categories
        binding.tvTopCategories.setText("Food & Dining: ₹5000\nTransportation: ₹2000\nBills: ₹1500");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
