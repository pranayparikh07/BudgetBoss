package com.example.budgetboss.presentation.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.budgetboss.databinding.FragmentDashboardBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DashboardViewModel viewModel = new androidx.lifecycle.ViewModelProvider(this).get(DashboardViewModel.class);
        
        TransactionAdapter adapter = new TransactionAdapter();
        binding.rvTransactions.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(requireContext()));
        binding.rvTransactions.setAdapter(adapter);

        viewModel.getRecentTransactions().observe(getViewLifecycleOwner(), transactions -> {
            adapter.setTransactions(transactions);
        });

        viewModel.getBalance().observe(getViewLifecycleOwner(), resource -> {
            if (resource.data != null) {
                binding.tvTotalBalance.setText(String.format("$%.2f", resource.data));
            }
        });
        
        viewModel.getIncome().observe(getViewLifecycleOwner(), resource -> {
             if (resource.data != null) {
                binding.tvIncome.setText(String.format("$%.2f", resource.data)); 
             }
        });

        viewModel.getExpense().observe(getViewLifecycleOwner(), resource -> {
             if (resource.data != null) {
                binding.tvExpense.setText(String.format("$%.2f", resource.data)); 
             }
        });

        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.tvGreeting.setText("Hello, " + user.getUsername());
            }
        });
        
        binding.fabAdd.setOnClickListener(v -> {
            androidx.navigation.Navigation.findNavController(view).navigate(com.example.budgetboss.R.id.action_dashboardFragment_to_addTransactionFragment);
        });
    }
}
