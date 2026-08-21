package com.yohannes.myapplock2;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.Settings;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends Activity {

    private LinearLayout appList;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("MyAppLock", MODE_PRIVATE);

        if (!prefs.contains("PIN")) {
            showCreatePin();
        } else {
            showMainScreen();
        }
    }

    private void showCreatePin() {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(40, 40, 40, 40);
        layout.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);
        title.setText("🔐 MyAppLock2");
        title.setTextSize(30);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);

        TextView message = new TextView(this);
        message.setText("Create your 4-digit PIN");
        message.setTextSize(20);
        message.setTextColor(Color.DKGRAY);
        message.setGravity(Gravity.CENTER);

        EditText pin = new EditText(this);
        pin.setHint("4-digit PIN");
        pin.setInputType(2);
        pin.setGravity(Gravity.CENTER);
        pin.setTextSize(20);

        Button save = new Button(this);
        save.setText("Save PIN");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value = pin.getText().toString();

                if (value.length() != 4) {
                    Toast.makeText(
                            MainActivity.this,
                            "PIN must be 4 digits",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                prefs.edit()
                        .putString("PIN", value)
                        .apply();

                showMainScreen();
            }
        });

        layout.addView(title);
        layout.addView(message);

        LinearLayout.LayoutParams pinParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        pinParams.topMargin = 30;
        layout.addView(pin, pinParams);

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        buttonParams.topMargin = 20;
        layout.addView(save, buttonParams);

        setContentView(layout);
    }

    private void showMainScreen() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(20, 20, 20, 20);
        root.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);
        title.setText("🔐 MyAppLock2");
        title.setTextSize(30);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 15, 0, 15);

        root.addView(title);

        Button usageButton = new Button(this);
        usageButton.setText("⚙️ Usage Access Settings");

        usageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        Settings.ACTION_USAGE_ACCESS_SETTINGS
                ));
            }
        });

        root.addView(usageButton);

        TextView listTitle = new TextView(this);
        listTitle.setText("📱 All Installed Apps");
        listTitle.setTextSize(22);
        listTitle.setTextColor(Color.BLACK);
        listTitle.setPadding(0, 20, 0, 10);

        root.addView(listTitle);

        ScrollView scroll = new ScrollView(this);

        appList = new LinearLayout(this);
        appList.setOrientation(LinearLayout.VERTICAL);

        scroll.addView(appList);

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1
                )
        );

        loadApps();

        setContentView(root);
    }

    private void loadApps() {

        PackageManager pm = getPackageManager();

        List<ApplicationInfo> apps =
                pm.getInstalledApplications(
                        PackageManager.GET_META_DATA
                );

        Collections.sort(
                apps,
                new Comparator<ApplicationInfo>() {
                    @Override
                    public int compare(
                            ApplicationInfo a,
                            ApplicationInfo b) {

                        return pm.getApplicationLabel(a)
                                .toString()
                                .compareToIgnoreCase(
                                        pm.getApplicationLabel(b)
                                                .toString()
                                );
                    }
                }
        );

        for (ApplicationInfo app : apps) {

            if (app.packageName.equals(getPackageName())) {
                continue;
            }

            String packageName = app.packageName;
            String appName =
                    pm.getApplicationLabel(app).toString();

            Drawable icon = pm.getApplicationIcon(app);

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding(10, 12, 10, 12);

            ImageView appIcon = new ImageView(this);
            appIcon.setImageDrawable(icon);

            LinearLayout.LayoutParams iconParams =
                    new LinearLayout.LayoutParams(60, 60);

            row.addView(appIcon, iconParams);

            TextView name = new TextView(this);
            name.setText(appName);
            name.setTextSize(17);
            name.setTextColor(Color.BLACK);
            name.setGravity(Gravity.CENTER_VERTICAL);
            name.setPadding(15, 0, 10, 0);

            row.addView(
                    name,
                    new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1
                    )
            );

            Button lockButton = new Button(this);

            boolean locked = prefs.getBoolean(
                    "LOCK_" + packageName,
                    false
            );

            lockButton.setText(
                    locked ? "🔓 Unlock" : "🔒 Lock"
            );

            lockButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            boolean current =
                                    prefs.getBoolean(
                                            "LOCK_" + packageName,
                                            false
                                    );

                            prefs.edit()
                                    .putBoolean(
                                            "LOCK_" + packageName,
                                            !current
                                    )
                                    .apply();

                            lockButton.setText(
                                    !current
                                            ? "🔓 Unlock"
                                            : "🔒 Lock"
                            );

                            Toast.makeText(
                                    MainActivity.this,
                                    !current
                                            ? appName + " locked"
                                            : appName + " unlocked",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
            );

            row.addView(lockButton);
            appList.addView(row);
        }
    }
    }
