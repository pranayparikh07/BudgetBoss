package com.example.budgetboss.presentation.intro;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.budgetboss.MainActivity;
import com.example.budgetboss.R;

public class IntroActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "BudgetBossPrefs";
    private static final String KEY_INTRO_SHOWN = "intro_shown";

    private TextView word1, word2, word3;
    private View chalkDust;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if intro has been shown before
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean introShown = prefs.getBoolean(KEY_INTRO_SHOWN, false);

        if (introShown) {
            // Skip intro and go directly to main
            navigateToMain();
            return;
        }

        // Make fullscreen immersive
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        
        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        controller.hide(WindowInsetsCompat.Type.systemBars());
        controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        setContentView(R.layout.activity_intro);

        // Initialize views
        word1 = findViewById(R.id.word1);
        word2 = findViewById(R.id.word2);
        word3 = findViewById(R.id.word3);
        chalkDust = findViewById(R.id.chalkDust);

        // Start animation sequence
        startChalkAnimation();
    }

    private void startChalkAnimation() {
        Handler handler = new Handler(Looper.getMainLooper());

        // Word 1: "TRACK" - appears with chalk writing effect
        handler.postDelayed(() -> animateWord(word1, () -> {
            
            // Word 2: "SAVE" - after word 1 completes
            handler.postDelayed(() -> animateWord(word2, () -> {
                
                // Word 3: "GROW" - after word 2 completes
                handler.postDelayed(() -> animateWord(word3, () -> {
                    
                    // All words shown, show dust effect and navigate
                    handler.postDelayed(() -> {
                        showChalkDustAndNavigate();
                    }, 800);
                    
                }), 400);
            }), 400);
        }), 500);
    }

    private void animateWord(TextView textView, Runnable onComplete) {
        // Chalk writing animation - scale up + fade in with slight shake
        textView.setScaleX(0.5f);
        textView.setScaleY(0.5f);
        textView.setRotation(-2f);

        // Fade in
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f);
        fadeIn.setDuration(400);

        // Scale up
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(textView, "scaleX", 0.5f, 1f);
        scaleX.setDuration(500);
        scaleX.setInterpolator(new OvershootInterpolator(1.5f));

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(textView, "scaleY", 0.5f, 1f);
        scaleY.setDuration(500);
        scaleY.setInterpolator(new OvershootInterpolator(1.5f));

        // Subtle rotation to simulate hand-drawn effect
        ObjectAnimator rotate = ObjectAnimator.ofFloat(textView, "rotation", -2f, 0f);
        rotate.setDuration(500);

        // Chalk scratch effect - slight horizontal shake
        ObjectAnimator shakeX = ObjectAnimator.ofFloat(textView, "translationX", 0f, -5f, 5f, -3f, 3f, 0f);
        shakeX.setDuration(300);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(fadeIn, scaleX, scaleY, rotate);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Play shake after main animation
                shakeX.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (onComplete != null) {
                            onComplete.run();
                        }
                    }
                });
                shakeX.start();
            }
        });
        animatorSet.start();
    }

    private void showChalkDustAndNavigate() {
        // Chalk dust fade in effect
        ObjectAnimator dustFadeIn = ObjectAnimator.ofFloat(chalkDust, "alpha", 0f, 0.3f);
        dustFadeIn.setDuration(500);

        // All words glow/pulse effect
        AnimatorSet glowSet = new AnimatorSet();
        
        ObjectAnimator glow1 = createGlowAnimation(word1);
        ObjectAnimator glow2 = createGlowAnimation(word2);
        ObjectAnimator glow3 = createGlowAnimation(word3);
        
        glowSet.playTogether(glow1, glow2, glow3);

        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playTogether(dustFadeIn, glowSet);
        finalSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Fade out everything and navigate
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    fadeOutAndNavigate();
                }, 600);
            }
        });
        finalSet.start();
    }

    private ObjectAnimator createGlowAnimation(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.05f, 1f);
        scaleX.setDuration(400);
        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        return scaleX;
    }

    private void fadeOutAndNavigate() {
        View rootView = findViewById(android.R.id.content);
        
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(rootView, "alpha", 1f, 0f);
        fadeOut.setDuration(500);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Mark intro as shown
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                prefs.edit().putBoolean(KEY_INTRO_SHOWN, true).apply();
                
                // Navigate to main
                navigateToMain();
            }
        });
        fadeOut.start();
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        // No animation for seamless transition
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        // Disable back button during intro
    }
}
