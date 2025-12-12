package com.example.budgetboss.presentation.investments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        viewModel.getInvestments().observe(getViewLifecycleOwner(), adapter::setInvestments);

        binding.fabAddInvestment.setOnClickListener(v -> showAddInvestmentDialog(null));
    }

    private void showInvestmentOptions(Investment investment) {
        String[] options = { "Edit", "Delete" };
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Investment Options")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showAddInvestmentDialog(investment);
                    } else if (which == 1) {
                        new android.app.AlertDialog.Builder(requireContext())
                                .setTitle("Delete Investment")
                                .setMessage("Are you sure?")
                                .setPositiveButton("Delete", (d, w) -> viewModel.deleteInvestment(investment))
                                .setNegativeButton("Cancel", null)
                                .show();
                    }
                })
                .show();
    }

    private void showAddInvestmentDialog(@Nullable Investment investmentToEdit) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(com.example.budgetboss.R.layout.fragment_add_investment, null);
        builder.setView(dialogView);

        TextView tvTitle = dialogView.findViewById(com.example.budgetboss.R.id.tvTitle);
        tvTitle.setText(investmentToEdit == null ? "Add Investment" : "Edit Investment");

        android.widget.Spinner spType = dialogView.findViewById(com.example.budgetboss.R.id.spType);
        com.google.android.material.textfield.TextInputEditText etName = dialogView
                .findViewById(com.example.budgetboss.R.id.etName);
        com.google.android.material.textfield.TextInputEditText etAmount = dialogView
                .findViewById(com.example.budgetboss.R.id.etAmount);
        com.google.android.material.textfield.TextInputEditText etRate = dialogView
                .findViewById(com.example.budgetboss.R.id.etRate);
        com.google.android.material.textfield.TextInputEditText etDuration = dialogView
                .findViewById(com.example.budgetboss.R.id.etDuration);
        android.widget.TextView tvReturns = dialogView.findViewById(com.example.budgetboss.R.id.tvCalculatedReturn);
        android.widget.Button btnSave = dialogView.findViewById(com.example.budgetboss.R.id.btnSave);
        android.view.View layoutDetails = dialogView.findViewById(com.example.budgetboss.R.id.layoutDetails);

        android.widget.ArrayAdapter<Investment.InvestmentType> typeAdapter = new android.widget.ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_dropdown_item, Investment.InvestmentType.values());
        spType.setAdapter(typeAdapter);

        if (investmentToEdit != null) {
            etName.setText(investmentToEdit.getName());
            etAmount.setText(String.valueOf(investmentToEdit.getAmount()));
            etRate.setText(String.valueOf(investmentToEdit.getInterestRate()));
            // Start Date as duration? Logic is fuzzy in original code. Assuming duration is
            // stored somewhere or calculated.
            // In original add logic, duration wasn't stored in Entity! Just used for
            // calculation.
            // I should just leave duration empty or try to infer.
            // Investment Entity doesn't have Duration?
            // Checking Investment.java... It has StartDate. No duration.
            // So I can't pre-fill Duration. User re-enters it for calculation.
            spType.setSelection(investmentToEdit.getType().ordinal());
        }

        spType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                Investment.InvestmentType type = (Investment.InvestmentType) parent.getItemAtPosition(position);
                if (type == Investment.InvestmentType.SIP || type == Investment.InvestmentType.LOAN_GIVEN
                        || type == Investment.InvestmentType.LOAN_TAKEN) {
                    layoutDetails.setVisibility(View.VISIBLE);
                } else {
                    layoutDetails.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        // Calculation Logic
        android.text.TextWatcher calculator = new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

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
                        Investment.InvestmentType type = (Investment.InvestmentType) spType.getSelectedItem();
                        double result = 0;

                        if (type == Investment.InvestmentType.LOAN_GIVEN
                                || type == Investment.InvestmentType.LOAN_TAKEN) {
                            result = (p * r * n) / 100;
                            tvReturns.setText("Total Interest: ₹" + String.format("%.2f", result));
                        } else if (type == Investment.InvestmentType.SIP) {
                            double i = (r / 100) / 12;
                            if (i > 0) {
                                double fv = p * ((Math.pow(1 + i, n) - 1) / i) * (1 + i);
                                tvReturns.setText("Total Value: ₹" + String.format("%.2f", fv));
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        };

        etAmount.addTextChangedListener(calculator);
        etRate.addTextChangedListener(calculator);
        etDuration.addTextChangedListener(calculator);

        android.app.AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String amountStr = etAmount.getText().toString();
            Investment.InvestmentType type = (Investment.InvestmentType) spType.getSelectedItem();

            if (!name.isEmpty() && !amountStr.isEmpty()) {
                double amount = Double.parseDouble(amountStr);
                double rate = etRate.getText().toString().isEmpty() ? 0
                        : Double.parseDouble(etRate.getText().toString());

                if (investmentToEdit != null) {
                    investmentToEdit.setName(name);
                    investmentToEdit.setAmount(amount);
                    investmentToEdit.setType(type);
                    investmentToEdit.setInterestRate(rate);
                    viewModel.updateInvestment(investmentToEdit);
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
                }
                dialog.dismiss();
            }
        });

        dialog.show();
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
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2,
                    parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Investment item = investments.get(position);
            holder.text1.setText(item.getName() + " (" + item.getType() + ")");
            holder.text2.setText("Amount: ₹" + item.getAmount());
        }

        @Override
        public int getItemCount() {
            return investments.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView text1, text2;

            ViewHolder(View v) {
                super(v);
                text1 = v.findViewById(android.R.id.text1);
                text2 = v.findViewById(android.R.id.text2);
                v.setOnClickListener(view -> {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        showInvestmentOptions(investments.get(pos));
                    }
                });
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
