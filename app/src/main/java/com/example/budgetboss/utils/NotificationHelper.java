package com.example.budgetboss.utils;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.budgetboss.MainActivity;
import com.example.budgetboss.R;

public final class NotificationHelper {
    public static final String CHANNEL_ID_TRANSACTIONS = "budgetboss.transactions";
    private static final int REQUEST_CODE = 2010;

    private NotificationHelper() {}

    public static void createChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID_TRANSACTIONS,
                    "BudgetBoss Alerts",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Spending, budgets, vault and reminders");
            channel.enableLights(true);
            channel.setLightColor(Color.CYAN);
            channel.enableVibration(true);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static void showTransactionNotification(Context context, String title, String message) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_TRANSACTIONS)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        boolean hasPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                        == android.content.pm.PackageManager.PERMISSION_GRANTED;

        if (!hasPermission || !NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            return; // Respect runtime notification permission/setting
        }

        try {
            NotificationManagerCompat.from(context)
                    .notify((int) System.currentTimeMillis(), builder.build());
        } catch (SecurityException ignored) {
            // Permission could still be revoked; fail quietly
        }
    }
}
