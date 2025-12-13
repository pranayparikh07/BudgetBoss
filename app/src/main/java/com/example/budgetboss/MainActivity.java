package com.example.budgetboss;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.graphics.Rect;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import dagger.hilt.android.AndroidEntryPoint;
import com.example.budgetboss.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ViewTreeObserver.OnGlobalLayoutListener keyboardListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Keep content below status bar to avoid overlap with system UI
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        androidx.navigation.fragment.NavHostFragment navHostFragment = (androidx.navigation.fragment.NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        androidx.navigation.NavController navController = navHostFragment.getNavController();
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = binding.bottomNav;
        androidx.navigation.ui.NavigationUI.setupWithNavController(bottomNav, navController);

        // Hide bottom nav when keyboard is shown
        setupKeyboardListener();

        // Check for navigation intent
        final boolean[] pendingAddTransaction = { false };
        final boolean[] pendingLogin = { false };
        
        if (getIntent() != null) {
            String navigateTo = getIntent().getStringExtra("NAVIGATE_TO");
            if ("add_transaction".equals(navigateTo)) {
                pendingAddTransaction[0] = true;
            } else if ("login".equals(navigateTo)) {
                pendingLogin[0] = true;
            }
        }
        
        // Check Firebase auth status
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null && !pendingLogin[0]) {
            pendingLogin[0] = true;
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
    
    private void setupKeyboardListener() {
        final View rootView = binding.getRoot();
        keyboardListener = () -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getRootView().getHeight();
            int keypadHeight = screenHeight - r.bottom;
            
            // If keyboard is shown (keypad height > 15% of screen)
            if (keypadHeight > screenHeight * 0.15) {
                binding.bottomNav.setVisibility(View.GONE);
            } else {
                // Only show if we're on a screen that should show bottom nav
                androidx.navigation.fragment.NavHostFragment navHostFragment = 
                    (androidx.navigation.fragment.NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
                if (navHostFragment != null) {
                    int currentDestId = navHostFragment.getNavController().getCurrentDestination() != null 
                        ? navHostFragment.getNavController().getCurrentDestination().getId() : 0;
                    if (currentDestId == R.id.dashboardFragment ||
                        currentDestId == R.id.budgetFragment ||
                        currentDestId == R.id.vaultFragment ||
                        currentDestId == R.id.profileFragment ||
                        currentDestId == R.id.investmentsFragment) {
                        binding.bottomNav.setVisibility(View.VISIBLE);
                    }
                }
            }
        };
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(keyboardListener);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (keyboardListener != null) {
            binding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(keyboardListener);
        }
    }
}
