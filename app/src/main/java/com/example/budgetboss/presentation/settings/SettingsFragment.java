package com.example.budgetboss.presentation.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.budgetboss.R;
import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsFragment extends Fragment {

    private static final String PREFS_NAME = "app_settings";
    private static final String KEY_DARK_MODE = "dark_mode";
    private SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        prefs = requireContext().getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE);

        view.findViewById(R.id.btnEditProfile).setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_profileFragment);
        });

        view.findViewById(R.id.btnChangePassword).setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Change Password Clicked", Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.btnHelp).setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_helpSupportFragment);
        });

        // Theme Toggle
        MaterialSwitch switchDarkMode = view.findViewById(R.id.switchDarkMode);
        boolean isDarkMode = prefs.getBoolean(KEY_DARK_MODE, true);
        switchDarkMode.setChecked(isDarkMode);
        
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_DARK_MODE, isChecked).apply();
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
        
        // About BudgetBoss
        view.findViewById(R.id.btnAbout).setOnClickListener(v -> showAboutDialog());
    }
    
    private void showAboutDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_about, null);
        
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext(),
            android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        builder.setView(dialogView);
        android.app.AlertDialog dialog = builder.create();
        
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setDimAmount(0.7f);
        }
        
        dialogView.findViewById(R.id.btnClose).setOnClickListener(v -> dialog.dismiss());
        
        dialog.show();
    }
}
