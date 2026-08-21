package com.yohannes.myapplock2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView text = new TextView(this);
        text.setText("MyAppLock2");
        text.setTextSize(28);
        text.setGravity(17);

        setContentView(text);
    }
}
