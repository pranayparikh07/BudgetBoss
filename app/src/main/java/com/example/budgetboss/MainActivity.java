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

        androidx.navigation.fragment.NavHostFragment navHostFragment = (androidx.navigation.fragment.NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        androidx.navigation.NavController navController = navHostFragment.getNavController();
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = binding.bottomNav;
        androidx.navigation.ui.NavigationUI.setupWithNavController(bottomNav, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.dashboardFragment || 
                destination.getId() == R.id.budgetFragment || 
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
