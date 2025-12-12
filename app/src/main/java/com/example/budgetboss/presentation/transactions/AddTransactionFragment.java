package com.example.budgetboss.presentation.transactions;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.budgetboss.databinding.FragmentAddTransactionBinding;
import com.example.budgetboss.domain.models.Transaction;
import com.example.budgetboss.presentation.viewmodel.TransactionViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddTransactionFragment extends Fragment {

    private FragmentAddTransactionBinding binding;
    private TransactionViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentAddTransactionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private Transaction transactionToEdit;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        if (getArguments() != null) {
            transactionToEdit = (Transaction) getArguments().getSerializable("transaction");
        }

        if (transactionToEdit != null) {
            binding.tvTitle.setText("Edit Transaction");
            binding.etTitle.getEditText().setText(transactionToEdit.getTitle());
            binding.etAmount.getEditText().setText(String.valueOf(transactionToEdit.getAmount()));
            binding.etCategory.getEditText().setText(transactionToEdit.getCategory());
            if (transactionToEdit.getType() == Transaction.TransactionType.CREDIT) {
                binding.rbIncome.setChecked(true);
            } else {
                binding.rbExpense.setChecked(true);
            }
            binding.btnSave.setText("Update Transaction");
            binding.btnDelete.setVisibility(View.VISIBLE);
            binding.btnDelete.setOnClickListener(v -> {
                // Check balance here? ViewModel handles it, but we need to fetch balance.
                // For now, assume ViewModel handles error posting.
                // We need to pass current balance to deleteTransaction.
                // Since we don't have balance stream here easily without observing,
                // let's assume balance check is done in VM with latest data or pass 0 and let
                // VM fetch (or modify VM to not require it if it can fetch internally).
                // Actually VM `getBalance()` returns LiveData. We can observe it once.
                viewModel.getBalance().observe(getViewLifecycleOwner(), resource -> {
                    if (resource.data != null) {
                        double currentBalance = resource.data;
                        // Pre-check for insufficient balance to avoid navigation if error
                        if (transactionToEdit.getType() == Transaction.TransactionType.CREDIT) {
                            if (currentBalance - transactionToEdit.getAmount() < 0) {
                                Toast.makeText(requireContext(), "Cannot delete: Insufficient balance!",
                                        Toast.LENGTH_SHORT).show();
                                return; // Do not delete, do not nav back
                            }
                        }

                        viewModel.deleteTransaction(transactionToEdit, currentBalance);
                        Navigation.findNavController(view).popBackStack();
                    }
                });
            });
        }

        binding.btnSave.setOnClickListener(v -> {
            String title = binding.etTitle.getEditText().getText().toString().trim();
            String amountStr = binding.etAmount.getEditText().getText().toString().trim();
            String categoryInput = binding.etCategory.getEditText().getText().toString().trim();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(amountStr) || TextUtils.isEmpty(categoryInput)) {
                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);
            Transaction.TransactionType type = binding.rbIncome.isChecked() ? Transaction.TransactionType.CREDIT
                    : Transaction.TransactionType.DEBIT;

            String source = binding.rbCash.isChecked() ? "Cash" : "UPI";
            String category = categoryInput + " | " + source;

            if (transactionToEdit != null) {
                transactionToEdit.setTitle(title);
                transactionToEdit.setAmount(amount);
                transactionToEdit.setCategory(category);
                transactionToEdit.setType(type);
                viewModel.updateTransaction(transactionToEdit);
                Toast.makeText(requireContext(), "Transaction Updated", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.addTransaction(title, amount, type, category);
                Toast.makeText(requireContext(), "Transaction Saved", Toast.LENGTH_SHORT).show();
            }
            Navigation.findNavController(view).popBackStack();
        });

        // Error handling for delete
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
