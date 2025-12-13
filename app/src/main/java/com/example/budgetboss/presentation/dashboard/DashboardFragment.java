package com.example.budgetboss.presentation.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.transition.MaterialSharedAxis;

import com.example.budgetboss.databinding.FragmentDashboardBinding;
import com.example.budgetboss.presentation.viewmodel.DashboardViewModel;
import com.example.budgetboss.presentation.dashboard.TransactionAdapter;
import com.example.budgetboss.presentation.receipt.ReceiptScannerActivity;
import com.example.budgetboss.utils.NotificationHelper;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.Y, true));
        setReturnTransition(new MaterialSharedAxis(MaterialSharedAxis.Y, false));
    }

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

            // Connect Real Chart Data
            com.example.budgetboss.utils.SimpleLineChartView chartView = view
                    .findViewById(com.example.budgetboss.R.id.chartView);
            if (chartView != null && transactions != null && !transactions.isEmpty()) {
                java.util.List<Float> chartPoints = new java.util.ArrayList<>();
                // Take last 7 or all
                // Transactions are likely sorted Newest First.
                // We want chart Left->Right as Oldest->Newest.
                // So take first N (which are newest) and reverse them.
                int limit = Math.min(transactions.size(), 7);
                for (int i = 0; i < limit; i++) {
                    chartPoints.add((float) transactions.get(i).getAmount());
                }
                java.util.Collections.reverse(chartPoints);
                chartView.setData(chartPoints);
            }
        });

        adapter.setOnItemClickListener(transaction -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("transaction", transaction);
            androidx.navigation.Navigation.findNavController(view)
                    .navigate(com.example.budgetboss.R.id.action_dashboardFragment_to_addTransactionFragment, bundle);
        });

        viewModel.getBalance().observe(getViewLifecycleOwner(), resource -> {
            if (resource.data != null) {
                binding.tvTotalBalance.setText(String.format(Locale.getDefault(), "₹%.2f", resource.data));

                // Update Widget Prefs
                android.content.SharedPreferences prefs = requireContext().getSharedPreferences("BudgetBossPrefs",
                        android.content.Context.MODE_PRIVATE);
                prefs.edit().putFloat("total_balance", resource.data.floatValue()).apply();

                // Trigger Widget Update
                android.content.Intent intent = new android.content.Intent(requireContext(),
                        com.example.budgetboss.widget.BudgetWidget.class);
                intent.setAction(android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                int[] ids = android.appwidget.AppWidgetManager.getInstance(requireContext())
                        .getAppWidgetIds(new android.content.ComponentName(requireContext(),
                                com.example.budgetboss.widget.BudgetWidget.class));
                intent.putExtra(android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                requireContext().sendBroadcast(intent);
            }
        });

        viewModel.getIncome().observe(getViewLifecycleOwner(), resource -> {
            if (resource.data != null) {
                currentIncome = resource.data;
                binding.tvIncome.setText(String.format(Locale.getDefault(), "₹%.2f", currentIncome));
                updateSavings();
            }
        });

        viewModel.getExpense().observe(getViewLifecycleOwner(), resource -> {
            if (resource.data != null) {
                currentExpense = resource.data;
                binding.tvExpense.setText(String.format(Locale.getDefault(), "₹%.2f", currentExpense));
                updateSavings();
            }
        });

        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            // First try to get name from SharedPreferences (profile data)
            android.content.SharedPreferences profilePrefs = requireContext().getSharedPreferences("profile_prefs",
                    android.content.Context.MODE_PRIVATE);
            String profileName = profilePrefs.getString("full_name", null);
            
            if (profileName != null && !profileName.isEmpty()) {
                // Use first name only for greeting
                String firstName = profileName.split(" ")[0];
                binding.tvGreeting.setText(firstName);
            } else if (user != null && user.getUsername() != null) {
                binding.tvGreeting.setText(user.getUsername());
            } else {
                binding.tvGreeting.setText("User");
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
            Bundle args = new Bundle();
            args.putString("prefillType", "income");
            androidx.navigation.Navigation.findNavController(view)
                .navigate(com.example.budgetboss.R.id.action_dashboardFragment_to_addTransactionFragment, args);
        });

        binding.btnQuickExpense.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("prefillType", "expense");
            androidx.navigation.Navigation.findNavController(view)
                .navigate(com.example.budgetboss.R.id.action_dashboardFragment_to_addTransactionFragment, args);
        });

        binding.btnAskAI.setOnClickListener(v -> {
            androidx.navigation.Navigation.findNavController(view)
                    .navigate(com.example.budgetboss.R.id.action_dashboardFragment_to_aiChatFragment);
        });

        binding.btnScanReceipt.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ReceiptScannerActivity.class);
            startActivity(intent);
        });

        binding.btnMyCards.setOnClickListener(v -> {
            androidx.navigation.Navigation.findNavController(view)
                    .navigate(com.example.budgetboss.R.id.action_dashboardFragment_to_cardsFragment);
        });

        // Notification Icon Listener now fires a real notification
        binding.btnNotifications.setOnClickListener(v -> {
            requestNotificationPermissionIfNeeded();
            NotificationHelper.showTransactionNotification(
                requireContext(),
                "Spending update",
                "You have new activity waiting in BudgetBoss"
            );
        });

    }

    private void updateSavings() {
        if (binding != null) {
            double savings = currentIncome - currentExpense;
            binding.tvSavings.setText(String.format(Locale.getDefault(), "₹%.2f", savings));
        }
    }

    private void requestNotificationPermissionIfNeeded() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (requireContext().checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] { android.Manifest.permission.POST_NOTIFICATIONS }, 1101);
            }
        }
    }
}
