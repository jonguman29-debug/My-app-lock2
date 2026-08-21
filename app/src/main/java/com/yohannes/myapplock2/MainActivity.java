package com.yohannes.myapplock2;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.Settings;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        message.setText(
            "App Lock\n\n" +
            "Protect your apps with MyAppLock2."
        );
        message.setTextSize(20);
        message.setTextColor(Color.DKGRAY);
        message.setGravity(Gravity.CENTER);

        Button settingsButton = new Button(this);
        settingsButton.setText("⚙️ Open App Access Settings");

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                    Settings.ACTION_USAGE_ACCESS_SETTINGS
                );
                startActivity(intent);
            }
        });

        layout.addView(title);
        layout.addView(message);

        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );

        params.topMargin = 40;
        layout.addView(settingsButton, params);

        setContentView(layout);
    }
    }
