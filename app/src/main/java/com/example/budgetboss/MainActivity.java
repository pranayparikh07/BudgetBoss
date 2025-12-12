package com.example.budgetboss;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import dagger.hilt.android.AndroidEntryPoint;
import com.example.budgetboss.databinding.ActivityMainBinding;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        androidx.navigation.fragment.NavHostFragment navHostFragment = (androidx.navigation.fragment.NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        androidx.navigation.NavController navController = navHostFragment.getNavController();
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = binding.bottomNav;
        androidx.navigation.ui.NavigationUI.setupWithNavController(bottomNav, navController);

        // Check for widget deep link
        final boolean[] pendingAddTransaction = { false };
        if (getIntent() != null && "add_transaction".equals(getIntent().getStringExtra("NAVIGATE_TO"))) {
            pendingAddTransaction[0] = true;
        }

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.dashboardFragment) {
                bottomNav.setVisibility(android.view.View.VISIBLE);

                // Handle Pending Navigation
                if (pendingAddTransaction[0]) {
                    pendingAddTransaction[0] = false;
                    // Use a handler to ensure layout is ready or just navigate
                    bottomNav.post(() -> controller.navigate(R.id.action_dashboardFragment_to_addTransactionFragment));
                }
            } else if (destination.getId() == R.id.budgetFragment ||
                    destination.getId() == R.id.vaultFragment ||
                    destination.getId() == R.id.profileFragment ||
                    destination.getId() == R.id.investmentsFragment) {
                bottomNav.setVisibility(android.view.View.VISIBLE);
            } else {
                bottomNav.setVisibility(android.view.View.GONE);
            }
        });
    }
}
