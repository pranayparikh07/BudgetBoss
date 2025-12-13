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

    private OnItemClickListener listener;
    private List<Transaction> transactions = new ArrayList<>();

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    public Transaction getItem(int position) {
        return transactions.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Transaction transaction);
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
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
            binding.tvDate.setText(
                    new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date(transaction.getDate())));

            // Set category text
            String category = transaction.getCategory();
            if (category != null && !category.isEmpty()) {
                binding.tvCategory.setText(category);
                binding.tvCategory.setVisibility(android.view.View.VISIBLE);
            } else {
                binding.tvCategory.setVisibility(android.view.View.GONE);
            }

            String amountPrefix = transaction.getType() == Transaction.TransactionType.CREDIT ? "+" : "-";
            binding.tvAmount.setText(amountPrefix + "₹" + String.format(Locale.getDefault(), "%.2f", transaction.getAmount()));

            // Set colors and icon based on transaction type
            android.content.Context ctx = itemView.getContext();
            if (transaction.getType() == Transaction.TransactionType.CREDIT) {
                binding.tvAmount.setTextColor(ctx.getResources().getColor(com.example.budgetboss.R.color.status_income, ctx.getTheme()));
                binding.iconContainer.setBackgroundResource(com.example.budgetboss.R.drawable.bg_stat_income);
                binding.ivCategoryIcon.setImageResource(com.example.budgetboss.R.drawable.ic_arrow_income);
                binding.ivCategoryIcon.setImageTintList(android.content.res.ColorStateList.valueOf(
                        ctx.getResources().getColor(com.example.budgetboss.R.color.status_income, ctx.getTheme())));
            } else {
                binding.tvAmount.setTextColor(ctx.getResources().getColor(com.example.budgetboss.R.color.status_expense, ctx.getTheme()));
                binding.iconContainer.setBackgroundResource(com.example.budgetboss.R.drawable.bg_stat_expense);
                binding.ivCategoryIcon.setImageResource(com.example.budgetboss.R.drawable.ic_arrow_expense);
                binding.ivCategoryIcon.setImageTintList(android.content.res.ColorStateList.valueOf(
                        ctx.getResources().getColor(com.example.budgetboss.R.color.status_expense, ctx.getTheme())));
            }

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemClick(transactions.get(getAdapterPosition()));
                }
            });
        }
    }
}
