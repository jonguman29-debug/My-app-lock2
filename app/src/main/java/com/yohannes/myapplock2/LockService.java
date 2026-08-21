package com.yohannes.myapplock2;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.List;

public class LockService extends Service {

    private boolean running = true;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(() -> {

            while (running) {

                String currentApp = getCurrentApp();

                if (currentApp != null &&
                        currentApp.equals("com.android.settings")) {

                    // ለጊዜው ሙከራ ብቻ
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

        }).start();

        return START_STICKY;
    }

    private String getCurrentApp() {

        UsageStatsManager manager =
                (UsageStatsManager) getSystemService(
                        Context.USAGE_STATS_SERVICE
                );

        long end = System.currentTimeMillis();
        long start = end - 5000;

        List<UsageStats> stats =
                manager.queryUsageStats(
                        UsageStatsManager.INTERVAL_DAILY,
                        start,
                        end
                );

        if (stats == null || stats.isEmpty()) {
            return null;
        }

        UsageStats recent = null;

        for (UsageStats item : stats) {
            if (recent == null ||
                    item.getLastTimeUsed() > recent.getLastTimeUsed()) {
                recent = item;
            }
        }

        return recent != null
                ? recent.getPackageName()
                : null;
    }

    @Override
    public void onDestroy() {
        running = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
            }
