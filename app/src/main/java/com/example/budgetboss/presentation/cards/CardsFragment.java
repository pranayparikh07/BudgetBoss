package com.example.budgetboss.presentation.cards;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.budgetboss.R;

public class CardsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cards, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View btnAdd = view.findViewById(R.id.btnAddCard);
        if (btnAdd != null) {
            btnAdd.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Add Card Feature Coming Soon", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
