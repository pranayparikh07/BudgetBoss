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
        });

        binding.fabAddBudget.setOnClickListener(v -> showAddBudgetDialog(null));
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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
        builder.setTitle(budgetToEdit == null ? "Set Budget Goal" : "Edit Budget Goal");

        android.widget.LinearLayout layout = new android.widget.LinearLayout(requireContext());
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final android.widget.EditText categoryInput = new android.widget.EditText(requireContext());
        categoryInput.setHint("Category (e.g. Food, Travel)");
        if (budgetToEdit != null)
            categoryInput.setText(budgetToEdit.category);
        layout.addView(categoryInput);

        final android.widget.EditText limitInput = new android.widget.EditText(requireContext());
        limitInput.setHint("Limit Amount (₹)");
        limitInput.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if (budgetToEdit != null)
            limitInput.setText(String.valueOf(budgetToEdit.limitAmount));
        layout.addView(limitInput);

        final android.widget.Spinner periodSpinner = new android.widget.Spinner(requireContext());
        String[] periods = new String[] { "Monthly", "Weekly", "Yearly" };
        android.widget.ArrayAdapter<String> periodAdapter = new android.widget.ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, periods);
        periodSpinner.setAdapter(periodAdapter);
        if (budgetToEdit != null) {
            for (int i = 0; i < periods.length; i++) {
                if (periods[i].equals(budgetToEdit.period)) {
                    periodSpinner.setSelection(i);
                    break;
                }
            }
        }
        layout.addView(periodSpinner);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String category = categoryInput.getText().toString().trim();
            String limitStr = limitInput.getText().toString().trim();
            String period = (String) periodSpinner.getSelectedItem();

            if (!category.isEmpty() && !limitStr.isEmpty()) {
                double limit = Double.parseDouble(limitStr);

                if (budgetToEdit != null) {
                    budgetToEdit.category = category;
                    budgetToEdit.limitAmount = limit;
                    budgetToEdit.period = period;
                    viewModel.updateBudget(budgetToEdit);
                } else {
                    String userId = com.google.firebase.auth.FirebaseAuth.getInstance().getUid();
                    if (userId == null)
                        userId = "local_user";

                    BudgetGoalEntity budget = new BudgetGoalEntity(
                            userId,
                            category,
                            limit,
                            period,
                            System.currentTimeMillis());
                    viewModel.addBudget(budget);
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
