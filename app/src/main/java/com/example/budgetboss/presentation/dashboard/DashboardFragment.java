package com.example.budgetboss.presentation.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.budgetboss.databinding.FragmentDashboardBinding;
import com.example.budgetboss.presentation.viewmodel.DashboardViewModel;
import com.example.budgetboss.presentation.dashboard.TransactionAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private double currentIncome = 0;
    private double currentExpense = 0;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DashboardViewModel viewModel = new androidx.lifecycle.ViewModelProvider(this).get(DashboardViewModel.class);

        TransactionAdapter adapter = new TransactionAdapter();
        binding.rvTransactions.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(requireContext()));
        binding.rvTransactions.setAdapter(adapter);

        new androidx.recyclerview.widget.ItemTouchHelper(
                new androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(0,
                        androidx.recyclerview.widget.ItemTouchHelper.LEFT
                                | androidx.recyclerview.widget.ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull androidx.recyclerview.widget.RecyclerView recyclerView,
                            @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder,
                            @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder,
                            int direction) {
                        com.example.budgetboss.domain.models.Transaction t = adapter
                                .getItem(viewHolder.getAdapterPosition());
                        viewModel.deleteTransaction(t);
                        com.google.android.material.snackbar.Snackbar
                                .make(binding.getRoot(), "Transaction Deleted",
                                        com.google.android.material.snackbar.Snackbar.LENGTH_LONG)
                                .setAction("Undo", v -> viewModel.addTransaction(t))
                                .show();
                    }
                }).attachToRecyclerView(binding.rvTransactions);

        viewModel.getRecentTransactions().observe(getViewLifecycleOwner(), transactions -> {
            adapter.setTransactions(transactions);
        });

        adapter.setOnItemClickListener(transaction -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("transaction", transaction);
            androidx.navigation.Navigation.findNavController(view)
                    .navigate(com.example.budgetboss.R.id.action_dashboardFragment_to_addTransactionFragment, bundle);
        });

        viewModel.getBalance().observe(getViewLifecycleOwner(), resource -> {
            if (resource.data != null) {
                binding.tvTotalBalance.setText(String.format("₹%.2f", resource.data));
            }
        });

        viewModel.getIncome().observe(getViewLifecycleOwner(), resource -> {
            if (resource.data != null) {
                currentIncome = resource.data;
                binding.tvIncome.setText(String.format("₹%.2f", currentIncome));
                updateSavings();
            }
        });

        viewModel.getExpense().observe(getViewLifecycleOwner(), resource -> {
            if (resource.data != null) {
                currentExpense = resource.data;
                binding.tvExpense.setText(String.format("₹%.2f", currentExpense));
                updateSavings();
            }
        });

        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.tvGreeting.setText("Hello, " + user.getUsername());
            }
        });

        binding.fabAdd.setOnClickListener(v -> {
            androidx.navigation.Navigation.findNavController(view)
                    .navigate(com.example.budgetboss.R.id.action_dashboardFragment_to_addTransactionFragment);
        });

        binding.btnViewAll.setOnClickListener(v -> androidx.navigation.Navigation.findNavController(view)
                .navigate(com.example.budgetboss.R.id.action_dashboardFragment_to_transactionsFragment));

        binding.btnAnalytics.setOnClickListener(v -> androidx.navigation.Navigation.findNavController(view)
                .navigate(com.example.budgetboss.R.id.action_dashboardFragment_to_analyticsFragment));

        binding.btnQuickIncome.setOnClickListener(v -> {
            // Can pass args to pre-select type
            androidx.navigation.Navigation.findNavController(view)
                    .navigate(com.example.budgetboss.R.id.action_dashboardFragment_to_addTransactionFragment);
        });

        binding.btnQuickExpense.setOnClickListener(v -> {
            androidx.navigation.Navigation.findNavController(view)
                    .navigate(com.example.budgetboss.R.id.action_dashboardFragment_to_addTransactionFragment);
        });

        setupChart(view);
    }

    private void setupChart(View view) {
        com.example.budgetboss.utils.SimpleLineChartView chartView = view
                .findViewById(com.example.budgetboss.R.id.chartView);
        if (chartView != null) {
            java.util.List<Float> mockData = new java.util.ArrayList<>();
            mockData.add(100f);
            mockData.add(400f);
            mockData.add(300f);
            mockData.add(700f);
            mockData.add(500f);
            mockData.add(800f);
            chartView.setData(mockData);
        }
    }

    private void updateSavings() {
        if (binding != null) {
            double savings = currentIncome - currentExpense;
            binding.tvSavings.setText(String.format("₹%.2f", savings));
        }
    }
}
