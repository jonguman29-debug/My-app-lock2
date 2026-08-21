package com.yohannes.myapplock2;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.accessibility.AccessibilityEvent;

public class AppLockAccessibilityService extends AccessibilityService {

    private SharedPreferences prefs;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        prefs = getSharedPreferences(
                "MyAppLock",
                MODE_PRIVATE
        );
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event == null) {
            return;
        }

        if (event.getEventType()
                != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return;
        }

        CharSequence packageName = event.getPackageName();

        if (packageName == null) {
            return;
        }

        String currentPackage = packageName.toString();

        // Don't lock MyAppLock2 itself
        if (currentPackage.equals(getPackageName())) {
            return;
        }

        if (prefs == null) {
            prefs = getSharedPreferences(
                    "MyAppLock",
                    MODE_PRIVATE
            );
        }

        boolean locked = prefs.getBoolean(
                "LOCK_" + currentPackage,
                false
        );

        if (!locked) {
            return;
        }

        Intent intent = new Intent(
                this,
                PinActivity.class
        );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP
        );

        intent.putExtra(
                "LOCKED_PACKAGE",
                currentPackage
        );

        startActivity(intent);
    }

    @Override
    public void onInterrupt() {
    }
}
