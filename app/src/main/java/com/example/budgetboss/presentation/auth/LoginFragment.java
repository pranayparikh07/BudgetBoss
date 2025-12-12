package com.example.budgetboss.presentation.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.budgetboss.R;
import com.example.budgetboss.databinding.FragmentLoginBinding;
import com.example.budgetboss.presentation.viewmodel.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AuthViewModel viewModel = new androidx.lifecycle.ViewModelProvider(this).get(AuthViewModel.class);
        NavController navController = Navigation.findNavController(view);

        viewModel.loginResult.observe(getViewLifecycleOwner(), resource -> {
            binding.btnLogin.setEnabled(true);
            if (resource != null) {
                if (resource.status == com.example.budgetboss.utils.Resource.Status.SUCCESS) {
                    if (navController.getCurrentDestination() != null
                            && navController.getCurrentDestination().getId() == R.id.loginFragment) {
                        navController.navigate(R.id.action_loginFragment_to_dashboardFragment);
                    }
                } else if (resource.status == com.example.budgetboss.utils.Resource.Status.ERROR) {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getEditText().getText().toString().trim();
            String password = binding.etPassword.getEditText().getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            binding.btnLogin.setEnabled(false);
            viewModel.login(email, password);
        });

        binding.tvRegisterLink.setOnClickListener(v -> {
            navController.navigate(R.id.action_loginFragment_to_registerFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
