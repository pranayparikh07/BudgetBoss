package com.example.budgetboss.presentation.dashboard;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetboss.databinding.ItemTransactionBinding;
import com.example.budgetboss.domain.models.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactions = new ArrayList<>();

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TransactionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.bind(transaction);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {
        private final ItemTransactionBinding binding;

        public TransactionViewHolder(ItemTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Transaction transaction) {
            binding.tvTitle.setText(transaction.getTitle());
            binding.tvDate.setText(new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date(transaction.getDate())));
            
            String amountPrefix = transaction.getType() == Transaction.TransactionType.CREDIT ? "+" : "-";
            binding.tvAmount.setText(amountPrefix + "$" + String.format("%.2f", transaction.getAmount()));
            
            if (transaction.getType() == Transaction.TransactionType.CREDIT) {
                binding.tvAmount.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
            } else {
                binding.tvAmount.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
            }
        }
    }
}
