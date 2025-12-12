package com.example.budgetboss.presentation.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.tvUsername.setText(user.getUsername());
                binding.tvEmail.setText(user.getEmail());
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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(com.example.budgetboss.R.layout.fragment_profile_edit, null);
        builder.setView(dialogView);

        com.google.android.material.textfield.TextInputEditText etName = dialogView
                .findViewById(com.example.budgetboss.R.id.etName);
        com.google.android.material.textfield.TextInputEditText etMobile = dialogView
                .findViewById(com.example.budgetboss.R.id.etMobile);
        com.google.android.material.textfield.TextInputEditText etUsername = dialogView
                .findViewById(com.example.budgetboss.R.id.etUsername);
        android.widget.Button btnSave = dialogView.findViewById(com.example.budgetboss.R.id.btnSaveProfile);

        // Pre-fill if possible (User model update needed for mobile/name?)
        // Assuming current User only has username/email.

        android.app.AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            // Update logic here (Mock or VM)
            android.widget.Toast.makeText(requireContext(), "Profile Updated", android.widget.Toast.LENGTH_SHORT)
                    .show();
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
