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

        // Animations
        ObjectAnimator fadeInTitle = ObjectAnimator.ofFloat(binding.tvAppName, "alpha", 0f, 1f);
        fadeInTitle.setDuration(1500);

        ObjectAnimator scaleXTitle = ObjectAnimator.ofFloat(binding.tvAppName, "scaleX", 0.8f, 1f);
        ObjectAnimator scaleYTitle = ObjectAnimator.ofFloat(binding.tvAppName, "scaleY", 0.8f, 1f);
        scaleXTitle.setDuration(1500);
        scaleYTitle.setDuration(1500);

        ObjectAnimator fadeInTagline = ObjectAnimator.ofFloat(binding.tvTagline, "alpha", 0f, 1f);
        fadeInTagline.setDuration(1000);
        fadeInTagline.setStartDelay(1000); // Start after title begins

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fadeInTitle).with(scaleXTitle).with(scaleYTitle).with(fadeInTagline);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();

        // Navigate to MainActivity after delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 3000); // 3 seconds
    }
}
