package com.example.budgetboss.presentation.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.SharedPreferences;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetboss.databinding.FragmentProfileBinding;
import com.example.budgetboss.MainActivity;
import com.example.budgetboss.presentation.viewmodel.DashboardViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private DashboardViewModel viewModel;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "profile_prefs";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_USERNAME = "username_local";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        prefs = requireContext().getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE);

        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                // First check profile prefs for name
                String profileName = prefs.getString(KEY_FULL_NAME, null);
                String displayName = (profileName != null && !profileName.isEmpty()) 
                    ? profileName : user.getUsername();
                    
                binding.tvUsername.setText(displayName);
                binding.tvEmail.setText(user.getEmail());
                
                // Set avatar initials
                String initials = getInitials(displayName);
                binding.tvAvatarInitials.setText(initials);
                
                bindCollectedData(user);
            }
        });

        binding.btnLogout.setOnClickListener(v -> {
            viewModel.logout();
            // Restart the activity or navigate to login
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        binding.btnEditProfile.setOnClickListener(v -> showEditProfileDialog());
        binding.btnPrivacyPolicy
                .setOnClickListener(v -> new TermsPrivacyFragment().show(getParentFragmentManager(), "privacy"));
        binding.btnTerms.setOnClickListener(v -> new TermsPrivacyFragment().show(getParentFragmentManager(), "terms"));
        binding.btnDeleteAccount.setOnClickListener(v -> android.widget.Toast
                .makeText(requireContext(), "Account Deletion Requested", android.widget.Toast.LENGTH_SHORT).show());

        binding.btnSettings.setOnClickListener(v -> androidx.navigation.Navigation.findNavController(view)
                .navigate(com.example.budgetboss.R.id.action_profileFragment_to_settingsFragment));
    }

    private void showEditProfileDialog() {
        View dialogView = getLayoutInflater().inflate(com.example.budgetboss.R.layout.dialog_edit_profile, null);
        
        com.google.android.material.dialog.MaterialAlertDialogBuilder builder = 
            new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext(),
                com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog);
        builder.setView(dialogView);
        builder.setBackground(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));

        com.google.android.material.textfield.TextInputEditText etName = dialogView
                .findViewById(com.example.budgetboss.R.id.etName);
        com.google.android.material.textfield.TextInputEditText etMobile = dialogView
                .findViewById(com.example.budgetboss.R.id.etMobile);
        com.google.android.material.textfield.TextInputEditText etUsername = dialogView
                .findViewById(com.example.budgetboss.R.id.etUsername);
        com.google.android.material.textfield.TextInputLayout tilName = dialogView
                .findViewById(com.example.budgetboss.R.id.tilName);
        com.google.android.material.button.MaterialButton btnSave = dialogView.findViewById(com.example.budgetboss.R.id.btnSave);
        com.google.android.material.button.MaterialButton btnCancel = dialogView.findViewById(com.example.budgetboss.R.id.btnCancel);

        // Pre-fill from stored data
        etName.setText(prefs.getString(KEY_FULL_NAME, ""));
        etMobile.setText(prefs.getString(KEY_MOBILE, ""));
        etUsername.setText(prefs.getString(KEY_USERNAME, binding.tvUsername.getText().toString()));

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        
        // Allow scrolling when keyboard opens
        if (dialog.getWindow() != null) {
            dialog.getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        btnSave.setOnClickListener(v -> {
            String name = etName.getText() != null ? etName.getText().toString().trim() : "";
            String mobile = etMobile.getText() != null ? etMobile.getText().toString().trim() : "";
            String username = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";

            // Validation
            if (name.isEmpty()) {
                tilName.setError("Please enter your name");
                return;
            }
            tilName.setError(null);

            prefs.edit()
                .putString(KEY_FULL_NAME, name)
                .putString(KEY_MOBILE, mobile)
                .putString(KEY_USERNAME, username.isEmpty() ? binding.tvUsername.getText().toString() : username)
                .apply();

            // Update UI immediately
            binding.tvUsername.setText(name);
            binding.tvAvatarInitials.setText(getInitials(name));
            
            bindCollectedData(null);
            showSuccessMessage("Profile updated successfully");
            dialog.dismiss();
        });

        dialog.show();
    }
    
    private void showSuccessMessage(String message) {
        com.google.android.material.snackbar.Snackbar.make(binding.getRoot(), message, 
            com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
            .setBackgroundTint(requireContext().getResources().getColor(com.example.budgetboss.R.color.status_income, requireContext().getTheme()))
            .setTextColor(android.graphics.Color.WHITE)
            .show();
    }

        private void bindCollectedData(@Nullable com.example.budgetboss.domain.models.User user) {
        String fullName = prefs.getString(KEY_FULL_NAME, "Not set");
        String mobile = prefs.getString(KEY_MOBILE, "Not set");
        String usernameLocal = prefs.getString(KEY_USERNAME, user != null ? user.getUsername() : "Not set");
        String email = user != null ? user.getEmail() : binding.tvEmail.getText().toString();

        String collected = "Full name: " + fullName + "\n" +
            "Username: " + usernameLocal + "\n" +
            "Email: " + email + "\n" +
            "Mobile: " + mobile;
        binding.tvCollectedData.setText(collected);
        }

    private String getInitials(String name) {
        if (name == null || name.isEmpty()) {
            return "U";
        }
        String[] parts = name.trim().split("\\s+");
        if (parts.length >= 2) {
            return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase(Locale.getDefault());
        }
        return name.substring(0, Math.min(2, name.length())).toUpperCase(Locale.getDefault());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
