package com.example.budgetboss.presentation.budget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.budgetboss.databinding.FragmentBudgetBinding;

import com.example.budgetboss.presentation.viewmodel.BudgetViewModel;

import java.util.ArrayList;
import java.util.List;
import com.example.budgetboss.data.local.entity.BudgetGoalEntity;

import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BudgetFragment extends Fragment {

    private FragmentBudgetBinding binding;
    private BudgetViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentBudgetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(BudgetViewModel.class);

        BudgetAdapter adapter = new BudgetAdapter();
        adapter.setListener(this::showBudgetOptions);
        binding.rvBudgets.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(requireContext()));
        binding.rvBudgets.setAdapter(adapter);

        viewModel.getAllBudgets().observe(getViewLifecycleOwner(), budgets -> {
            adapter.setBudgets(budgets);
            // Show/hide empty state
            if (budgets == null || budgets.isEmpty()) {
                binding.layoutEmpty.setVisibility(View.VISIBLE);
                binding.rvBudgets.setVisibility(View.GONE);
            } else {
                binding.layoutEmpty.setVisibility(View.GONE);
                binding.rvBudgets.setVisibility(View.VISIBLE);
            }
        });

        binding.fabAddBudget.setOnClickListener(v -> showAddBudgetDialog(null));
        binding.btnAddFirstBudget.setOnClickListener(v -> showAddBudgetDialog(null));
    }

    private void showBudgetOptions(BudgetGoalEntity budget) {
        String[] options = { "View Analysis", "Edit", "Delete" };
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Budget Options")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) { // View Analysis
                        Bundle bundle = new Bundle();
                        bundle.putString("budgetId", budget.category);
                        androidx.navigation.Navigation.findNavController(requireView())
                                .navigate(com.example.budgetboss.R.id.action_budgetFragment_to_budgetDetailsFragment,
                                        bundle);
                    } else if (which == 1) { // Edit
                        showAddBudgetDialog(budget);
                    } else if (which == 2) { // Delete
                        new android.app.AlertDialog.Builder(requireContext())
                                .setTitle("Delete Budget")
                                .setMessage("Are you sure you want to delete this budget goal?")
                                .setPositiveButton("Delete", (d, w) -> viewModel.deleteBudget(budget))
                                .setNegativeButton("Cancel", null)
                                .show();
                    }
                })
                .show();
    }

    private void showAddBudgetDialog(@Nullable BudgetGoalEntity budgetToEdit) {
        View dialogView = getLayoutInflater().inflate(com.example.budgetboss.R.layout.dialog_add_budget, null);
        
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext(), 
            android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        builder.setView(dialogView);
        android.app.AlertDialog dialog = builder.create();
        
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            // Add dim background
            dialog.getWindow().setDimAmount(0.7f);
        }
        
        // Get views
        android.widget.TextView tvTitle = dialogView.findViewById(com.example.budgetboss.R.id.tvDialogTitle);
        com.google.android.material.textfield.TextInputEditText etCategory = dialogView.findViewById(com.example.budgetboss.R.id.etCategory);
        com.google.android.material.textfield.TextInputEditText etLimit = dialogView.findViewById(com.example.budgetboss.R.id.etLimit);
        com.google.android.material.chip.ChipGroup chipGroupPeriod = dialogView.findViewById(com.example.budgetboss.R.id.chipGroupPeriod);
        com.google.android.material.chip.Chip chipWeekly = dialogView.findViewById(com.example.budgetboss.R.id.chipWeekly);
        com.google.android.material.chip.Chip chipMonthly = dialogView.findViewById(com.example.budgetboss.R.id.chipMonthly);
        com.google.android.material.chip.Chip chipYearly = dialogView.findViewById(com.example.budgetboss.R.id.chipYearly);
        com.google.android.material.button.MaterialButton btnCancel = dialogView.findViewById(com.example.budgetboss.R.id.btnCancel);
        com.google.android.material.button.MaterialButton btnSave = dialogView.findViewById(com.example.budgetboss.R.id.btnSave);
        
        // Set title based on mode
        if (budgetToEdit != null) {
            tvTitle.setText("Edit Budget Goal");
            btnSave.setText("Update");
            etCategory.setText(budgetToEdit.category);
            etLimit.setText(String.valueOf(budgetToEdit.limitAmount));
            
            // Set period chip
            if ("Weekly".equals(budgetToEdit.period)) chipWeekly.setChecked(true);
            else if ("Yearly".equals(budgetToEdit.period)) chipYearly.setChecked(true);
            else chipMonthly.setChecked(true);
        }
        
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        btnSave.setOnClickListener(v -> {
            String category = etCategory.getText() != null ? etCategory.getText().toString().trim() : "";
            String limitStr = etLimit.getText() != null ? etLimit.getText().toString().trim() : "";
            
            // Get selected period
            String period = "Monthly";
            int checkedId = chipGroupPeriod.getCheckedChipId();
            if (checkedId == com.example.budgetboss.R.id.chipWeekly) period = "Weekly";
            else if (checkedId == com.example.budgetboss.R.id.chipYearly) period = "Yearly";
            
            if (category.isEmpty()) {
                etCategory.setError("Please enter a category");
                return;
            }
            if (limitStr.isEmpty()) {
                etLimit.setError("Please enter a limit");
                return;
            }
            
            double limit = Double.parseDouble(limitStr);
            
            if (budgetToEdit != null) {
                budgetToEdit.category = category;
                budgetToEdit.limitAmount = limit;
                budgetToEdit.period = period;
                viewModel.updateBudget(budgetToEdit);
                showSuccessMessage("Budget updated successfully!");
            } else {
                String userId = com.google.firebase.auth.FirebaseAuth.getInstance().getUid();
                if (userId == null) userId = "local_user";
                
                BudgetGoalEntity budget = new BudgetGoalEntity(
                    userId, category, limit, period, System.currentTimeMillis());
                viewModel.addBudget(budget);
                showSuccessMessage("Budget created! Start tracking your " + category + " expenses.");
            }
            dialog.dismiss();
        });
        
        dialog.show();
    }
    
    private void showSuccessMessage(String message) {
        com.google.android.material.snackbar.Snackbar.make(binding.getRoot(), message, 
            com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
            .setBackgroundTint(requireContext().getResources().getColor(com.example.budgetboss.R.color.status_income, requireContext().getTheme()))
            .setTextColor(android.graphics.Color.WHITE)
            .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
