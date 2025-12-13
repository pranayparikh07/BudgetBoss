package com.example.budgetboss;

import android.app.Application;
import dagger.hilt.android.HiltAndroidApp;
import com.example.budgetboss.utils.NotificationHelper;

@HiltAndroidApp
public class BudgetBossApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// Register notification channels at startup so notifications can be delivered immediately
		NotificationHelper.createChannels(this);
	}
}
