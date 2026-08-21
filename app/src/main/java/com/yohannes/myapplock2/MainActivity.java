package com.yohannes.myapplock2;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.Settings;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.List;

public class MainActivity extends Activity {

    private LinearLayout appList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showMainScreen();
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
        listTitle.setText("📱 Installed Apps");
        listTitle.setTextSize(22);
        listTitle.setTextColor(Color.BLACK);
        listTitle.setPadding(0, 25, 0, 10);

        root.addView(listTitle);

        ScrollView scroll = new ScrollView(this);

        appList = new LinearLayout(this);
        appList.setOrientation(LinearLayout.VERTICAL);

        scroll.addView(appList);
        root.addView(scroll);

        loadApps();

        setContentView(root);
    }

    private void loadApps() {

        PackageManager pm = getPackageManager();
        List<ApplicationInfo> apps =
            pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo app : apps) {

            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                continue;
            }

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding(10, 15, 10, 15);

            TextView name = new TextView(this);
            name.setText(pm.getApplicationLabel(app).toString());
            name.setTextSize(18);
            name.setTextColor(Color.BLACK);

            Button lockButton = new Button(this);
            lockButton.setText("🔒 Lock");

            lockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lockButton.setText("🔓 Locked");
                }
            });

            row.addView(name,
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
