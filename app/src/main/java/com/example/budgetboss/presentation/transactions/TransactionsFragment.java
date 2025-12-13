package com.example.budgetboss.presentation.transactions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.budgetboss.databinding.FragmentTransactionsBinding;
import com.example.budgetboss.presentation.dashboard.TransactionAdapter;
import com.example.budgetboss.presentation.viewmodel.DashboardViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TransactionsFragment extends Fragment {

    private FragmentTransactionsBinding binding;
    private DashboardViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentTransactionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        TransactionAdapter adapter = new TransactionAdapter();
        binding.rvTransactions.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvTransactions.setAdapter(adapter);

        viewModel.getRecentTransactions().observe(getViewLifecycleOwner(), transactions -> {
            adapter.setTransactions(transactions);
            // Show/hide empty state
            if (transactions == null || transactions.isEmpty()) {
                binding.layoutEmpty.setVisibility(android.view.View.VISIBLE);
                binding.rvTransactions.setVisibility(android.view.View.GONE);
            } else {
                binding.layoutEmpty.setVisibility(android.view.View.GONE);
                binding.rvTransactions.setVisibility(android.view.View.VISIBLE);
            }
        });
        
        // Empty state button
        binding.btnAddTransaction.setOnClickListener(v -> {
            androidx.navigation.Navigation.findNavController(view)
                .navigate(com.example.budgetboss.R.id.action_transactionsFragment_to_addTransactionFragment);
        });

        // TODO: Add search/filter logic if requested later
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
