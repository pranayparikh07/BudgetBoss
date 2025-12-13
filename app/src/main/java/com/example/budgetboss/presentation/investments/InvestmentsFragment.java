package com.example.budgetboss.presentation.investments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetboss.databinding.FragmentInvestmentsBinding;
import com.example.budgetboss.presentation.viewmodel.InvestmentViewModel;
import com.example.budgetboss.domain.models.Investment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class InvestmentsFragment extends Fragment {

    private FragmentInvestmentsBinding binding;
    private InvestmentViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentInvestmentsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(InvestmentViewModel.class);

        InvestmentAdapter adapter = new InvestmentAdapter();
        binding.rvInvestments.setAdapter(adapter);

        viewModel.getInvestments().observe(getViewLifecycleOwner(), investments -> {
            adapter.setInvestments(investments);
            // Toggle empty state - FAB always visible
            if (investments == null || investments.isEmpty()) {
                binding.layoutEmpty.setVisibility(View.VISIBLE);
                binding.rvInvestments.setVisibility(View.GONE);
            } else {
                binding.layoutEmpty.setVisibility(View.GONE);
                binding.rvInvestments.setVisibility(View.VISIBLE);
            }
        });

        binding.fabAddInvestment.setOnClickListener(v -> showAddInvestmentDialog(null));
        binding.btnAddFirstInvestment.setOnClickListener(v -> showAddInvestmentDialog(null));
    }

    private void showInvestmentOptions(Investment investment) {
        View dialogView = getLayoutInflater().inflate(com.example.budgetboss.R.layout.dialog_investment_options, null);
        
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext(),
            android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        builder.setView(dialogView);
        android.app.AlertDialog dialog = builder.create();
        
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setDimAmount(0.7f);
        }
        
        // Set investment info
        android.widget.TextView tvName = dialogView.findViewById(com.example.budgetboss.R.id.tvInvestmentName);
        android.widget.TextView tvAmount = dialogView.findViewById(com.example.budgetboss.R.id.tvInvestmentAmount);
        tvName.setText(investment.getName());
        tvAmount.setText(String.format(Locale.getDefault(), "₹%.0f • %s", investment.getAmount(), investment.getType().toString()));
        
        dialogView.findViewById(com.example.budgetboss.R.id.btnEdit).setOnClickListener(v -> {
            dialog.dismiss();
            showAddInvestmentDialog(investment);
        });
        
        dialogView.findViewById(com.example.budgetboss.R.id.btnDelete).setOnClickListener(v -> {
            dialog.dismiss();
            showDeleteConfirmDialog(investment);
        });
        
        dialogView.findViewById(com.example.budgetboss.R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
        
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                android.view.WindowManager.LayoutParams.MATCH_PARENT,
                android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }
    
    private void showDeleteConfirmDialog(Investment investment) {
        View dialogView = getLayoutInflater().inflate(com.example.budgetboss.R.layout.dialog_confirm_delete, null);
        
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext(),
            android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        builder.setView(dialogView);
        android.app.AlertDialog dialog = builder.create();
        
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setDimAmount(0.7f);
        }
        
        android.widget.TextView tvMessage = dialogView.findViewById(com.example.budgetboss.R.id.tvMessage);
        tvMessage.setText("Are you sure you want to delete \"" + investment.getName() + "\"? This action cannot be undone.");
        
        dialogView.findViewById(com.example.budgetboss.R.id.btnDelete).setOnClickListener(v -> {
            viewModel.deleteInvestment(investment);
            dialog.dismiss();
            showSuccessMessage("Investment deleted");
        });
        
        dialogView.findViewById(com.example.budgetboss.R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
        
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                android.view.WindowManager.LayoutParams.MATCH_PARENT,
                android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    private void showAddInvestmentDialog(@Nullable Investment investmentToEdit) {
        View dialogView = getLayoutInflater().inflate(com.example.budgetboss.R.layout.dialog_add_investment, null);
        
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext(),
            android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        builder.setView(dialogView);
        android.app.AlertDialog dialog = builder.create();
        
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setDimAmount(0.7f);
        }

        // Get views
        android.widget.ImageView ivIcon = dialogView.findViewById(com.example.budgetboss.R.id.ivIcon);
        TextView tvTitle = dialogView.findViewById(com.example.budgetboss.R.id.tvDialogTitle);
        com.google.android.material.chip.ChipGroup chipGroup = dialogView.findViewById(com.example.budgetboss.R.id.chipGroupType);
        com.google.android.material.chip.Chip chipSIP = dialogView.findViewById(com.example.budgetboss.R.id.chipSIP);
        com.google.android.material.chip.Chip chipStock = dialogView.findViewById(com.example.budgetboss.R.id.chipStock);
        com.google.android.material.chip.Chip chipMutualFund = dialogView.findViewById(com.example.budgetboss.R.id.chipMutualFund);
        com.google.android.material.chip.Chip chipLoanGiven = dialogView.findViewById(com.example.budgetboss.R.id.chipLoanGiven);
        com.google.android.material.chip.Chip chipLoanTaken = dialogView.findViewById(com.example.budgetboss.R.id.chipLoanTaken);
        
        com.google.android.material.textfield.TextInputLayout tilName = dialogView.findViewById(com.example.budgetboss.R.id.tilName);
        com.google.android.material.textfield.TextInputLayout tilAmount = dialogView.findViewById(com.example.budgetboss.R.id.tilAmount);
        com.google.android.material.textfield.TextInputLayout tilRate = dialogView.findViewById(com.example.budgetboss.R.id.tilRate);
        com.google.android.material.textfield.TextInputLayout tilDuration = dialogView.findViewById(com.example.budgetboss.R.id.tilDuration);
        
        com.google.android.material.textfield.TextInputEditText etName = dialogView.findViewById(com.example.budgetboss.R.id.etName);
        com.google.android.material.textfield.TextInputEditText etAmount = dialogView.findViewById(com.example.budgetboss.R.id.etAmount);
        com.google.android.material.textfield.TextInputEditText etRate = dialogView.findViewById(com.example.budgetboss.R.id.etRate);
        com.google.android.material.textfield.TextInputEditText etDuration = dialogView.findViewById(com.example.budgetboss.R.id.etDuration);
        
        com.google.android.material.card.MaterialCardView cardReturns = dialogView.findViewById(com.example.budgetboss.R.id.cardReturns);
        TextView tvCalculatedReturn = dialogView.findViewById(com.example.budgetboss.R.id.tvCalculatedReturn);
        
        com.google.android.material.button.MaterialButton btnCancel = dialogView.findViewById(com.example.budgetboss.R.id.btnCancel);
        com.google.android.material.button.MaterialButton btnSave = dialogView.findViewById(com.example.budgetboss.R.id.btnSave);

        // Track selected type
        final Investment.InvestmentType[] selectedType = {Investment.InvestmentType.SIP};
        
        // Set title based on mode
        tvTitle.setText(investmentToEdit == null ? "Add Investment" : "Edit Investment");

        // Update UI based on type selection
        Runnable updateUIForType = () -> {
            Investment.InvestmentType type = selectedType[0];
            boolean isLoan = type == Investment.InvestmentType.LOAN_GIVEN || type == Investment.InvestmentType.LOAN_TAKEN;
            boolean needsDetails = type == Investment.InvestmentType.SIP || isLoan;
            
            tilRate.setVisibility(needsDetails ? View.VISIBLE : View.GONE);
            tilDuration.setVisibility(needsDetails ? View.VISIBLE : View.GONE);
            cardReturns.setVisibility(needsDetails ? View.VISIBLE : View.GONE);
            
            if (isLoan) {
                ivIcon.setImageResource(com.example.budgetboss.R.drawable.ic_account_balance);
                tilAmount.setHint(type == Investment.InvestmentType.LOAN_GIVEN ? "Principal Amount Given" : "Principal Amount Borrowed");
                tilRate.setHint("Interest Rate (% per year)");
                tilDuration.setHint("Duration (Years)");
            } else if (type == Investment.InvestmentType.SIP) {
                ivIcon.setImageResource(com.example.budgetboss.R.drawable.ic_investment);
                tilAmount.setHint("Monthly Investment");
                tilRate.setHint("Expected Return (% per year)");
                tilDuration.setHint("Duration (Months)");
            } else {
                ivIcon.setImageResource(com.example.budgetboss.R.drawable.ic_investment);
                tilAmount.setHint("Investment Amount");
            }
        };

        // Chip selection listener
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int checkedId = checkedIds.get(0);
            
            if (checkedId == com.example.budgetboss.R.id.chipSIP) {
                selectedType[0] = Investment.InvestmentType.SIP;
            } else if (checkedId == com.example.budgetboss.R.id.chipStock) {
                selectedType[0] = Investment.InvestmentType.STOCK;
            } else if (checkedId == com.example.budgetboss.R.id.chipMutualFund) {
                selectedType[0] = Investment.InvestmentType.MUTUAL_FUND;
            } else if (checkedId == com.example.budgetboss.R.id.chipLoanGiven) {
                selectedType[0] = Investment.InvestmentType.LOAN_GIVEN;
            } else if (checkedId == com.example.budgetboss.R.id.chipLoanTaken) {
                selectedType[0] = Investment.InvestmentType.LOAN_TAKEN;
            }
            updateUIForType.run();
        });

        // Pre-fill if editing
        if (investmentToEdit != null) {
            etName.setText(investmentToEdit.getName());
            etAmount.setText(String.valueOf(investmentToEdit.getAmount()));
            etRate.setText(String.valueOf(investmentToEdit.getInterestRate()));
            selectedType[0] = investmentToEdit.getType();
            
            // Select correct chip
            switch (investmentToEdit.getType()) {
                case SIP: chipSIP.setChecked(true); break;
                case STOCK: chipStock.setChecked(true); break;
                case MUTUAL_FUND: chipMutualFund.setChecked(true); break;
                case LOAN_GIVEN: chipLoanGiven.setChecked(true); break;
                case LOAN_TAKEN: chipLoanTaken.setChecked(true); break;
            }
        }
        
        updateUIForType.run();

        // Calculation Logic
        android.text.TextWatcher calculator = new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(android.text.Editable s) {
                try {
                    String amountStr = etAmount.getText().toString();
                    String rateStr = etRate.getText().toString();
                    String durStr = etDuration.getText().toString();

                    if (!amountStr.isEmpty() && !rateStr.isEmpty() && !durStr.isEmpty()) {
                        double p = Double.parseDouble(amountStr);
                        double r = Double.parseDouble(rateStr);
                        double n = Double.parseDouble(durStr);
                        Investment.InvestmentType type = selectedType[0];

                        if (type == Investment.InvestmentType.LOAN_GIVEN || type == Investment.InvestmentType.LOAN_TAKEN) {
                            double interest = (p * r * n) / 100;
                            tvCalculatedReturn.setText(String.format(Locale.getDefault(), "₹%.2f", interest));
                        } else if (type == Investment.InvestmentType.SIP) {
                            double i = (r / 100) / 12;
                            if (i > 0) {
                                double fv = p * ((Math.pow(1 + i, n) - 1) / i) * (1 + i);
                                tvCalculatedReturn.setText(String.format(Locale.getDefault(), "₹%.2f", fv));
                            }
                        }
                    } else {
                        tvCalculatedReturn.setText("₹0.00");
                    }
                } catch (Exception e) {
                    tvCalculatedReturn.setText("₹0.00");
                }
            }
        };

        etAmount.addTextChangedListener(calculator);
        etRate.addTextChangedListener(calculator);
        etDuration.addTextChangedListener(calculator);

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String amountStr = etAmount.getText().toString().trim();
            Investment.InvestmentType type = selectedType[0];

            if (name.isEmpty()) {
                tilName.setError("Enter name");
                return;
            }
            if (amountStr.isEmpty()) {
                tilAmount.setError("Enter amount");
                return;
            }

            double amount = Double.parseDouble(amountStr);
            double rate = etRate.getText().toString().isEmpty() ? 0 : Double.parseDouble(etRate.getText().toString());

            if (investmentToEdit != null) {
                investmentToEdit.setName(name);
                investmentToEdit.setAmount(amount);
                investmentToEdit.setType(type);
                investmentToEdit.setInterestRate(rate);
                viewModel.updateInvestment(investmentToEdit);
                showSuccessMessage("Investment updated");
            } else {
                Investment investment = new Investment(
                        java.util.UUID.randomUUID().toString(),
                        name,
                        amount,
                        type,
                        rate,
                        System.currentTimeMillis(),
                        "");
                viewModel.addInvestment(investment);
                showSuccessMessage("Investment added");
            }
            dialog.dismiss();
        });

        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                android.view.WindowManager.LayoutParams.MATCH_PARENT,
                android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    class InvestmentAdapter extends RecyclerView.Adapter<InvestmentAdapter.ViewHolder> {
        private List<Investment> investments = new ArrayList<>();

        void setInvestments(List<Investment> list) {
            this.investments = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(com.example.budgetboss.R.layout.item_investment, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Investment item = investments.get(position);
            holder.tvName.setText(item.getName());
            holder.tvType.setText(item.getType().toString());
            holder.tvAmount.setText(String.format(Locale.getDefault(), "₹%.0f", item.getAmount()));
            
            if (item.getInterestRate() > 0) {
                holder.tvRate.setText(String.format(Locale.getDefault(), "%.1f%% p.a.", item.getInterestRate()));
                holder.tvRate.setVisibility(View.VISIBLE);
            } else {
                holder.tvRate.setVisibility(View.GONE);
            }
            
            // Set icon based on type
            switch (item.getType()) {
                case LOAN_GIVEN:
                    holder.iconContainer.setBackgroundResource(com.example.budgetboss.R.drawable.bg_stat_income);
                    holder.ivIcon.setImageResource(com.example.budgetboss.R.drawable.ic_loan);
                    holder.ivIcon.setImageTintList(android.content.res.ColorStateList.valueOf(
                        requireContext().getResources().getColor(com.example.budgetboss.R.color.status_income, requireContext().getTheme())));
                    holder.tvAmount.setTextColor(requireContext().getResources().getColor(com.example.budgetboss.R.color.status_income, requireContext().getTheme()));
                    break;
                case LOAN_TAKEN:
                    holder.iconContainer.setBackgroundResource(com.example.budgetboss.R.drawable.bg_stat_expense);
                    holder.ivIcon.setImageResource(com.example.budgetboss.R.drawable.ic_loan);
                    holder.ivIcon.setImageTintList(android.content.res.ColorStateList.valueOf(
                        requireContext().getResources().getColor(com.example.budgetboss.R.color.status_expense, requireContext().getTheme())));
                    holder.tvAmount.setTextColor(requireContext().getResources().getColor(com.example.budgetboss.R.color.status_expense, requireContext().getTheme()));
                    break;
                default:
                    holder.iconContainer.setBackgroundResource(com.example.budgetboss.R.drawable.bg_stat_savings);
                    holder.ivIcon.setImageResource(com.example.budgetboss.R.drawable.ic_investments);
                    holder.ivIcon.setImageTintList(android.content.res.ColorStateList.valueOf(
                        requireContext().getResources().getColor(com.example.budgetboss.R.color.status_savings, requireContext().getTheme())));
                    holder.tvAmount.setTextColor(requireContext().getResources().getColor(com.example.budgetboss.R.color.status_savings, requireContext().getTheme()));
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return investments.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvType, tvAmount, tvRate;
            FrameLayout iconContainer;
            android.widget.ImageView ivIcon;

            ViewHolder(View v) {
                super(v);
                tvName = v.findViewById(com.example.budgetboss.R.id.tvName);
                tvType = v.findViewById(com.example.budgetboss.R.id.tvType);
                tvAmount = v.findViewById(com.example.budgetboss.R.id.tvAmount);
                tvRate = v.findViewById(com.example.budgetboss.R.id.tvRate);
                iconContainer = v.findViewById(com.example.budgetboss.R.id.iconContainer);
                ivIcon = v.findViewById(com.example.budgetboss.R.id.ivIcon);
                v.setOnClickListener(view -> {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        showInvestmentOptions(investments.get(pos));
                    }
                });
            }
        }
    }
    
    private void showSuccessMessage(String message) {
        if (getView() != null) {
            com.google.android.material.snackbar.Snackbar.make(getView(), message, 
                com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
                .setBackgroundTint(getResources().getColor(com.example.budgetboss.R.color.income, null))
                .setTextColor(getResources().getColor(android.R.color.white, null))
                .show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
