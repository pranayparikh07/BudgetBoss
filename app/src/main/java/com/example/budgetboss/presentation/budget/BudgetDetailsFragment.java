package com.example.budgetboss.presentation.budget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.budgetboss.databinding.FragmentBudgetDetailsBinding;
import com.example.budgetboss.presentation.viewmodel.BudgetViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BudgetDetailsFragment extends Fragment {

    private FragmentBudgetDetailsBinding binding;
    private BudgetViewModel viewModel;
    // Assuming passed via arguments
    private String budgetId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentBudgetDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(BudgetViewModel.class);

        if (getArguments() != null) {
            budgetId = getArguments().getString("budgetId");
        }

        // Mock Data for now as we don't have getBudgetById method exposed yet or
        // arguments passing fully set up
        binding.tvCategoryName.setText(budgetId != null ? budgetId : "Mock Category");
        binding.cpIndicator.setProgress(70);
        binding.tvRemaining.setText("₹1,500 Remaining");
        binding.tvLimit.setText("of ₹5,000 Limit");

        // Setup Recycler
        // binding.rvCategoryTransactions.setAdapter(...);
        binding.rvCategoryTransactions.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.fabEditBudget.setOnClickListener(v -> {
            // Show edit dialog
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
