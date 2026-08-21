package com.yohannes.myapplock2;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.Settings;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
        title.setText("🔐 Create PIN");
        title.setTextSize(28);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);

        EditText pin = new EditText(this);
        pin.setHint("Enter 4-digit PIN");
        pin.setInputType(2);
        pin.setGravity(Gravity.CENTER);
        pin.setTextSize(20);

        Button save = new Button(this);
        save.setText("Save PIN");

        save.setOnClickListener(v -> {

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
        });

        layout.addView(title);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        500,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        p.topMargin = 40;
        layout.addView(pin, p);

        p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        p.topMargin = 25;
        layout.addView(save, p);

        setContentView(layout);
    }

    private void showMainScreen() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(25, 25, 25, 25);
        root.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);
        title.setText("🔐 MyAppLock2");
        title.setTextSize(30);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 20, 0, 20);

        root.addView(title);

        Button usageButton = new Button(this);
        usageButton.setText("⚙️ Usage Access Settings");

        usageButton.setOnClickListener(v ->
                startActivity(new Intent(
                        Settings.ACTION_USAGE_ACCESS_SETTINGS
                ))
        );

        root.addView(usageButton);

        TextView listTitle = new TextView(this);
        listTitle.setText("📱 Installed Apps");
        listTitle.setTextSize(22);
        listTitle.setTextColor(Color.BLACK);
        listTitle.setPadding(0, 25, 0, 10);

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

        for (ApplicationInfo app : apps) {

            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                continue;
            }

            String packageName = app.packageName;

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding(10, 10, 10, 10);

            TextView name = new TextView(this);
            name.setText(
                    pm.getApplicationLabel(app).toString()
            );
            name.setTextSize(18);
            name.setTextColor(Color.BLACK);

            Button lockButton = new Button(this);

            boolean locked =
                    prefs.getBoolean(
                            "LOCK_" + packageName,
                            false
                    );

            lockButton.setText(
                    locked ? "🔓 Unlock" : "🔒 Lock"
            );

            lockButton.setOnClickListener(v -> {

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
                                ? name.getText() + " locked"
                                : name.getText() + " unlocked",
                        Toast.LENGTH_SHORT
                ).show();
            });

            row.addView(
                    name,
                    new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1
                    )
            );

            row.addView(lockButton);

            appList.addView(row);
        }
    }
                     }
