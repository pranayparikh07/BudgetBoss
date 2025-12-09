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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddTransactionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        binding.btnSave.setOnClickListener(v -> {
            String title = binding.etTitle.getEditText().getText().toString().trim();
            String amountStr = binding.etAmount.getEditText().getText().toString().trim();
            String category = binding.etCategory.getEditText().getText().toString().trim();
            
            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(amountStr) || TextUtils.isEmpty(category)) {
                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);
            Transaction.TransactionType type = binding.rbIncome.isChecked() ? Transaction.TransactionType.CREDIT : Transaction.TransactionType.DEBIT;
            
            viewModel.addTransaction(title, amount, type, category);
            Toast.makeText(requireContext(), "Transaction Saved", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).popBackStack();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
