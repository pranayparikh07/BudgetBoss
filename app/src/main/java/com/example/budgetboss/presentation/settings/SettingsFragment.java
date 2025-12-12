package com.example.budgetboss.presentation.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.budgetboss.R;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnEditProfile).setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_profileFragment);
        });

        view.findViewById(R.id.btnChangePassword).setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Change Password Clicked", Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.btnHelp).setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_helpSupportFragment);
        });

        view.findViewById(R.id.switchDarkMode).setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Theme Toggle Clicked", Toast.LENGTH_SHORT).show();
        });
    }
}
