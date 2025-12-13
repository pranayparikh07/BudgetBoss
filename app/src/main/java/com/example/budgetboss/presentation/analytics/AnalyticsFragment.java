package com.example.budgetboss.presentation.analytics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Locale;

import com.example.budgetboss.databinding.FragmentAnalyticsBinding;
import com.example.budgetboss.presentation.viewmodel.DashboardViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AnalyticsFragment extends Fragment {

    private FragmentAnalyticsBinding binding;
    private DashboardViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentAnalyticsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class); // Reuse DashboardVM for now as it has
                                                                               // income/expense
        final double[] incomeHolder = { 0.0 };
        final double[] expenseHolder = { 0.0 };

        viewModel.getIncome().observe(getViewLifecycleOwner(), incomeRes -> {
            if (incomeRes != null && incomeRes.data != null) {
                incomeHolder[0] = incomeRes.data;
                updateIncomeExpense(incomeHolder[0], expenseHolder[0]);
            }
        });

        viewModel.getExpense().observe(getViewLifecycleOwner(), expenseRes -> {
            if (expenseRes != null && expenseRes.data != null) {
                expenseHolder[0] = expenseRes.data;
                updateIncomeExpense(incomeHolder[0], expenseHolder[0]);
            }
        });

        // Mock Top Categories
        viewModel.getRecentTransactions().observe(getViewLifecycleOwner(), transactions -> {
            if (binding != null && transactions != null) {
                java.util.Map<String, Double> categoryMap = new java.util.HashMap<>();
                for (com.example.budgetboss.domain.models.Transaction t : transactions) {
                    // Only count expenses for "Top Spending Categories"
                    if (t.getType() == com.example.budgetboss.domain.models.Transaction.TransactionType.DEBIT) {
                        // Parse Category "Name | Type" -> "Name"
                        String rawCat = t.getCategory();
                        String name = rawCat.contains("|") ? rawCat.split("\\|")[0].trim() : rawCat;
                        double current = categoryMap.getOrDefault(name, 0.0);
                        categoryMap.put(name, current + t.getAmount());
                    }
                }

                // Sort by value desc
                java.util.List<java.util.Map.Entry<String, Double>> list = new java.util.ArrayList<>(
                        categoryMap.entrySet());
                list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

                StringBuilder sb = new StringBuilder();
                int count = 0;
                for (java.util.Map.Entry<String, Double> entry : list) {
                    if (count >= 3) {
                        break;
                    }
                    sb.append(entry.getKey()).append(": ₹").append(String.format(Locale.getDefault(), "%.2f", entry.getValue()))
                            .append("\n");
                    count++;
                }

                if (sb.length() > 0) {
                    binding.tvTopCategories.setText(sb.toString().trim());
                } else {
                    binding.tvTopCategories.setText("No expense data available.");
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateIncomeExpense(double income, double expense) {
        if (binding == null)
            return;
        double total = income + expense;
        if (total <= 0)
            return;
        int incomePct = (int) ((income / total) * 100);
        binding.pbIncomeExpense.setProgress(incomePct);
        binding.tvIncomePct.setText("Income: " + incomePct + "%");
        binding.tvExpensePct.setText("Expense: " + (100 - incomePct) + "%");
    }
}
