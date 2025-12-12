package com.example.budgetboss.presentation.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetboss.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.rvNotifications);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<String> mockNotifications = new ArrayList<>();
        mockNotifications.add("You exceeded your Food budget by ₹500.");
        mockNotifications.add("SIP Investment due tomorrow.");
        mockNotifications.add("Welcome to BudgetBoss Premium!");
        mockNotifications.add("New feature: AI Insights available.");

        NotificationAdapter adapter = new NotificationAdapter(mockNotifications);
        rv.setAdapter(adapter);

        View btnClearAll = view.findViewById(R.id.btnClearAll);
        if (btnClearAll != null) {
            btnClearAll.setOnClickListener(v -> {
                mockNotifications.clear();
                adapter.notifyDataSetChanged();
                android.widget.Toast
                        .makeText(requireContext(), "Notifications Cleared", android.widget.Toast.LENGTH_SHORT).show();
            });
        }
    }

    class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
        private final List<String> items;

        NotificationAdapter(List<String> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView tv = new TextView(parent.getContext());
            tv.setPadding(32, 24, 32, 24);
            tv.setTextSize(16);
            tv.setTextColor(android.graphics.Color.WHITE); // Assuming dark theme
            tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new ViewHolder(tv);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ((TextView) holder.itemView).setText(items.get(position));
            // Add separator or background if needed
            holder.itemView.setBackgroundResource(android.R.drawable.list_selector_background);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ViewHolder(View v) {
                super(v);
            }
        }
    }
}
