package com.example.budgetboss.presentation.budget;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetboss.data.local.entity.BudgetGoalEntity;
import com.example.budgetboss.databinding.ItemBudgetBinding;

import java.util.ArrayList;
import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {

    private OnBudgetClickListener listener;
    private List<BudgetGoalEntity> budgets = new ArrayList<>();

    public interface OnBudgetClickListener {
        void onBudgetClick(BudgetGoalEntity budget);
    }

    public void setListener(OnBudgetClickListener listener) {
        this.listener = listener;
    }

    public void setBudgets(List<BudgetGoalEntity> budgets) {
        this.budgets = budgets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBudgetBinding binding = ItemBudgetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BudgetViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        holder.bind(budgets.get(position));
    }

    @Override
    public int getItemCount() {
        return budgets.size();
    }

    class BudgetViewHolder extends RecyclerView.ViewHolder {
        private final ItemBudgetBinding binding;

        public BudgetViewHolder(ItemBudgetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onBudgetClick(budgets.get(pos));
                }
            });
        }

        public void bind(BudgetGoalEntity budget) {
            binding.tvCategory.setText(budget.category);
            binding.tvLimit.setText("Limit: ₹" + budget.limitAmount);
            binding.tvPeriod.setText(budget.period);
            binding.pbBudget.setProgress(0);
        }
    }
}
