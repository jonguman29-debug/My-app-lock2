package com.yohannes.myapplock2;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PinActivity extends Activity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(
                "MyAppLock",
                MODE_PRIVATE
        );

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(40, 40, 40, 40);
        layout.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);
        title.setText("🔐 App Locked");
        title.setTextSize(30);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);

        EditText pin = new EditText(this);
        pin.setHint("Enter PIN");
        pin.setInputType(2);
        pin.setGravity(Gravity.CENTER);

        Button unlock = new Button(this);
        unlock.setText("🔓 Unlock");

        unlock.setOnClickListener(v -> {

            String entered =
                    pin.getText().toString();

            String saved =
                    prefs.getString("PIN", "");

            if (entered.equals(saved)) {

                finish();

            } else {

                Toast.makeText(
                        this,
                        "Wrong PIN",
                        Toast.LENGTH_SHORT
                ).show();

                pin.setText("");
            }
        });

        layout.addView(title);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        600,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        p.topMargin = 40;
        layout.addView(pin, p);

        p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        p.topMargin = 25;
        layout.addView(unlock, p);

        setContentView(layout);
    }
              }
