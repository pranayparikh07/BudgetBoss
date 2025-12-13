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

import java.util.Locale;

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
                 binding.tvVaultBalance.setText(String.format(Locale.getDefault(), "₹%.2f", balance));
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
                binding.tvVaultBalance.setText(String.format(Locale.getDefault(), "₹%.2f", balance));
                binding.btnReveal.setText("Hide Balance");
            } else {
                binding.tvVaultBalance.setText("******");
                binding.btnReveal.setText("Reveal Balance");
            }
        });

        binding.btnAddFunds.setOnClickListener(v -> {
            showVaultTransactionDialog(true);
        });

        binding.btnWithdraw.setOnClickListener(v -> {
            showVaultTransactionDialog(false);
        });
    }

    private void showVaultTransactionDialog(boolean isDeposit) {
        View dialogView = getLayoutInflater().inflate(com.example.budgetboss.R.layout.dialog_vault_transaction, null);
        
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext(),
            android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        builder.setView(dialogView);
        android.app.AlertDialog dialog = builder.create();
        
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialog.getWindow().setDimAmount(0.7f);
        }
        
        // Get views
        android.widget.TextView tvTitle = dialogView.findViewById(com.example.budgetboss.R.id.tvDialogTitle);
        android.widget.TextView tvSubtitle = dialogView.findViewById(com.example.budgetboss.R.id.tvDialogSubtitle);
        android.widget.FrameLayout iconContainer = dialogView.findViewById(com.example.budgetboss.R.id.iconContainer);
        android.widget.ImageView ivIcon = dialogView.findViewById(com.example.budgetboss.R.id.ivIcon);
        com.google.android.material.card.MaterialCardView cardCash = dialogView.findViewById(com.example.budgetboss.R.id.cardCash);
        com.google.android.material.card.MaterialCardView cardUPI = dialogView.findViewById(com.example.budgetboss.R.id.cardUPI);
        com.google.android.material.textfield.TextInputEditText etAmount = dialogView.findViewById(com.example.budgetboss.R.id.etAmount);
        com.google.android.material.button.MaterialButton btnCancel = dialogView.findViewById(com.example.budgetboss.R.id.btnCancel);
        com.google.android.material.button.MaterialButton btnConfirm = dialogView.findViewById(com.example.budgetboss.R.id.btnConfirm);
        
        // Configure based on deposit/withdraw
        if (isDeposit) {
            tvTitle.setText("Add Funds");
            tvSubtitle.setText("Add money to your secure vault");
            iconContainer.setBackgroundResource(com.example.budgetboss.R.drawable.bg_stat_income);
            ivIcon.setImageResource(com.example.budgetboss.R.drawable.ic_add_income);
            ivIcon.setImageTintList(android.content.res.ColorStateList.valueOf(
                requireContext().getResources().getColor(com.example.budgetboss.R.color.status_income, requireContext().getTheme())));
            btnConfirm.setText("Add Funds");
        } else {
            tvTitle.setText("Withdraw Funds");
            tvSubtitle.setText("Withdraw money from your vault");
            iconContainer.setBackgroundResource(com.example.budgetboss.R.drawable.bg_stat_expense);
            ivIcon.setImageResource(com.example.budgetboss.R.drawable.ic_add_expense);
            ivIcon.setImageTintList(android.content.res.ColorStateList.valueOf(
                requireContext().getResources().getColor(com.example.budgetboss.R.color.status_expense, requireContext().getTheme())));
            btnConfirm.setText("Withdraw");
        }
        
        // Payment method selection
        final String[] selectedMethod = {"Cash"}; // Default
        cardCash.setChecked(true);
        cardCash.setStrokeColor(requireContext().getResources().getColor(com.example.budgetboss.R.color.blue_primary, requireContext().getTheme()));
        
        cardCash.setOnClickListener(v -> {
            selectedMethod[0] = "Cash";
            cardCash.setChecked(true);
            cardUPI.setChecked(false);
            cardCash.setStrokeColor(requireContext().getResources().getColor(com.example.budgetboss.R.color.blue_primary, requireContext().getTheme()));
            cardUPI.setStrokeColor(requireContext().getResources().getColor(com.example.budgetboss.R.color.colorCardBorder, requireContext().getTheme()));
        });
        
        cardUPI.setOnClickListener(v -> {
            selectedMethod[0] = "UPI";
            cardUPI.setChecked(true);
            cardCash.setChecked(false);
            cardUPI.setStrokeColor(requireContext().getResources().getColor(com.example.budgetboss.R.color.blue_primary, requireContext().getTheme()));
            cardCash.setStrokeColor(requireContext().getResources().getColor(com.example.budgetboss.R.color.colorCardBorder, requireContext().getTheme()));
        });
        
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        btnConfirm.setOnClickListener(v -> {
            String amountStr = etAmount.getText() != null ? etAmount.getText().toString().trim() : "";
            if (amountStr.isEmpty()) {
                etAmount.setError("Please enter amount");
                return;
            }
            
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                etAmount.setError("Amount must be greater than 0");
                return;
            }
            
            if (isDeposit) {
                viewModel.addToVault(amount);
                showSuccessMessage("₹" + amountStr + " added via " + selectedMethod[0]);
            } else {
                viewModel.withdrawFromVault(amount);
                showSuccessMessage("₹" + amountStr + " withdrawn to " + selectedMethod[0]);
            }
            dialog.dismiss();
        });
        
        dialog.show();
        // Set layout AFTER show() for proper width
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                android.view.WindowManager.LayoutParams.MATCH_PARENT,
                android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        }
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
