package com.example.budgetboss;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;

import com.example.budgetboss.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Hide Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Animations for logo
        if (binding.ivLogo != null) {
            ObjectAnimator fadeInLogo = ObjectAnimator.ofFloat(binding.ivLogo, "alpha", 0f, 1f);
            fadeInLogo.setDuration(800);
            
            ObjectAnimator scaleXLogo = ObjectAnimator.ofFloat(binding.ivLogo, "scaleX", 0.5f, 1f);
            ObjectAnimator scaleYLogo = ObjectAnimator.ofFloat(binding.ivLogo, "scaleY", 0.5f, 1f);
            scaleXLogo.setDuration(800);
            scaleYLogo.setDuration(800);
            
            AnimatorSet logoSet = new AnimatorSet();
            logoSet.playTogether(fadeInLogo, scaleXLogo, scaleYLogo);
            logoSet.setInterpolator(new AccelerateDecelerateInterpolator());
            logoSet.start();
        }

        // Animations for title
        ObjectAnimator fadeInTitle = ObjectAnimator.ofFloat(binding.tvAppName, "alpha", 0f, 1f);
        fadeInTitle.setDuration(1000);
        fadeInTitle.setStartDelay(400);

        ObjectAnimator scaleXTitle = ObjectAnimator.ofFloat(binding.tvAppName, "scaleX", 0.8f, 1f);
        ObjectAnimator scaleYTitle = ObjectAnimator.ofFloat(binding.tvAppName, "scaleY", 0.8f, 1f);
        scaleXTitle.setDuration(1000);
        scaleYTitle.setDuration(1000);
        scaleXTitle.setStartDelay(400);
        scaleYTitle.setStartDelay(400);

        ObjectAnimator fadeInTagline = ObjectAnimator.ofFloat(binding.tvTagline, "alpha", 0f, 1f);
        fadeInTagline.setDuration(800);
        fadeInTagline.setStartDelay(800);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(fadeInTitle, scaleXTitle, scaleYTitle, fadeInTagline);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();

        // Navigate after delay - check auth status
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            
            // Pass auth status to MainActivity so it knows where to navigate
            if (auth.getCurrentUser() == null) {
                intent.putExtra("NAVIGATE_TO", "login");
            }
            
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, 2500);
    }
}
