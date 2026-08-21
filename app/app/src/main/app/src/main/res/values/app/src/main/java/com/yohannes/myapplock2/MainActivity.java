package com.yohannes.myapplock2;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.widget.*;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(40, 40, 40, 40);
        layout.setBackgroundColor(Color.rgb(17, 24, 39));

        TextView title = new TextView(this);
        title.setText("🔐 MyAppLock2");
        title.setTextColor(Color.WHITE);
        title.setTextSize(28);
        title.setGravity(Gravity.CENTER);

        EditText pin = new EditText(this);
        pin.setHint("Enter PIN");
        pin.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        pin.setTextColor(Color.WHITE);
        pin.setHintTextColor(Color.LTGRAY);

        Button unlock = new Button(this);
        unlock.setText("UNLOCK");

        unlock.setOnClickListener(v -> {
            if (pin.getText().toString().equals("1234")) {
                Toast.makeText(this, "Unlocked ✅", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Wrong PIN ❌", Toast.LENGTH_SHORT).show();
            }
        });

        layout.addView(title);
        layout.addView(pin);
        layout.addView(unlock);

        setContentView(layout);
    }
                                  }
