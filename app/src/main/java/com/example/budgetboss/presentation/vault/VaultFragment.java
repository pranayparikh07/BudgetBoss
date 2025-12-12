package com.example.budgetboss.presentation.vault;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.budgetboss.databinding.FragmentVaultBinding;

import androidx.lifecycle.ViewModelProvider;
import com.example.budgetboss.presentation.viewmodel.VaultViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VaultFragment extends Fragment {

    private FragmentVaultBinding binding;
    private VaultViewModel viewModel;
    private boolean isBalanceVisible = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVaultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(VaultViewModel.class);

        // UI State Observation
        viewModel.isPinSet().observe(getViewLifecycleOwner(), isPinSet -> {
            if (isPinSet) {
                binding.tvPinMessage.setText("Enter PIN to Unlocked Vault");
                binding.btnUnlock.setText("Unlock");
            } else {
                binding.tvPinMessage.setText("Set New 6-Digit PIN");
                binding.btnUnlock.setText("Set PIN");
            }
        });

        viewModel.isUnlocked().observe(getViewLifecycleOwner(), isUnlocked -> {
            if (isUnlocked) {
                binding.layoutPin.setVisibility(View.GONE);
                binding.layoutContent.setVisibility(View.VISIBLE);
            } else {
                binding.layoutPin.setVisibility(View.VISIBLE);
                binding.layoutContent.setVisibility(View.GONE);
            }
        });

        viewModel.getBalance().observe(getViewLifecycleOwner(), balance -> {
             if (isBalanceVisible) {
                 binding.tvVaultBalance.setText(String.format("₹%.2f", balance));
             } else {
                 binding.tvVaultBalance.setText("******");
             }
        });
        
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            android.widget.Toast.makeText(requireContext(), error, android.widget.Toast.LENGTH_SHORT).show();
        });

        // Click Listeners
        binding.btnUnlock.setOnClickListener(v -> {
            String pin = binding.etPin.getText().toString(); // Direct access or getEditText()
            if (pin.length() != 6) {
                android.widget.Toast.makeText(requireContext(), "PIN must be 6 digits", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            if (Boolean.TRUE.equals(viewModel.isPinSet().getValue())) {
                viewModel.validatePin(pin);
            } else {
                viewModel.setPin(pin);
            }
        });

        binding.btnReveal.setOnClickListener(v -> {
            isBalanceVisible = !isBalanceVisible;
            Double balance = viewModel.getBalance().getValue();
            if (balance == null) balance = 0.0;
            
            if (isBalanceVisible) {
                binding.tvVaultBalance.setText(String.format("₹%.2f", balance));
                binding.btnReveal.setText("Hide Balance");
            } else {
                binding.tvVaultBalance.setText("******");
                binding.btnReveal.setText("Reveal Balance");
            }
        });

        binding.btnAddFunds.setOnClickListener(v -> {
            // Simple Dialog for MVP
            showAmountDialog(true);
        });

        binding.btnWithdraw.setOnClickListener(v -> {
             showAmountDialog(false);
        });
    }

    private void showAmountDialog(boolean isDeposit) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
        builder.setTitle(isDeposit ? "Add Funds" : "Withdraw Funds");
        final android.widget.EditText input = new android.widget.EditText(requireContext());
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String amountStr = input.getText().toString();
            if (!amountStr.isEmpty()) {
                double amount = Double.parseDouble(amountStr);
                if (isDeposit) {
                    viewModel.addToVault(amount);
                } else {
                    viewModel.withdrawFromVault(amount);
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
